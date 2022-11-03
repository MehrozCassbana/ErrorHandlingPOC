package com.bykea.task.utils.clickutils

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import kotlin.math.abs

class OnSwipeTouchListener(context: Context?) :
    OnTouchListener {

    private val gestureDetector: GestureDetector
    fun onSwipeLeft() {}
    fun onSwipeRight() {}
    fun onSwipeBottom() {}
    fun onSwipeTop() {}
    fun onLongSwipeBottom() {}
    fun onLongSwipeTop() {}
    fun onClick() {}

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            onClick()
            return false //super.onSingleTapConfirmed(e);
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > Companion.SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > Companion.SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                        result = true
                    }
                } else if (abs(diffY) > SWIPE_DISTANCE_THRESHOLD && abs(
                        velocityY
                    ) > SWIPE_VELOCITY_THRESHOLD
                ) {
                    if (diffY > 0) {
                        if (abs(diffY) > LONG_SWIPE_DISTANCE_THRESHOLD && abs(
                                velocityY
                            ) > LONG_SWIPE_VELOCITY_THRESHOLD
                        ) {
                            onLongSwipeBottom()
                        } else {
                            onSwipeBottom()
                        }
                    } else {
                        if (abs(diffY) > LONG_SWIPE_DISTANCE_THRESHOLD && abs(
                                velocityY
                            ) > LONG_SWIPE_VELOCITY_THRESHOLD
                        ) {
                            onLongSwipeTop()
                        } else {
                            onSwipeTop()
                        }
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
        }
    }

    companion object {
        private const val SWIPE_DISTANCE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
        private const val LONG_SWIPE_DISTANCE_THRESHOLD = SWIPE_DISTANCE_THRESHOLD + 500
        private const val LONG_SWIPE_VELOCITY_THRESHOLD = SWIPE_VELOCITY_THRESHOLD + 500
    }
}