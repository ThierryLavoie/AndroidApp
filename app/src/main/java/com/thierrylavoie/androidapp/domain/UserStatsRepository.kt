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

    fun spendPoints(points: Int): Boolean {
        if (totalPoints >= points) {
            totalPoints -= points
            return true
        }
        return false
    }

    fun isItemUnlocked(itemId: String): Boolean {
        val unlocked = prefs.getStringSet("unlocked_items", setOf("base_default")) ?: setOf("base_default")
        return unlocked.contains(itemId)
    }

    fun unlockItem(itemId: String) {
        val unlocked = prefs.getStringSet("unlocked_items", setOf("base_default"))?.toMutableSet() ?: mutableSetOf("base_default")
        unlocked.add(itemId)
        prefs.edit().putStringSet("unlocked_items", unlocked).apply()
    }

    fun getEquippedItem(category: String): String? {
        return prefs.getString("equipped_$category", null)
    }

    fun equipItem(category: String, itemId: String?) {
        prefs.edit().putString("equipped_$category", itemId).apply()
    }

    fun incrementGamesPlayed() {
        totalGamesPlayed++
    }

    fun getRank(): String {
        return when {
            totalPoints < 100 -> "rank_novice"
            totalPoints < 500 -> "rank_apprentice"
            totalPoints < 1500 -> "rank_scholar"
            totalPoints < 3000 -> "rank_master"
            else -> "rank_grandmaster"
        }
    }
}
