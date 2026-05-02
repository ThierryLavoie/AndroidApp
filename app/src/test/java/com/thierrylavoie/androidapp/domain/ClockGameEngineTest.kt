package com.thierrylavoie.androidapp.domain

import org.junit.Assert.assertFalse
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
    fun readClockAnswer_validatesAmPm() {
        val target = ClockTime(hour12 = 20, minute = 45)
        val wrongAmResult = engine.checkReadClockAnswer(
            target = target,
            enteredHour = 8,
            enteredMinute = 45,
            isPm = false
        )
        assertFalse(wrongAmResult)

        val rightPmResult = engine.checkReadClockAnswer(
            target = target,
            enteredHour = 8,
            enteredMinute = 45,
            isPm = true
        )
        assertTrue(rightPmResult)
    }
}
