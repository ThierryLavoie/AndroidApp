package com.thierrylavoie.androidapp.domain

import kotlin.random.Random

enum class GameMode {
    SET_HANDS,
    READ_CLOCK
}

data class ClockTime(
    val hour12: Int,
    val minute: Int
) {
    fun displayText(): String {
        val period = if (hour12 < 12) "AM" else "PM"
        val displayHour = when (val normalizedHour = hour12 % 12) {
            0 -> 12
            else -> normalizedHour
        }
        return "%d:%02d %s".format(displayHour, minute, period)
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

    fun checkReadClockAnswer(target: ClockTime, enteredHour: Int, enteredMinute: Int, isPm: Boolean): Boolean {
        if (enteredHour !in 1..12 || enteredMinute !in 0..59) {
            return false
        }

        val enteredHour24 = when {
            enteredHour == 12 && !isPm -> 0
            enteredHour == 12 && isPm -> 12
            isPm -> enteredHour + 12
            else -> enteredHour
        }

        return target.hour12 == enteredHour24 && target.minute == enteredMinute
    }

    fun toDisplayHour(hour24: Int): Int {
        return when (hour24 % 12) {
            0 -> 12
            else -> hour24 % 12
        }
    }
}
