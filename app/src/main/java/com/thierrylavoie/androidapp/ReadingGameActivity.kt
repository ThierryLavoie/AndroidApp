package com.thierrylavoie.androidapp

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thierrylavoie.androidapp.domain.ReadingGameEngine
import com.thierrylavoie.androidapp.domain.UserStatsRepository
import com.thierrylavoie.androidapp.ui.ReadingGameViewModel

class ReadingGameActivity : AppCompatActivity() {

    private val viewModel: ReadingGameViewModel by viewModels {
        ReadingGameViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading_game)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val textContainer = findViewById<TextView>(R.id.textContainer)
        val questionContainer = findViewById<TextView>(R.id.questionContainer)
        val optionsGroup = findViewById<RadioGroup>(R.id.optionsGroup)
        val scoreView = findViewById<TextView>(R.id.scoreView)
        val totalPointsView = findViewById<TextView>(R.id.totalPointsView)
        val feedbackView = findViewById<TextView>(R.id.feedbackView)
        val submitButton = findViewById<Button>(R.id.submitButton)
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
            val task = viewModel.currentTask
            textContainer.text = task.text
            questionContainer.text = task.question
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

            val isCorrect = viewModel.submitAnswer(selectedIndex)
            feedbackView.text = if (isCorrect) {
                getString(R.string.correct_feedback)
            } else {
                getString(R.string.wrong_feedback, viewModel.currentTask.options[viewModel.currentTask.correctOptionIndex])
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

private class ReadingGameViewModelFactory(private val context: android.content.Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReadingGameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val repository = UserStatsRepository(context)
            return ReadingGameViewModel(ReadingGameEngine(), repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
