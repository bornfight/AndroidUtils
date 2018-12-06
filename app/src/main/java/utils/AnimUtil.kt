package com.bornfight.android.utils

import android.view.View
import android.view.ViewAnimationUtils
import androidx.interpolator.view.animation.FastOutLinearInInterpolator

/**
 * Created by tomislav on 28/04/2017.
 */

object AnimUtil {

    fun circularRevealView(view: View, duration: Int) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val centerX = view.width / 2
            val centerY = view.height / 2

            val startRadius = 0f
            val endRadius = (view.height / 2).toFloat()
            val animator = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius)
            animator.duration = duration.toLong()
            animator.interpolator = FastOutLinearInInterpolator()
            animator.start()
        }
    }
}
