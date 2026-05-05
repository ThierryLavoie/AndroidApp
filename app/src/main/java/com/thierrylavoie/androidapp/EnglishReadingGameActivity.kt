package com.thierrylavoie.ludo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thierrylavoie.ludo.domain.ReadingGameEngineEnglish
import com.thierrylavoie.ludo.domain.UserStatsRepository
import com.thierrylavoie.ludo.ui.EnglishReadingGameViewModel

class EnglishReadingGameActivity : AppCompatActivity() {

    private val viewModel: EnglishReadingGameViewModel by viewModels {
        EnglishReadingGameViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading_game)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val readingTitle = findViewById<TextView>(R.id.readingTitle)
        readingTitle.text = getString(R.string.reading_title_en)
        
        val textContainer = findViewById<TextView>(R.id.textContainer)
        val questionContainer = findViewById<TextView>(R.id.questionContainer)
        val optionsGroup = findViewById<RadioGroup>(R.id.optionsGroup)
        val scoreView = findViewById<TextView>(R.id.scoreView)
        val totalPointsView = findViewById<TextView>(R.id.totalPointsView)
        val feedbackView = findViewById<TextView>(R.id.feedbackView)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val btnRestart = findViewById<Button>(R.id.btnRestart)
        val btnBackToMenu = findViewById<Button>(R.id.btnBackToMenu)

        btnBackToMenu.setOnClickListener { finish() }

        fun updateScoreText() {
            scoreView.text = getString(
                R.string.score_format,
                viewModel.score,
                viewModel.roundsPlayed
            )
            totalPointsView.text = getString(R.string.user_points_format, viewModel.totalPoints)
        }

        fun renderRound() {
            if (viewModel.isGameOver) {
                textContainer.text = getString(R.string.game_over_reading)
                questionContainer.text = ""
                optionsGroup.visibility = View.GONE
                submitButton.visibility = View.GONE
                btnRestart.visibility = View.VISIBLE
                return
            }

            val task = viewModel.currentTask ?: return
            textContainer.text = task.text
            questionContainer.text = task.question
            optionsGroup.visibility = View.VISIBLE
            optionsGroup.clearCheck()
            
            val radio1 = findViewById<RadioButton>(R.id.option1)
            val radio2 = findViewById<RadioButton>(R.id.option2)
            val radio3 = findViewById<RadioButton>(R.id.option3)
            val radio4 = findViewById<RadioButton>(R.id.option4)
            
            radio1.text = task.options[0]
            radio2.text = task.options[1]
            radio3.text = task.options[2]
            radio4.text = task.options[3]
        }

        btnRestart.setOnClickListener {
            viewModel.restart()
            feedbackView.text = ""
            btnRestart.visibility = View.GONE
            submitButton.visibility = View.VISIBLE
            optionsGroup.visibility = View.VISIBLE
            updateScoreText()
            renderRound()
        }

        submitButton.setOnClickListener {
            val checkedId = optionsGroup.checkedRadioButtonId
            if (checkedId == -1) {
                feedbackView.text = getString(R.string.invalid_input)
                return@setOnClickListener
            }

            val selectedIndex = when (checkedId) {
                R.id.option1 -> 0
                R.id.option2 -> 1
                R.id.option3 -> 2
                R.id.option4 -> 3
                else -> -1
            }

            val taskBeforeSubmit = viewModel.currentTask
            val isCorrect = viewModel.submitAnswer(selectedIndex)
            feedbackView.text = if (isCorrect) {
                getString(R.string.correct_feedback)
            } else {
                getString(R.string.wrong_feedback, taskBeforeSubmit?.options?.get(taskBeforeSubmit.correctOptionIndex) ?: "")
            }

            updateScoreText()
            viewModel.startNextRound()
            renderRound()
        }

        updateScoreText()
        renderRound()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

private class EnglishReadingGameViewModelFactory(private val context: android.content.Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EnglishReadingGameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val repository = UserStatsRepository(context)
            return EnglishReadingGameViewModel(ReadingGameEngineEnglish(), repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
