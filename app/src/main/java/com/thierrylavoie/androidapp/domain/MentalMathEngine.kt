package com.thierrylavoie.ludo.domain

import kotlin.random.Random

import java.io.Serializable

data class MathOperation(
    val left: String,
    val right: String,
    val operator: String,
    val result: Int,
    val expectedAnswer: Int,
    val isMissingTerm: Boolean = false
) : Serializable

enum class MathLevel {
    GRADE_1, // Add/Sub within 20
    GRADE_2, // Add/Sub within 100, Mult 2, 5, 10
    GRADE_3, // Mult tables up to 10
    GRADE_4, // Mult tables up to 12
    GRADE_5  // Larger numbers
}

class MentalMathEngine(
    private val random: Random = Random.Default
) {
    fun nextRound(level: MathLevel): MathOperation {
        val baseOp = when (level) {
            MathLevel.GRADE_1 -> generateGrade1()
            MathLevel.GRADE_2 -> generateGrade2()
            MathLevel.GRADE_3 -> generateGrade3()
            MathLevel.GRADE_4 -> generateGrade4()
            MathLevel.GRADE_5 -> generateGrade5()
        }

        // 30% chance to turn it into a missing term variant
        return if (random.nextFloat() < 0.3f) {
            toMissingTerm(baseOp)
        } else {
            baseOp
        }
    }

    private fun toMissingTerm(op: MathOperation): MathOperation {
        val missingLeft = random.nextBoolean()
        return if (missingLeft) {
            MathOperation(
                left = "?",
                right = op.right,
                operator = op.operator,
                result = op.result,
                expectedAnswer = op.left.toInt(),
                isMissingTerm = true
            )
        } else {
            MathOperation(
                left = op.left,
                right = "?",
                operator = op.operator,
                result = op.result,
                expectedAnswer = op.right.toInt(),
                isMissingTerm = true
            )
        }
    }

    private fun generateGrade1(): MathOperation {
        val isAdd = random.nextBoolean()
        val operator = if (isAdd) "+" else "-"
        val left = random.nextInt(1, 20)
        val right = if (isAdd) random.nextInt(1, 21 - left) else random.nextInt(1, left + 1)
        val result = if (isAdd) left + right else left - right
        return MathOperation(left.toString(), right.toString(), operator, result, result)
    }

    private fun generateGrade2(): MathOperation {
        val type = random.nextInt(3)
        return when (type) {
            0 -> { // Add within 100
                val left = random.nextInt(1, 100)
                val right = random.nextInt(1, 101 - left)
                val res = left + right
                MathOperation(left.toString(), right.toString(), "+", res, res)
            }
            1 -> { // Sub within 100
                val left = random.nextInt(1, 100)
                val right = random.nextInt(1, left + 1)
                val res = left - right
                MathOperation(left.toString(), right.toString(), "-", res, res)
            }
            else -> { // Easy Mult
                val table = listOf(2, 5, 10).random(random)
                val other = random.nextInt(1, 11)
                val res = table * other
                MathOperation(table.toString(), other.toString(), "*", res, res)
            }
        }
    }

    private fun generateGrade3(): MathOperation {
        val operator = listOf("+", "-", "*").random(random)
        return when (operator) {
            "*" -> {
                val left = random.nextInt(2, 11)
                val right = random.nextInt(2, 11)
                val res = left * right
                MathOperation(left.toString(), right.toString(), "*", res, res)
            }
            else -> {
                val left = random.nextInt(10, 200)
                val right = random.nextInt(10, 200)
                if (operator == "+") {
                    val res = left + right
                    MathOperation(left.toString(), right.toString(), "+", res, res)
                } else {
                    val a = maxOf(left, right)
                    val b = minOf(left, right)
                    val res = a - b
                    MathOperation(a.toString(), b.toString(), "-", res, res)
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
                val res = left * right
                MathOperation(left.toString(), right.toString(), "*", res, res)
            }
            "+" -> {
                val left = random.nextInt(100, 1000)
                val right = random.nextInt(100, 1000)
                val res = left + right
                MathOperation(left.toString(), right.toString(), "+", res, res)
            }
            else -> {
                val left = random.nextInt(100, 1000)
                val right = random.nextInt(1, left)
                val res = left - right
                MathOperation(left.toString(), right.toString(), "-", res, res)
            }
        }
    }

    private fun generateGrade5(): MathOperation {
        val operator = listOf("+", "-", "*").random(random)
        return when (operator) {
            "*" -> {
                val left = random.nextInt(10, 50)
                val right = random.nextInt(2, 15)
                val res = left * right
                MathOperation(left.toString(), right.toString(), "*", res, res)
            }
            "+" -> {
                val left = random.nextInt(500, 5000)
                val right = random.nextInt(500, 5000)
                val res = left + right
                MathOperation(left.toString(), right.toString(), "+", res, res)
            }
            else -> {
                val left = random.nextInt(1000, 10000)
                val right = random.nextInt(100, left)
                val res = left - right
                MathOperation(left.toString(), right.toString(), "-", res, res)
            }
        }
    }

    fun checkAnswer(operation: MathOperation, answer: Int): Boolean {
        return operation.expectedAnswer == answer
    }
}
