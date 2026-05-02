package com.thierrylavoie.androidapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.google.android.material.button.MaterialButton
import com.thierrylavoie.androidapp.domain.UserStatsRepository

class LandingActivity : AppCompatActivity() {

    private lateinit var statsRepository: UserStatsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        statsRepository = UserStatsRepository(this)

        val btnClockGame = findViewById<MaterialButton>(R.id.btnClockGame)
        val btnMentalMath = findViewById<MaterialButton>(R.id.btnMentalMath)
        val btnReadingGame = findViewById<MaterialButton>(R.id.btnReadingGame)
        val languageSelector = findViewById<RadioGroup>(R.id.languageSelector)

        btnClockGame.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        btnMentalMath.setOnClickListener {
            startActivity(Intent(this, MentalCalculationActivity::class.java))
        }

        btnReadingGame.setOnClickListener {
            startActivity(Intent(this, ReadingGameActivity::class.java))
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
    }

    private fun updateStatsUi() {
        val totalPointsView = findViewById<TextView>(R.id.totalPointsView)
        val userRankView = findViewById<TextView>(R.id.userRankView)
        totalPointsView.text = getString(R.string.user_points_format, statsRepository.totalPoints)
        userRankView.text = getString(R.string.user_rank_format, statsRepository.getRank())
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
