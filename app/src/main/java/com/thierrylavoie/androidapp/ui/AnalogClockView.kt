package com.thierrylavoie.androidapp.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

class AnalogClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {
    private enum class ActiveHand {
        HOUR,
        MINUTE
    }

    private val facePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FAFAFA")
        style = Paint.Style.FILL
    }

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#333333")
        style = Paint.Style.STROKE
        strokeWidth = 6f
    }

    private val tickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#555555")
        strokeWidth = 4f
    }

    private val hourHandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1E88E5")
        strokeWidth = 12f
        strokeCap = Paint.Cap.ROUND
    }

    private val minuteHandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#F4511E")
        strokeWidth = 8f
        strokeCap = Paint.Cap.ROUND
    }

    private val centerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#222222")
        style = Paint.Style.FILL
    }

    private val numberPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#222222")
        textAlign = Paint.Align.CENTER
        textSize = 36f
    }

    var isInteractive: Boolean = true
        set(value) {
            field = value
            invalidate()
        }

    var selectedHour: Int = 12
        private set

    var selectedMinute: Int = 0
        private set

    var onTimeChanged: ((hour: Int, minute: Int) -> Unit)? = null
    private var activeHand: ActiveHand = ActiveHand.HOUR

    fun setDisplayedTime(hour: Int, minute: Int) {
        selectedHour = hour.coerceIn(1, 12)
        selectedMinute = minute.coerceIn(0, 59)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(width, height) * 0.42f

        canvas.drawCircle(centerX, centerY, radius, facePaint)
        canvas.drawCircle(centerX, centerY, radius, borderPaint)

        drawTicks(canvas, centerX, centerY, radius)
        drawNumbers(canvas, centerX, centerY, radius)
        drawHands(canvas, centerX, centerY, radius)
        canvas.drawCircle(centerX, centerY, 10f, centerPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isInteractive) return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                activeHand = nearestHand(event.x, event.y)
                updateTimeFromTouch(event.x, event.y)
                parent.requestDisallowInterceptTouchEvent(true)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                updateTimeFromTouch(event.x, event.y)
                return true
            }
            MotionEvent.ACTION_UP -> {
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    private fun updateTimeFromTouch(x: Float, y: Float) {
        val angle = pointToClockAngle(x, y)
        when (activeHand) {
            ActiveHand.HOUR -> {
                val snap = ((angle / 30f).roundToInt() % 12 + 12) % 12
                selectedHour = if (snap == 0) 12 else snap
            }
            ActiveHand.MINUTE -> {
                val snap = ((angle / 6f).roundToInt() % 60 + 60) % 60
                selectedMinute = snap
            }
        }
        onTimeChanged?.invoke(selectedHour, selectedMinute)
        invalidate()
    }

    private fun nearestHand(touchX: Float, touchY: Float): ActiveHand {
        val touchAngle = pointToClockAngle(touchX, touchY)

        val minuteAngle = (selectedMinute * 6f) % 360
        val hourAngle = ((selectedHour % 12) * 30f + (selectedMinute / 60f) * 30f) % 360

        val distToMinute = angularDistance(touchAngle, minuteAngle)
        val distToHour = angularDistance(touchAngle, hourAngle)

        return if (distToHour < distToMinute) ActiveHand.HOUR else ActiveHand.MINUTE
    }

    private fun angularDistance(a1: Float, a2: Float): Float {
        val diff = Math.abs(a1 - a2) % 360
        return if (diff > 180) 360 - diff else diff
    }

    private fun drawTicks(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        for (i in 0 until 60) {
            val angleRad = Math.toRadians((i * 6 - 90).toDouble())
            val outerX = cx + cos(angleRad).toFloat() * radius
            val outerY = cy + sin(angleRad).toFloat() * radius
            val tickLength = if (i % 5 == 0) 24f else 12f
            val innerX = cx + cos(angleRad).toFloat() * (radius - tickLength)
            val innerY = cy + sin(angleRad).toFloat() * (radius - tickLength)
            canvas.drawLine(innerX, innerY, outerX, outerY, tickPaint)
        }
    }

    private fun drawNumbers(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        for (n in 1..12) {
            val angleRad = Math.toRadians((n * 30 - 90).toDouble())
            val x = cx + cos(angleRad).toFloat() * (radius - 50f)
            val y = cy + sin(angleRad).toFloat() * (radius - 50f) + 12f
            canvas.drawText(n.toString(), x, y, numberPaint)
        }
    }

    private fun drawHands(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        val minuteAngle = selectedMinute * 6f
        val minuteRad = Math.toRadians((minuteAngle - 90).toDouble())
        val minuteX = cx + cos(minuteRad).toFloat() * (radius - 35f)
        val minuteY = cy + sin(minuteRad).toFloat() * (radius - 35f)
        canvas.drawLine(cx, cy, minuteX, minuteY, minuteHandPaint)

        val hourAngle = (selectedHour % 12) * 30f + (selectedMinute / 60f) * 30f
        val hourRad = Math.toRadians((hourAngle - 90).toDouble())
        val hourX = cx + cos(hourRad).toFloat() * (radius - 85f)
        val hourY = cy + sin(hourRad).toFloat() * (radius - 85f)
        canvas.drawLine(cx, cy, hourX, hourY, hourHandPaint)
    }

    private fun pointToClockAngle(x: Float, y: Float): Float {
        val cx = width / 2f
        val cy = height / 2f
        val radians = atan2(y - cy, x - cx).toDouble()
        return ((radians * 180 / PI) + 90 + 360).toFloat() % 360f
    }
}
