package com.thierrylavoie.ludo.domain

import kotlin.random.Random

import java.io.Serializable

enum class GameMode {
    SET_HANDS,
    READ_CLOCK
}

data class ClockTime(
    val hour12: Int,
    val minute: Int
) : Serializable {
    fun displayText(): String {
        val isFrench = androidx.appcompat.app.AppCompatDelegate.getApplicationLocales()[0]?.language?.startsWith("fr") == true
        
        val normalizedHour = when (val h = hour12 % 12) {
            0 -> 12
            else -> h
        }

        return if (isFrench) {
            val period = if (hour12 < 12) "du matin" else if (hour12 < 18) "de l'après-midi" else "du soir"
            "%d:%02d %s".format(normalizedHour, minute, period)
        } else {
            val period = if (hour12 < 12) "AM" else "PM"
            "%d:%02d %s".format(normalizedHour, minute, period)
        }
    }
}

class ClockGameEngine(
    private val random: Random = Random.Default
) {
    fun nextRound(): ClockTime {
        val isAm = random.nextBoolean()
        val hour = random.nextInt(1, 13)
        val minute = random.nextInt(0, 12) * 5
        val hour24 = when {
            hour == 12 && isAm -> 0
            hour == 12 && !isAm -> 12
            isAm -> hour
            else -> hour + 12
        }
        return ClockTime(hour12 = hour24, minute = minute)
    }

    fun checkSetHandsAnswer(target: ClockTime, selectedHourHand: Int, selectedMinute: Int): Boolean {
        val targetHour = toDisplayHour(target.hour12)
        return targetHour == selectedHourHand && target.minute == selectedMinute
    }

    fun checkReadClockAnswer(target: ClockTime, enteredHour: Int, enteredMinute: Int): Boolean {
        if (enteredHour !in 1..12 || enteredMinute !in 0..59) {
            return false
        }
        return toDisplayHour(target.hour12) == enteredHour && target.minute == enteredMinute
    }

    fun toDisplayHour(hour24: Int): Int {
        return when (hour24 % 12) {
            0 -> 12
            else -> hour24 % 12
        }
    }
}
