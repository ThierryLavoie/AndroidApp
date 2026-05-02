package com.thierrylavoie.androidapp.ui

import androidx.lifecycle.ViewModel
import com.thierrylavoie.androidapp.domain.ClockGameEngine
import com.thierrylavoie.androidapp.domain.ClockTime
import com.thierrylavoie.androidapp.domain.GameMode

class MainViewModel(
    private val gameEngine: ClockGameEngine
) : ViewModel() {

    var mode: GameMode = GameMode.SET_HANDS
        private set

    var score: Int = 0
        private set

    var roundsPlayed: Int = 0
        private set

    var currentTarget: ClockTime = gameEngine.nextRound()
        private set

    fun setMode(newMode: GameMode) {
        mode = newMode
        currentTarget = gameEngine.nextRound()
    }

    fun submitSetHandsAnswer(hourHand: Int, minuteHand: Int): Boolean {
        val isCorrect = gameEngine.checkSetHandsAnswer(currentTarget, hourHand, minuteHand)
        updateScore(isCorrect)
        return isCorrect
    }

    fun submitReadClockAnswer(hour: Int, minute: Int, isPm: Boolean): Boolean {
        val isCorrect = gameEngine.checkReadClockAnswer(currentTarget, hour, minute, isPm)
        updateScore(isCorrect)
        return isCorrect
    }

    fun startNextRound() {
        currentTarget = gameEngine.nextRound()
    }

    fun displayHourForTarget(): Int {
        return gameEngine.toDisplayHour(currentTarget.hour12)
    }

    private fun updateScore(isCorrect: Boolean) {
        roundsPlayed += 1
        if (isCorrect) {
            score += 1
        }
    }
}
