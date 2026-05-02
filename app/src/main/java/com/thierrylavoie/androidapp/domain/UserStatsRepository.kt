package com.thierrylavoie.androidapp.domain

import android.content.Context
import android.content.SharedPreferences

class UserStatsRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_stats", Context.MODE_PRIVATE)

    var totalPoints: Int
        get() = prefs.getInt("total_points", 0)
        set(value) = prefs.edit().putInt("total_points", value).apply()

    var totalGamesPlayed: Int
        get() = prefs.getInt("games_played", 0)
        set(value) = prefs.edit().putInt("games_played", value).apply()

    var currentStreak: Int
        get() = prefs.getInt("current_streak", 0)
        set(value) = prefs.edit().putInt("current_streak", value).apply()

    fun addPoints(points: Int) {
        totalPoints += points
    }

    fun incrementGamesPlayed() {
        totalGamesPlayed++
    }

    fun getRank(): String {
        return when {
            totalPoints < 100 -> "Novice"
            totalPoints < 500 -> "Apprentice"
            totalPoints < 1500 -> "Scholar"
            totalPoints < 3000 -> "Master"
            else -> "Grandmaster"
        }
    }
}
