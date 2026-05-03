package com.thierrylavoie.androidapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.thierrylavoie.androidapp.domain.ShopManager
import com.thierrylavoie.androidapp.domain.UserStatsRepository

class ShopActivity : AppCompatActivity() {

    private lateinit var statsRepository: UserStatsRepository
    private lateinit var adapter: ShopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        statsRepository = UserStatsRepository(this)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        updatePointsDisplay()

        val recyclerView = findViewById<RecyclerView>(R.id.shopRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ShopAdapter()
        recyclerView.adapter = adapter
    }

    private fun updatePointsDisplay() {
        val pointsView = findViewById<TextView>(R.id.shopTotalPoints)
        pointsView.text = getString(R.string.user_points_format, statsRepository.totalPoints)
    }

    private inner class ShopAdapter : RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

        private val items = ShopManager.items

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val icon: TextView = view.findViewById(R.id.itemIcon)
            val name: TextView = view.findViewById(R.id.itemName)
            val price: TextView = view.findViewById(R.id.itemPrice)
            val btnAction: MaterialButton = view.findViewById(R.id.btnAction)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            val isFrench = AppCompatDelegate.getApplicationLocales()[0]?.language?.startsWith("fr") == true
            
            holder.icon.text = item.icon
            holder.name.text = if (isFrench) item.nameFr else item.nameEn
            holder.price.text = holder.itemView.context.getString(R.string.item_price_format, item.price)

            val isUnlocked = statsRepository.isItemUnlocked(item.id)
            val isEquipped = statsRepository.getEquippedItem(item.category) == item.id

            when {
                isEquipped -> {
                    holder.btnAction.text = getString(R.string.shop_equipped)
                    holder.btnAction.isEnabled = false
                }
                isUnlocked -> {
                    holder.btnAction.text = getString(R.string.shop_equip)
                    holder.btnAction.isEnabled = true
                    holder.btnAction.setOnClickListener {
                        statsRepository.equipItem(item.category, item.id)
                        notifyDataSetChanged()
                    }
                }
                else -> {
                    holder.btnAction.text = getString(R.string.shop_buy, item.price)
                    holder.btnAction.isEnabled = true
                    holder.btnAction.setOnClickListener {
                        if (statsRepository.spendPoints(item.price)) {
                            statsRepository.unlockItem(item.id)
                            statsRepository.equipItem(item.category, item.id)
                            updatePointsDisplay()
                            notifyDataSetChanged()
                        } else {
                            Toast.makeText(this@ShopActivity, R.string.not_enough_points, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        override fun getItemCount() = items.size
    }
}
