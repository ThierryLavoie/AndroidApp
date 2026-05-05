package com.thierrylavoie.ludo.ui

import androidx.lifecycle.ViewModel
import com.thierrylavoie.ludo.domain.MathLevel
import com.thierrylavoie.ludo.domain.MathOperation
import com.thierrylavoie.ludo.domain.MentalMathEngine
import com.thierrylavoie.ludo.domain.UserStatsRepository

class MentalMathViewModel(
    private val engine: MentalMathEngine,
    private val statsRepository: UserStatsRepository
) : ViewModel() {
    var score = 0
        private set
    var roundsPlayed = 0
        private set
    lateinit var currentOperation: MathOperation
        private set
    var level: MathLevel = MathLevel.GRADE_1
        private set

    val totalPoints: Int
        get() = statsRepository.totalPoints

    init {
        startNextRound()
    }

    fun setLevel(newLevel: MathLevel) {
        level = newLevel
        score = 0
        roundsPlayed = 0
        startNextRound()
    }

    fun startNextRound() {
        currentOperation = engine.nextRound(level)
    }

    fun submitAnswer(answer: Int): Boolean {
        roundsPlayed++
        val isCorrect = engine.checkAnswer(currentOperation, answer)
        if (isCorrect) {
            score++
            val points = when(level) {
                MathLevel.GRADE_1 -> 5
                MathLevel.GRADE_2 -> 10
                MathLevel.GRADE_3 -> 15
                MathLevel.GRADE_4 -> 20
                MathLevel.GRADE_5 -> 25
            }
            statsRepository.addPoints(points)
        }
        return isCorrect
    }
}
