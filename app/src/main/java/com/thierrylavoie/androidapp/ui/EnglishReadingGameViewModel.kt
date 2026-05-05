package com.thierrylavoie.ludo.ui

import androidx.lifecycle.ViewModel
import com.thierrylavoie.ludo.domain.ReadingGameEngineEnglish
import com.thierrylavoie.ludo.domain.ReadingTask
import com.thierrylavoie.ludo.domain.UserStatsRepository

class EnglishReadingGameViewModel(
    private val engine: ReadingGameEngineEnglish,
    private val statsRepository: UserStatsRepository
) : ViewModel() {
    var score = 0
        private set
    var roundsPlayed = 0
        private set
    
    private var availableTasks = engine.getAllTasks().shuffled().toMutableList()
    var currentTask: ReadingTask? = null
        private set
    
    var isGameOver = false
        private set

    val totalPoints: Int
        get() = statsRepository.totalPoints

    init {
        startNextRound()
    }

    fun startNextRound() {
        if (availableTasks.isNotEmpty()) {
            currentTask = availableTasks.removeAt(0)
        } else {
            currentTask = null
            isGameOver = true
        }
    }

    fun submitAnswer(selectedIndex: Int): Boolean {
        val task = currentTask ?: return false
        roundsPlayed++
        val isCorrect = engine.checkAnswer(task, selectedIndex)
        if (isCorrect) {
            score++
            statsRepository.addPoints(15)
        }
        return isCorrect
    }

    fun restart() {
        score = 0
        roundsPlayed = 0
        isGameOver = false
        availableTasks = engine.getAllTasks().shuffled().toMutableList()
        startNextRound()
    }
}
