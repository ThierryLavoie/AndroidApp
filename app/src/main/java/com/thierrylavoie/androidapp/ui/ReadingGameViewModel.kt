package com.thierrylavoie.androidapp.ui

import androidx.lifecycle.ViewModel
import com.thierrylavoie.androidapp.domain.ReadingGameEngine
import com.thierrylavoie.androidapp.domain.ReadingTask
import com.thierrylavoie.androidapp.domain.UserStatsRepository

class ReadingGameViewModel(
    private val engine: ReadingGameEngine,
    private val statsRepository: UserStatsRepository
) : ViewModel() {
    var score = 0
        private set
    var roundsPlayed = 0
        private set
    lateinit var currentTask: ReadingTask
        private set

    val totalPoints: Int
        get() = statsRepository.totalPoints

    init {
        startNextRound()
    }

    fun startNextRound() {
        currentTask = engine.nextRound()
    }

    fun submitAnswer(selectedIndex: Int): Boolean {
        roundsPlayed++
        val isCorrect = engine.checkAnswer(currentTask, selectedIndex)
        if (isCorrect) {
            score++
            statsRepository.addPoints(15)
        }
        return isCorrect
    }
}
