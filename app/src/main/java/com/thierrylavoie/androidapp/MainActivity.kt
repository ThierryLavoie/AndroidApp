package com.thierrylavoie.androidapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thierrylavoie.androidapp.domain.ClockGameEngine
import com.thierrylavoie.androidapp.domain.GameMode
import com.thierrylavoie.androidapp.ui.AnalogClockView
import com.thierrylavoie.androidapp.ui.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val modeSelector = findViewById<RadioGroup>(R.id.modeSelector)
        val promptView = findViewById<TextView>(R.id.promptView)
        val scoreView = findViewById<TextView>(R.id.scoreView)
        val feedbackView = findViewById<TextView>(R.id.feedbackView)
        val analogClockView = findViewById<AnalogClockView>(R.id.analogClockView)
        val readClockAnswerSection = findViewById<View>(R.id.readClockAnswerSection)
        val answerHourInput = findViewById<EditText>(R.id.answerHourInput)
        val answerMinuteInput = findViewById<EditText>(R.id.answerMinuteInput)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val btnBackToMenu = findViewById<Button>(R.id.btnBackToMenu)
        val totalPointsView = findViewById<TextView>(R.id.totalPointsView)

        btnBackToMenu.setOnClickListener { finish() }

        fun updateModeUi() {
            val setHandsMode = viewModel.mode == GameMode.SET_HANDS
            analogClockView.isInteractive = setHandsMode
            readClockAnswerSection.visibility = if (setHandsMode) View.GONE else View.VISIBLE
            submitButton.text = if (setHandsMode) {
                getString(R.string.submit_hands_answer)
            } else {
                getString(R.string.submit_time_answer)
            }
            answerHourInput.isEnabled = !setHandsMode
            answerMinuteInput.isEnabled = !setHandsMode
        }

        fun updateScoreText() {
            scoreView.text = getString(
                R.string.score_format,
                viewModel.score,
                viewModel.roundsPlayed
            )
            totalPointsView.text = getString(R.string.user_points_format, viewModel.totalPoints)
        }

        fun renderPrompt() {
            val target = viewModel.currentTarget
            if (viewModel.mode == GameMode.SET_HANDS) {
                promptView.text = getString(R.string.mode_set_hands_prompt, target.displayText())
                analogClockView.setDisplayedTime(12, 0)
            } else {
                promptView.text = getString(R.string.mode_read_clock_prompt)
                analogClockView.setDisplayedTime(viewModel.displayHourForTarget(), target.minute)
            }
        }

        fun clearReadClockInputs() {
            answerHourInput.text?.clear()
            answerMinuteInput.text?.clear()
        }

        fun goToNextRound() {
            viewModel.startNextRound()
            clearReadClockInputs()
            renderPrompt()
        }

        analogClockView.onTimeChanged = { _, _ ->
            // no-op: the clock is visual-only for gameplay
        }

        fun syncRadiosFromStateWithoutFiringListeners() {
            modeSelector.setOnCheckedChangeListener(null)
            when (viewModel.mode) {
                GameMode.SET_HANDS -> modeSelector.check(R.id.modeSetHands)
                GameMode.READ_CLOCK -> modeSelector.check(R.id.modeReadClock)
            }
        }

        syncRadiosFromStateWithoutFiringListeners()

        modeSelector.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == View.NO_ID) return@setOnCheckedChangeListener
            val selectedMode = if (checkedId == R.id.modeSetHands) {
                GameMode.SET_HANDS
            } else {
                GameMode.READ_CLOCK
            }
            viewModel.setMode(selectedMode)
            feedbackView.text = ""
            updateModeUi()
            renderPrompt()
        }

        submitButton.setOnClickListener {
            val isCorrect = if (viewModel.mode == GameMode.SET_HANDS) {
                viewModel.submitSetHandsAnswer(analogClockView.selectedHour, analogClockView.selectedMinute)
            } else {
                val enteredHour = answerHourInput.text.toString().toIntOrNull() ?: -1
                val enteredMinute = answerMinuteInput.text.toString().toIntOrNull() ?: -1
                viewModel.submitReadClockAnswer(enteredHour, enteredMinute)
            }

            feedbackView.text = if (isCorrect) {
                getString(R.string.correct_feedback)
            } else {
                getString(R.string.wrong_feedback, viewModel.currentTarget.displayText())
            }
            updateScoreText()
            goToNextRound()
        }

        updateModeUi()
        updateScoreText()
        renderPrompt()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

private class MainViewModelFactory(private val context: android.content.Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val repository = com.thierrylavoie.androidapp.domain.UserStatsRepository(context)
            return MainViewModel(ClockGameEngine(), repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
