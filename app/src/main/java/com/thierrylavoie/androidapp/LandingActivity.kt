package com.thierrylavoie.ludo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.google.android.material.button.MaterialButton
import com.thierrylavoie.ludo.domain.ShopManager
import com.thierrylavoie.ludo.domain.UserStatsRepository

class LandingActivity : AppCompatActivity() {

    private lateinit var statsRepository: UserStatsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        statsRepository = UserStatsRepository(this)

        val btnClockGame = findViewById<MaterialButton>(R.id.btnClockGame)
        val btnMentalMath = findViewById<MaterialButton>(R.id.btnMentalMath)
        val btnReadingGame = findViewById<MaterialButton>(R.id.btnReadingGame)
        val btnReadingGameEn = findViewById<MaterialButton>(R.id.btnReadingGameEn)
        val btnShop = findViewById<MaterialButton>(R.id.btnShop)
        val languageSelector = findViewById<RadioGroup>(R.id.languageSelector)

        btnShop.setOnClickListener {
            startActivity(Intent(this, ShopActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btnCustomize).setOnClickListener {
            startActivity(Intent(this, CustomizeAvatarActivity::class.java))
        }

        btnClockGame.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        btnMentalMath.setOnClickListener {
            startActivity(Intent(this, MentalCalculationActivity::class.java))
        }

        btnReadingGame.setOnClickListener {
            startActivity(Intent(this, ReadingGameActivity::class.java))
        }

        btnReadingGameEn.setOnClickListener {
            startActivity(Intent(this, EnglishReadingGameActivity::class.java))
        }

        syncLanguageSelector()

        languageSelector.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == View.NO_ID) return@setOnCheckedChangeListener
            val lang = if (checkedId == R.id.languageFrench) "fr" else "en"
            val currentLang = AppCompatDelegate.getApplicationLocales()[0]?.language ?: "en"
            if (lang != currentLang) {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(lang))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateStatsUi()
        updateAvatarUi()
    }

    private fun updateAvatarUi() {
        val baseView = findViewById<TextView>(R.id.avatarBase)
        val hatView = findViewById<TextView>(R.id.avatarHat)
        val glassesView = findViewById<TextView>(R.id.avatarGlasses)
        val chestView = findViewById<TextView>(R.id.avatarChest)
        val companionView = findViewById<TextView>(R.id.avatarCompanion)

        val equippedBaseId = statsRepository.getEquippedItem("BASE") ?: "base_default"
        val equippedHatId = statsRepository.getEquippedItem("HAT")
        val equippedGlassesId = statsRepository.getEquippedItem("GLASSES")
        val equippedChestId = statsRepository.getEquippedItem("CHEST")
        val equippedCompanionId = statsRepository.getEquippedItem("COMPANION")

        baseView.text = ShopManager.items.find { it.id == equippedBaseId }?.icon ?: "😶"
        
        applyItem(hatView, "HAT", equippedHatId)
        applyItem(glassesView, "GLASSES", equippedGlassesId)
        applyItem(chestView, "CHEST", equippedChestId)
        applyItem(companionView, "COMPANION", equippedCompanionId)
    }

    private fun applyItem(view: TextView, category: String, itemId: String?) {
        val item = ShopManager.items.find { it.id == itemId }
        if (item == null) {
            view.visibility = View.GONE
            return
        }
        view.text = item.icon
        view.visibility = View.VISIBLE
        val (ox, oy) = statsRepository.getAccessoryOffset(category)
        
        // Scale down the offset for the smaller landing menu avatar if necessary
        // The landing avatar is 200dp, the customizer is 260dp. Ratio is ~0.77
        view.translationX = ox * 0.77f
        view.translationY = oy * 0.77f
    }

    private fun updateStatsUi() {
        val totalPointsView = findViewById<TextView>(R.id.totalPointsView)
        val userRankView = findViewById<TextView>(R.id.userRankView)
        totalPointsView.text = getString(R.string.user_points_format, statsRepository.totalPoints)
        
        val rankKey = statsRepository.getRank()
        val rankResId = resources.getIdentifier(rankKey, "string", packageName)
        val rankString = if (rankResId != 0) getString(rankResId) else rankKey
        
        userRankView.text = getString(R.string.user_rank_format, rankString)
    }

    private fun syncLanguageSelector() {
        val languageSelector = findViewById<RadioGroup>(R.id.languageSelector)
        val appLanguage = AppCompatDelegate.getApplicationLocales()[0]?.language ?: "en"
        if (appLanguage.startsWith("fr")) {
            languageSelector.check(R.id.languageFrench)
        } else {
            languageSelector.check(R.id.languageEnglish)
        }
    }
}
