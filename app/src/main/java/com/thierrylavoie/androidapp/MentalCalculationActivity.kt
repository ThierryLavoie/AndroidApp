package com.thierrylavoie.androidapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thierrylavoie.androidapp.domain.MathLevel
import com.thierrylavoie.androidapp.domain.MentalMathEngine
import com.thierrylavoie.androidapp.ui.MentalMathViewModel

class MentalCalculationActivity : AppCompatActivity() {

    private val viewModel: MentalMathViewModel by viewModels {
        MentalMathViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mental_calculation)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val operationPrompt = findViewById<TextView>(R.id.operationPrompt)
        val scoreView = findViewById<TextView>(R.id.scoreView)
        val feedbackView = findViewById<TextView>(R.id.feedbackView)
        val answerInput = findViewById<EditText>(R.id.answerInput)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val levelSelector = findViewById<RadioGroup>(R.id.levelSelector)
        val btnBackToMenu = findViewById<Button>(R.id.btnBackToMenu)
        val totalPointsView = findViewById<TextView>(R.id.totalPointsView)

        btnBackToMenu.setOnClickListener { finish() }

        fun updateScoreText() {
            scoreView.text = getString(
                R.string.score_format,
                viewModel.score,
                viewModel.roundsPlayed
            )
            totalPointsView.text = getString(R.string.user_points_format, viewModel.totalPoints)
        }

        fun renderPrompt() {
            val op = viewModel.currentOperation
            operationPrompt.text = getString(R.string.math_prompt, op.left, op.operator, op.right)
            answerInput.text?.clear()
        }

        levelSelector.setOnCheckedChangeListener { _, checkedId ->
            val level = when (checkedId) {
                R.id.levelGrade1 -> MathLevel.GRADE_1
                R.id.levelGrade2 -> MathLevel.GRADE_2
                R.id.levelGrade3 -> MathLevel.GRADE_3
                R.id.levelGrade4 -> MathLevel.GRADE_4
                R.id.levelGrade5 -> MathLevel.GRADE_5
                else -> MathLevel.GRADE_1
            }
            viewModel.setLevel(level)
            feedbackView.text = ""
            updateScoreText()
            renderPrompt()
        }

        // Initialize level selector
        when (viewModel.level) {
            MathLevel.GRADE_1 -> levelSelector.check(R.id.levelGrade1)
            MathLevel.GRADE_2 -> levelSelector.check(R.id.levelGrade2)
            MathLevel.GRADE_3 -> levelSelector.check(R.id.levelGrade3)
            MathLevel.GRADE_4 -> levelSelector.check(R.id.levelGrade4)
            MathLevel.GRADE_5 -> levelSelector.check(R.id.levelGrade5)
        }

        submitButton.setOnClickListener {
            val answer = answerInput.text.toString().toIntOrNull()
            if (answer == null) {
                feedbackView.text = getString(R.string.invalid_input)
                return@setOnClickListener
            }

            val isCorrect = viewModel.submitAnswer(answer)
            feedbackView.text = if (isCorrect) {
                getString(R.string.correct_feedback)
            } else {
                getString(R.string.wrong_feedback, viewModel.currentOperation.result.toString())
            }

            updateScoreText()
            viewModel.startNextRound()
            renderPrompt()
        }

        updateScoreText()
        renderPrompt()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

private class MentalMathViewModelFactory(private val context: android.content.Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MentalMathViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val repository = com.thierrylavoie.androidapp.domain.UserStatsRepository(context)
            return MentalMathViewModel(MentalMathEngine(), repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
