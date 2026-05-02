package com.thierrylavoie.androidapp.domain

import kotlin.random.Random

data class MathOperation(
    val left: Int,
    val right: Int,
    val operator: String,
    val result: Int
)

enum class MathLevel {
    GRADE_1, // Add/Sub within 20
    GRADE_2, // Add/Sub within 100, Mult 2, 5, 10
    GRADE_3, // Mult tables up to 10
    GRADE_4, // Mult tables up to 12, Sub with negative results possible? No, keep positive.
    GRADE_5  // Larger numbers
}

class MentalMathEngine(
    private val random: Random = Random.Default
) {
    fun nextRound(level: MathLevel): MathOperation {
        return when (level) {
            MathLevel.GRADE_1 -> generateGrade1()
            MathLevel.GRADE_2 -> generateGrade2()
            MathLevel.GRADE_3 -> generateGrade3()
            MathLevel.GRADE_4 -> generateGrade4()
            MathLevel.GRADE_5 -> generateGrade5()
        }
    }

    private fun generateGrade1(): MathOperation {
        val isAdd = random.nextBoolean()
        val operator = if (isAdd) "+" else "-"
        val left = random.nextInt(1, 20)
        val right = if (isAdd) random.nextInt(1, 21 - left) else random.nextInt(1, left + 1)
        val result = if (isAdd) left + right else left - right
        return MathOperation(left, right, operator, result)
    }

    private fun generateGrade2(): MathOperation {
        val type = random.nextInt(3)
        return when (type) {
            0 -> { // Add within 100
                val left = random.nextInt(1, 100)
                val right = random.nextInt(1, 101 - left)
                MathOperation(left, right, "+", left + right)
            }
            1 -> { // Sub within 100
                val left = random.nextInt(1, 100)
                val right = random.nextInt(1, left + 1)
                MathOperation(left, right, "-", left - right)
            }
            else -> { // Easy Mult
                val table = listOf(2, 5, 10).random(random)
                val other = random.nextInt(1, 11)
                MathOperation(table, other, "*", table * other)
            }
        }
    }

    private fun generateGrade3(): MathOperation {
        val operator = listOf("+", "-", "*").random(random)
        return when (operator) {
            "*" -> {
                val left = random.nextInt(2, 11)
                val right = random.nextInt(2, 11)
                MathOperation(left, right, "*", left * right)
            }
            else -> {
                val left = random.nextInt(10, 200)
                val right = random.nextInt(10, 200)
                if (operator == "+") MathOperation(left, right, "+", left + right)
                else {
                    val a = maxOf(left, right)
                    val b = minOf(left, right)
                    MathOperation(a, b, "-", a - b)
                }
            }
        }
    }

    private fun generateGrade4(): MathOperation {
        val operator = listOf("+", "-", "*").random(random)
        return when (operator) {
            "*" -> {
                val left = random.nextInt(2, 13)
                val right = random.nextInt(2, 13)
                MathOperation(left, right, "*", left * right)
            }
            "+" -> {
                val left = random.nextInt(100, 1000)
                val right = random.nextInt(100, 1000)
                MathOperation(left, right, "+", left + right)
            }
            else -> {
                val left = random.nextInt(100, 1000)
                val right = random.nextInt(1, left)
                MathOperation(left, right, "-", left - right)
            }
        }
    }

    private fun generateGrade5(): MathOperation {
        val operator = listOf("+", "-", "*").random(random)
        return when (operator) {
            "*" -> {
                val left = random.nextInt(10, 50)
                val right = random.nextInt(2, 15)
                MathOperation(left, right, "*", left * right)
            }
            "+" -> {
                val left = random.nextInt(500, 5000)
                val right = random.nextInt(500, 5000)
                MathOperation(left, right, "+", left + right)
            }
            else -> {
                val left = random.nextInt(1000, 10000)
                val right = random.nextInt(100, left)
                MathOperation(left, right, "-", left - right)
            }
        }
    }

    fun checkAnswer(operation: MathOperation, answer: Int): Boolean {
        return operation.result == answer
    }
}
