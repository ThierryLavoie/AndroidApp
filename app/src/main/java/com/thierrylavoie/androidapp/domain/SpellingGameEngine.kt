package com.thierrylavoie.ludo.domain

import kotlin.random.Random

import java.io.Serializable

data class SpellingWord(
    val word: String,
    val language: String // "en" or "fr"
) : Serializable

class SpellingGameEngine(private val random: Random = Random.Default) {
    private val frenchWords = listOf(
        "maison", "chat", "chien", "pomme", "école", "soleil", "fleur", "garçon", "fille", "arbre",
        "oiseau", "livre", "table", "chaise", "fenêtre", "porte", "crayon", "papier", "ciel", "mer"
    )

    private val englishWords = listOf(
        "house", "cat", "dog", "apple", "school", "sun", "flower", "boy", "girl", "tree",
        "bird", "book", "table", "chair", "window", "door", "pencil", "paper", "sky", "sea"
    )

    fun nextWord(language: String): SpellingWord {
        val list = if (language.startsWith("fr")) frenchWords else englishWords
        return SpellingWord(list.random(random), if (language.startsWith("fr")) "fr" else "en")
    }

    fun checkAnswer(target: String, input: String): Boolean {
        return target.trim().equals(input.trim(), ignoreCase = true)
    }
}
