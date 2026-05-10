package com.thierrylavoie.ludo.domain

import org.junit.Assert.assertTrue
import org.junit.Test

class ClockGameEngineTest {

    private val engine = ClockGameEngine()

    @Test
    fun setHandsAnswer_matchesExactHands() {
        val target = ClockTime(hour12 = 8, minute = 45)
        val result = engine.checkSetHandsAnswer(
            target = target,
            selectedHourHand = 8,
            selectedMinute = 45
        )
        assertTrue(result)
    }

    @Test
    fun readClockAnswer_matchesDisplayedHourAndMinute() {
        val target = ClockTime(hour12 = 20, minute = 45)
        val result = engine.checkReadClockAnswer(
            target = target,
            enteredHour = 8,
            enteredMinute = 45
        )
        assertTrue(result)
    }
}
