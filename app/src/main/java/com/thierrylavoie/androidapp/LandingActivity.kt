package com.thierrylavoie.androidapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.google.android.material.button.MaterialButton

class LandingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        val btnClockGame = findViewById<MaterialButton>(R.id.btnClockGame)
        val btnMentalMath = findViewById<MaterialButton>(R.id.btnMentalMath)
        val languageSelector = findViewById<RadioGroup>(R.id.languageSelector)

        btnClockGame.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        btnMentalMath.setOnClickListener {
            startActivity(Intent(this, MentalCalculationActivity::class.java))
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
