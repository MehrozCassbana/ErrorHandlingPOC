package com.bykea.task.core.extensions

import androidx.constraintlayout.motion.widget.MotionLayout

fun MotionLayout.onTransitionComplete(onComplete : () -> Unit){
    this.setTransitionListener(object: MotionLayout.TransitionListener {
        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
            onComplete.invoke()
        }

        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}

    })
}