package com.thierrylavoie.androidapp.ui

import androidx.lifecycle.ViewModel
import com.thierrylavoie.androidapp.domain.MathLevel
import com.thierrylavoie.androidapp.domain.MathOperation
import com.thierrylavoie.androidapp.domain.MentalMathEngine

class MentalMathViewModel(private val engine: MentalMathEngine) : ViewModel() {
    var score = 0
        private set
    var roundsPlayed = 0
        private set
    lateinit var currentOperation: MathOperation
        private set
    var level: MathLevel = MathLevel.GRADE_1
        private set

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
        }
        return isCorrect
    }
}
