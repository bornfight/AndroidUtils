@file:kotlin.jvm.JvmName("InterfaceUtil")
@file:kotlin.jvm.JvmMultifileClass

package com.bornfight.utils

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.ImageView
import androidx.interpolator.view.animation.FastOutLinearInInterpolator

/**
 * Created by ianic on 01/03/2019.
 */

/**
 * Sets the saturation of this [ImageView] to 0.
 */
fun ImageView.setBlackWhite() {
    val matrix = ColorMatrix()
    matrix.setSaturation(0f)

    val filter = ColorMatrixColorFilter(matrix)
    colorFilter = filter
}

/**
 * Enables content visibility behind the status bar by setting [View.SYSTEM_UI_FLAG_LAYOUT_STABLE]
 * or [View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN] flags to [android.view.Window.getDecorView].systemUiVisibility
 * and the status bar color to the given color.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun Activity.enableContentBehindStatusBar(statusBarColor: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = statusBarColor // in Activity's onCreate() for instance
    }

}

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
