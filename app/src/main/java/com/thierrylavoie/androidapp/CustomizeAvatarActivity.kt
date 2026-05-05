package com.thierrylavoie.androidapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.thierrylavoie.androidapp.domain.ShopManager
import com.thierrylavoie.androidapp.domain.UserStatsRepository

class CustomizeAvatarActivity : AppCompatActivity() {

    private lateinit var statsRepository: UserStatsRepository
    private lateinit var inventoryAdapter: InventoryAdapter
    private var currentCategory = "BASE"
    private var dX = 0f
    private var dY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customize_avatar)

        statsRepository = UserStatsRepository(this)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        updatePointsDisplay()

        findViewById<Button>(R.id.btnReset).setOnClickListener {
            resetPositions()
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            saveLayout()
            finish()
        }

        setupTabs()
        setupRecyclerView()
        setupAvatar()
    }

    override fun onResume() {
        super.onResume()
        updatePointsDisplay()
        inventoryAdapter.refresh()
    }

    private fun updatePointsDisplay() {
        findViewById<TextView>(R.id.pointsDisplay).text = 
            getString(R.string.user_points_format, statsRepository.totalPoints)
    }

    private fun setupTabs() {
        val tabs = findViewById<TabLayout>(R.id.categoryTabs)
        val categories = listOf(
            "BASE" to R.string.cat_base,
            "HAT" to R.string.cat_hat,
            "GLASSES" to R.string.cat_glasses,
            "CHEST" to R.string.cat_chest,
            "COMPANION" to R.string.cat_companion
        )
        for (cat in categories) {
            tabs.addTab(tabs.newTab().setText(getString(cat.second)).setTag(cat.first))
        }

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentCategory = tab?.tag?.toString() ?: "BASE"
                inventoryAdapter.updateCategory(currentCategory)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupRecyclerView() {
        val rv = findViewById<RecyclerView>(R.id.inventoryRecyclerView)
        rv.layoutManager = GridLayoutManager(this, 4)
        inventoryAdapter = InventoryAdapter()
        rv.adapter = inventoryAdapter
    }

    private fun resetPositions() {
        val categories = listOf("HAT", "GLASSES", "CHEST", "COMPANION")
        for (cat in categories) {
            statsRepository.setAccessoryOffset(cat, 0f, 0f)
        }
        setupAvatar()
    }

    private fun setupAvatar() {
        val baseView = findViewById<TextView>(R.id.custAvatarBase)
        val hatView = findViewById<TextView>(R.id.custAvatarHat)
        val glassesView = findViewById<TextView>(R.id.custAvatarGlasses)
        val chestView = findViewById<TextView>(R.id.custAvatarChest)
        val companionView = findViewById<TextView>(R.id.custAvatarCompanion)

        val equippedBaseId = statsRepository.getEquippedItem("BASE") ?: "base_default"
        val equippedHatId = statsRepository.getEquippedItem("HAT")
        val equippedGlassesId = statsRepository.getEquippedItem("GLASSES")
        val equippedChestId = statsRepository.getEquippedItem("CHEST")
        val equippedCompanionId = statsRepository.getEquippedItem("COMPANION")

        baseView.text = ShopManager.items.find { it.id == equippedBaseId }?.icon ?: "😶"
        
        setupItem(hatView, "HAT", equippedHatId)
        setupItem(glassesView, "GLASSES", equippedGlassesId)
        setupItem(chestView, "CHEST", equippedChestId)
        setupItem(companionView, "COMPANION", equippedCompanionId)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupItem(view: TextView, category: String, itemId: String?) {
        val item = ShopManager.items.find { it.id == itemId }
        if (item == null) {
            view.isVisible = false
            return
        }

        view.text = item.icon
        view.isVisible = true

        val (savedX, savedY) = statsRepository.getAccessoryOffset(category)
        view.translationX = savedX
        view.translationY = savedY

        view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = v.translationX - event.rawX
                    dY = v.translationY - event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    v.translationX = event.rawX + dX
                    v.translationY = event.rawY + dY
                }
            }
            true
        }
    }

    private fun saveLayout() {
        val categories = mapOf(
            "HAT" to R.id.custAvatarHat,
            "GLASSES" to R.id.custAvatarGlasses,
            "CHEST" to R.id.custAvatarChest,
            "COMPANION" to R.id.custAvatarCompanion
        )

        for ((category, id) in categories) {
            val view = findViewById<TextView>(id)
            if (view.isVisible) {
                statsRepository.setAccessoryOffset(category, view.translationX, view.translationY)
            }
        }
    }

    private inner class InventoryAdapter : RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {
        private var items = ShopManager.getItemsByCategory(currentCategory)

        @SuppressLint("NotifyDataSetChanged")
        fun updateCategory(category: String) {
            items = ShopManager.getItemsByCategory(category)
            notifyDataSetChanged()
        }

        @SuppressLint("NotifyDataSetChanged")
        fun refresh() {
            notifyDataSetChanged()
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val card: MaterialCardView = view.findViewById(R.id.itemCard)
            val icon: TextView = view.findViewById(R.id.itemIcon)
            val check: ImageView = view.findViewById(R.id.checkMark)
            val lock: ImageView = view.findViewById(R.id.lockIcon)
            val price: TextView = view.findViewById(R.id.itemPrice)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_inventory, parent, false)
            return ViewHolder(view)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.icon.text = item.icon
            
            val isUnlocked = (item.price == 0) || statsRepository.isItemUnlocked(item.id)
            val isEquipped = statsRepository.getEquippedItem(item.category) == item.id || 
                             (item.id == "base_default" && statsRepository.getEquippedItem("BASE") == null)

            holder.lock.isVisible = !isUnlocked
            holder.price.isVisible = !isUnlocked
            if (!isUnlocked) {
                holder.price.text = getString(R.string.item_price_format, item.price)
            }
            
            holder.check.isVisible = isEquipped
            holder.card.strokeWidth = if (isEquipped) 6 else 0

            holder.itemView.setOnClickListener {
                if (!isUnlocked) {
                    val isFrench = AppCompatDelegate.getApplicationLocales()[0]?.language?.startsWith("fr") == true
                    val name = if (isFrench) item.nameFr else item.nameEn
                    
                    AlertDialog.Builder(this@CustomizeAvatarActivity)
                        .setTitle(R.string.shop_title)
                        .setMessage(getString(R.string.buy_confirmation, name, item.price))
                        .setPositiveButton(R.string.shop_buy_confirm_label) { _, _ ->
                            if (statsRepository.spendPoints(item.price)) {
                                statsRepository.unlockItem(item.id)
                                statsRepository.equipItem(item.category, item.id)
                                updatePointsDisplay()
                                setupAvatar()
                                notifyDataSetChanged()
                            } else {
                                Toast.makeText(this@CustomizeAvatarActivity, R.string.not_enough_points, Toast.LENGTH_SHORT).show()
                            }
                        }
                        .setNegativeButton(R.string.btn_cancel, null)
                        .show()
                } else {
                    if (isEquipped) {
                        if (item.category != "BASE") {
                            statsRepository.equipItem(item.category, null)
                        }
                    } else {
                        statsRepository.equipItem(item.category, item.id)
                    }
                }
                setupAvatar()
                notifyDataSetChanged()
            }
        }

        override fun getItemCount() = items.size
    }
}
