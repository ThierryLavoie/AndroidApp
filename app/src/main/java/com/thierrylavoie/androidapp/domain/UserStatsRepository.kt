package com.thierrylavoie.androidapp.domain

import android.content.Context
import android.content.SharedPreferences

class UserStatsRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_stats", Context.MODE_PRIVATE)

    var totalPoints: Int
        get() = prefs.getInt("total_points", 0)
        set(value) = prefs.edit().putInt("total_points", value).apply()

    var lifetimePoints: Int
        get() {
            val stored = prefs.getInt("lifetime_points", -1)
            if (stored == -1) {
                // One-time initialization: lifetime points start at current point balance
                val current = totalPoints
                prefs.edit().putInt("lifetime_points", current).apply()
                return current
            }
            return stored
        }
        private set(value) = prefs.edit().putInt("lifetime_points", value).apply()

    var totalGamesPlayed: Int
        get() = prefs.getInt("games_played", 0)
        set(value) = prefs.edit().putInt("games_played", value).apply()

    var currentStreak: Int
        get() = prefs.getInt("current_streak", 0)
        set(value) = prefs.edit().putInt("current_streak", value).apply()

    fun addPoints(points: Int) {
        totalPoints += points
        lifetimePoints += points
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

    fun getAccessoryOffset(category: String): Pair<Float, Float> {
        val x = prefs.getFloat("offset_${category}_x", 0f)
        val y = prefs.getFloat("offset_${category}_y", 0f)
        return Pair(x, y)
    }

    fun setAccessoryOffset(category: String, x: Float, y: Float) {
        prefs.edit()
            .putFloat("offset_${category}_x", x)
            .putFloat("offset_${category}_y", y)
            .apply()
    }

    fun incrementGamesPlayed() {
        totalGamesPlayed++
    }

    fun getRank(): String {
        val pts = lifetimePoints
        return when {
            pts < 100 -> "rank_novice"
            pts < 500 -> "rank_apprentice"
            pts < 1500 -> "rank_scholar"
            pts < 3000 -> "rank_master"
            else -> "rank_grandmaster"
        }
    }
}
