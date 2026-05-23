package com.thierrylavoie.ludo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.thierrylavoie.ludo.domain.*
import com.thierrylavoie.ludo.ui.AnalogClockView
import java.util.*

class ChallengeActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private enum class QuestionType { MATH, CLOCK, READING, SPELLING }

    private lateinit var statsRepository: UserStatsRepository
    private val mathEngine = MentalMathEngine()
    private val clockEngine = ClockGameEngine()
    private val readingEngine = ReadingGameEngine()
    private val readingEngineEn = ReadingGameEngineEnglish()
    private val spellingEngine = SpellingGameEngine()

    private var currentQuestionIndex = 0
    private var correctAnswersCount = 0
    private val totalQuestions = 10
    
    private lateinit var currentQuestionType: QuestionType
    private var currentTask: Any? = null

    private lateinit var challengeProgress: TextView
    private lateinit var feedbackView: TextView
    private lateinit var submitButton: Button
    
    private lateinit var clockUi: LinearLayout
    private lateinit var inputUi: LinearLayout
    private lateinit var choiceUi: LinearLayout
    
    private lateinit var analogClockView: AnalogClockView
    private lateinit var questionText: TextView
    private lateinit var genericInput: EditText
    private lateinit var readingText: TextView
    private lateinit var readingQuestion: TextView
    private lateinit var optionsGroup: RadioGroup
    private lateinit var btnListen: ImageButton

    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge)

        statsRepository = UserStatsRepository(this)
        tts = TextToSpeech(this, this)

        challengeProgress = findViewById(R.id.challengeProgress)
        feedbackView = findViewById(R.id.feedbackView)
        submitButton = findViewById(R.id.submitButton)
        
        clockUi = findViewById(R.id.clockUi)
        inputUi = findViewById(R.id.inputUi)
        choiceUi = findViewById(R.id.choiceUi)
        
        analogClockView = findViewById(R.id.analogClockView)
        questionText = findViewById(R.id.questionText)
        genericInput = findViewById(R.id.genericInput)
        readingText = findViewById(R.id.readingText)
        readingQuestion = findViewById(R.id.readingQuestion)
        optionsGroup = findViewById(R.id.optionsGroup)
        btnListen = findViewById(R.id.btnListen)

        findViewById<Button>(R.id.btnBackToMenu).setOnClickListener { finish() }

        submitButton.setOnClickListener { checkAnswer() }
        btnListen.setOnClickListener { speakWord() }

        nextQuestion()
    }

    private fun nextQuestion() {
        if (currentQuestionIndex >= totalQuestions) {
            finishChallenge()
            return
        }

        currentQuestionIndex++
        updateProgress()
        
        // Pick random question type
        currentQuestionType = QuestionType.entries.random()
        setupQuestionUi(currentQuestionType)
    }

    private fun setupQuestionUi(type: QuestionType) {
        clockUi.visibility = View.GONE
        inputUi.visibility = View.GONE
        choiceUi.visibility = View.GONE
        btnListen.visibility = View.GONE
        genericInput.text.clear()
        genericInput.hint = getString(R.string.math_answer_hint)
        
        when (type) {
            QuestionType.MATH -> {
                inputUi.visibility = View.VISIBLE
                val level = MathLevel.entries.random()
                val op = mathEngine.nextRound(level)
                currentTask = op
                questionText.text = if (op.isMissingTerm) {
                    getString(R.string.math_missing_term_prompt, op.left, op.operator, op.right, op.result)
                } else {
                    getString(R.string.math_prompt, op.left, op.operator, op.right)
                }
                genericInput.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_SIGNED
            }
            QuestionType.CLOCK -> {
                clockUi.visibility = View.VISIBLE
                val time = clockEngine.nextRound()
                currentTask = time
                analogClockView.setDisplayedTime(clockEngine.toDisplayHour(time.hour12), time.minute)
                analogClockView.isInteractive = false
                genericInput.visibility = View.VISIBLE
                inputUi.visibility = View.VISIBLE
                questionText.text = getString(R.string.mode_read_clock_prompt)
                genericInput.hint = getString(R.string.hour_hint) + " " + getString(R.string.minute_hint) + " (HHMM)"
                genericInput.inputType = android.text.InputType.TYPE_CLASS_NUMBER
            }
            QuestionType.READING -> {
                choiceUi.visibility = View.VISIBLE
                val isEn = kotlin.random.Random.nextBoolean()
                val task = if (isEn) readingEngineEn.getAllTasks().random() else readingEngine.getAllTasks().random()
                currentTask = task
                readingText.text = task.text
                readingQuestion.text = task.question
                optionsGroup.removeAllViews()
                task.options.forEachIndexed { index, option ->
                    val rb = RadioButton(this)
                    rb.text = option
                    rb.id = index
                    optionsGroup.addView(rb)
                }
            }
            QuestionType.SPELLING -> {
                inputUi.visibility = View.VISIBLE
                btnListen.visibility = View.VISIBLE
                val isFr = kotlin.random.Random.nextBoolean()
                val word = spellingEngine.nextWord(if (isFr) "fr" else "en")
                currentTask = word
                questionText.text = getString(R.string.spelling_instruction)
                genericInput.inputType = android.text.InputType.TYPE_CLASS_TEXT
                genericInput.hint = getString(R.string.spelling_hint)
                Handler(Looper.getMainLooper()).postDelayed({ speakWord() }, 500)
            }
        }
    }

    private fun checkAnswer() {
        var isCorrect = false
        when (currentQuestionType) {
            QuestionType.MATH -> {
                val input = genericInput.text.toString().toIntOrNull() ?: -1
                isCorrect = mathEngine.checkAnswer(currentTask as MathOperation, input)
            }
            QuestionType.CLOCK -> {
                val input = genericInput.text.toString()
                if (input.length >= 3) {
                    val hour = input.substring(0, input.length - 2).toIntOrNull() ?: -1
                    val min = input.substring(input.length - 2).toIntOrNull() ?: -1
                    isCorrect = clockEngine.checkReadClockAnswer(currentTask as ClockTime, hour, min)
                }
            }
            QuestionType.READING -> {
                val selectedId = optionsGroup.checkedRadioButtonId
                if (selectedId != -1) {
                    isCorrect = readingEngine.checkAnswer(currentTask as ReadingTask, selectedId)
                }
            }
            QuestionType.SPELLING -> {
                val input = genericInput.text.toString()
                isCorrect = spellingEngine.checkAnswer((currentTask as SpellingWord).word, input)
            }
        }

        if (isCorrect) {
            correctAnswersCount++
            statsRepository.addPoints(10) // Points per question
        }
        
        submitButton.isEnabled = false
        feedbackView.text = if (isCorrect) {
            feedbackView.setTextColor(getColor(R.color.fresh_green))
            "🎉 " + getString(R.string.correct_feedback)
        } else {
            feedbackView.setTextColor(getColor(R.color.vibrant_pink))
            "❌ " + getString(R.string.wrong_feedback, getAnswerString())
        }

        Handler(Looper.getMainLooper()).postDelayed({
            feedbackView.text = ""
            submitButton.isEnabled = true
            nextQuestion()
        }, 1500)
    }

    private fun getAnswerString(): String {
        return when (val task = currentTask) {
            is MathOperation -> task.expectedAnswer.toString()
            is ClockTime -> clockEngine.toDisplayHour(task.hour12).toString() + ":" + "%02d".format(task.minute)
            is ReadingTask -> task.options[task.correctOptionIndex]
            is SpellingWord -> task.word
            else -> ""
        }
    }

    private fun updateProgress() {
        challengeProgress.text = getString(R.string.challenge_progress, currentQuestionIndex)
    }

    private fun finishChallenge() {
        val success = correctAnswersCount >= 7
        val message = if (success) {
            statsRepository.addPoints(50)
            getString(R.string.challenge_success)
        } else {
            getString(R.string.challenge_failure, correctAnswersCount)
        }

        AlertDialog.Builder(this)
            .setTitle(R.string.challenge_complete)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok) { _, _ -> finish() }
            .show()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) isTtsReady = true
    }

    private fun speakWord() {
        if (isTtsReady && currentTask is SpellingWord) {
            val task = currentTask as SpellingWord
            tts?.language = if (task.language == "fr") Locale.FRENCH else Locale.ENGLISH
            tts?.speak(task.word, TextToSpeech.QUEUE_FLUSH, null, "challenge_word")
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}
