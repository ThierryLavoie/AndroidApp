package com.thierrylavoie.ludo

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thierrylavoie.ludo.domain.SpellingGameEngine
import com.thierrylavoie.ludo.ui.SpellingViewModel
import java.util.*

class SpellingGameActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private val viewModel: SpellingViewModel by viewModels {
        SpellingViewModelFactory(applicationContext)
    }

    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    private lateinit var scoreView: TextView
    private lateinit var totalPointsView: TextView
    private lateinit var feedbackView: TextView
    private lateinit var answerInput: EditText
    private lateinit var submitButton: Button
    private lateinit var btnListen: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spelling_game)

        tts = TextToSpeech(this, this)

        scoreView = findViewById(R.id.scoreView)
        totalPointsView = findViewById(R.id.totalPointsView)
        feedbackView = findViewById(R.id.feedbackView)
        answerInput = findViewById(R.id.answerInput)
        submitButton = findViewById(R.id.submitButton)
        btnListen = findViewById(R.id.btnListen)

        findViewById<Button>(R.id.btnBackToMenu).setOnClickListener { finish() }

        btnListen.setOnClickListener {
            speakWord()
        }

        submitButton.setOnClickListener {
            checkAnswer()
        }

        updateUi()
        if (viewModel.roundsPlayed == 0) {
            viewModel.nextRound(getCurrentLanguage())
        }
    }

    private fun getCurrentLanguage(): String {
        return androidx.appcompat.app.AppCompatDelegate.getApplicationLocales()[0]?.language ?: "en"
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isTtsReady = true
            updateTtsLanguage()
        }
    }

    private fun updateTtsLanguage() {
        val lang = if (getCurrentLanguage().startsWith("fr")) Locale.FRENCH else Locale.ENGLISH
        tts?.language = lang
    }

    private fun speakWord() {
        if (isTtsReady) {
            updateTtsLanguage()
            val word = viewModel.currentWord.word
            tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, "spelling_word")
        } else {
            feedbackView.setTextColor(getColor(R.color.playful_blue))
            feedbackView.text = getString(R.string.tts_initializing)
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                speakWord()
            }, 1000)
        }
    }

    private fun checkAnswer() {
        val input = answerInput.text.toString()
        if (input.isBlank()) return

        val isCorrect = viewModel.submitAnswer(input, getCurrentLanguage())
        
        submitButton.isEnabled = false
        feedbackView.text = if (isCorrect) {
            feedbackView.setTextColor(getColor(R.color.fresh_green))
            "🎉 " + getString(R.string.correct_feedback)
        } else {
            feedbackView.setTextColor(getColor(R.color.vibrant_pink))
            "❌ " + getString(R.string.wrong_feedback, viewModel.currentWord.word)
        }

        feedbackView.scaleX = 0f
        feedbackView.scaleY = 0f
        feedbackView.animate().scaleX(1.2f).scaleY(1.2f).setDuration(300).start()

        updateUi()

        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            feedbackView.text = ""
            submitButton.isEnabled = true
            answerInput.text.clear()
            viewModel.nextRound(getCurrentLanguage())
            speakWord()
        }, 2000)
    }

    private fun updateUi() {
        scoreView.text = getString(R.string.score_format, viewModel.score, viewModel.roundsPlayed)
        totalPointsView.text = getString(R.string.user_points_format, viewModel.totalPoints)
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}

private class SpellingViewModelFactory(private val context: android.content.Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpellingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val repository = com.thierrylavoie.ludo.domain.UserStatsRepository(context)
            return SpellingViewModel(SpellingGameEngine(), repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
