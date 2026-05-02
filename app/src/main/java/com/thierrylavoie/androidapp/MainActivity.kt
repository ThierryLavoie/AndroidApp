package com.thierrylavoie.androidapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
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
        MainViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val modeSelector = findViewById<RadioGroup>(R.id.modeSelector)
        val promptView = findViewById<TextView>(R.id.promptView)
        val scoreView = findViewById<TextView>(R.id.scoreView)
        val feedbackView = findViewById<TextView>(R.id.feedbackView)
        val analogClockView = findViewById<AnalogClockView>(R.id.analogClockView)
        val handSelector = findViewById<RadioGroup>(R.id.handSelector)
        val readClockAnswerSection = findViewById<View>(R.id.readClockAnswerSection)
        val selectHourHand = findViewById<RadioButton>(R.id.selectHourHand)
        val answerHourInput = findViewById<EditText>(R.id.answerHourInput)
        val answerMinuteInput = findViewById<EditText>(R.id.answerMinuteInput)
        val answerPeriodSelector = findViewById<RadioGroup>(R.id.answerPeriodSelector)
        val submitButton = findViewById<Button>(R.id.submitButton)

        fun updateModeUi() {
            val setHandsMode = viewModel.mode == GameMode.SET_HANDS
            analogClockView.isInteractive = setHandsMode
            readClockAnswerSection.visibility = if (setHandsMode) View.GONE else View.VISIBLE
            handSelector.isEnabled = setHandsMode
            for (i in 0 until handSelector.childCount) {
                handSelector.getChildAt(i).isEnabled = setHandsMode
            }
            submitButton.text = if (setHandsMode) {
                getString(R.string.submit_hands_answer)
            } else {
                getString(R.string.submit_time_answer)
            }
            answerHourInput.isEnabled = !setHandsMode
            answerMinuteInput.isEnabled = !setHandsMode
            for (i in 0 until answerPeriodSelector.childCount) {
                answerPeriodSelector.getChildAt(i).isEnabled = !setHandsMode
            }
        }

        fun updateScoreText() {
            scoreView.text = getString(
                R.string.score_format,
                viewModel.score,
                viewModel.roundsPlayed
            )
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
            answerPeriodSelector.check(R.id.answerAm)
        }

        fun goToNextRound() {
            viewModel.startNextRound()
            clearReadClockInputs()
            renderPrompt()
        }

        analogClockView.onTimeChanged = { _, _ ->
            // no-op: the clock is visual-only for gameplay
        }

        handSelector.setOnCheckedChangeListener { _, checkedId ->
            analogClockView.activeHand = if (checkedId == R.id.selectMinuteHand) {
                AnalogClockView.ActiveHand.MINUTE
            } else {
                AnalogClockView.ActiveHand.HOUR
            }
        }

        modeSelector.setOnCheckedChangeListener { _, checkedId ->
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
                val isPm = answerPeriodSelector.checkedRadioButtonId == R.id.answerPm
                viewModel.submitReadClockAnswer(enteredHour, enteredMinute, isPm)
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
        selectHourHand.isChecked = true
        analogClockView.activeHand = AnalogClockView.ActiveHand.HOUR
        renderPrompt()
    }
}

private class MainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(ClockGameEngine()) as T
    }
}
