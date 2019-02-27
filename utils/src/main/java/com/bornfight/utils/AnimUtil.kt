@file:kotlin.jvm.JvmName("AnimUtil")
@file:kotlin.jvm.JvmMultifileClass

package com.bornfight.utils

import android.view.View
import android.view.ViewAnimationUtils
import androidx.interpolator.view.animation.FastOutLinearInInterpolator

/**
 * Created by tomislav on 28/04/2017.
 */

/**
 * Performs a circular reveal animation on this [View].
 *
 * @param duration the animation duration
 */
fun View.circularReveal(duration: Int) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        val centerX = width / 2
        val centerY = height / 2

        val startRadius = 0f
        val endRadius = (height / 2).toFloat()
        val animator = ViewAnimationUtils.createCircularReveal(this, centerX, centerY, startRadius, endRadius)
        animator.duration = duration.toLong()
        animator.interpolator = FastOutLinearInInterpolator()
        animator.start()
    }
}
