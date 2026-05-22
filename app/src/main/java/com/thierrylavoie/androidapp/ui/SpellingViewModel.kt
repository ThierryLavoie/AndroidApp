package com.thierrylavoie.ludo.ui

import androidx.lifecycle.ViewModel
import com.thierrylavoie.ludo.domain.SpellingGameEngine
import com.thierrylavoie.ludo.domain.SpellingWord
import com.thierrylavoie.ludo.domain.UserStatsRepository

class SpellingViewModel(
    private val engine: SpellingGameEngine,
    private val statsRepository: UserStatsRepository
) : ViewModel() {

    var score = 0
        private set
    var roundsPlayed = 0
        private set
    lateinit var currentWord: SpellingWord
        private set

    val totalPoints: Int
        get() = statsRepository.totalPoints

    fun startNewGame(language: String) {
        score = 0
        roundsPlayed = 0
        nextRound(language)
    }

    fun nextRound(language: String) {
        currentWord = engine.nextWord(language)
    }

    fun submitAnswer(input: String, language: String): Boolean {
        roundsPlayed++
        val isCorrect = engine.checkAnswer(currentWord.word, input)
        if (isCorrect) {
            score++
            statsRepository.addPoints(15)
        }
        return isCorrect
    }
}
