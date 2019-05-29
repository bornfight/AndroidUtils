@file:kotlin.jvm.JvmName("TextViewUtil")
@file:kotlin.jvm.JvmMultifileClass

package com.bornfight.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.text.util.Linkify
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.interpolator.view.animation.FastOutLinearInInterpolator

/**
 * Created by ianic on 01/03/2019.
 */

/**
 * First spans the `"a href"` html tags, then uses [Linkify.addLinks] to span plain text links (web,
 * phone, email...)
 * Then replaces [URLSpan]s with custom ones which open the links with [launchUrl] or your
 * custom [TextViewUtil.OnUrlClickListener] will be triggered
 *
 * @param htmlContent        html content that should be spanned and added to `textView`
 * @param spanPlainTextLinks whether to span plain text links with [Linkify.ALL]
 * @param urlClickListener   custom URL click listener, overrides CustomTabUtil.launchUrl method
 */
fun TextView.spanText(
    htmlContent: String?,
    spanPlainTextLinks: Boolean,
    urlClickListener: OnUrlClickListener? = null
) {
    // create spans for <a href> tags, these open in default browser
    var hrefSpannable =
        setCustomUrlSpans(context, SpannableString(htmlContent.span()), urlClickListener)

    if (spanPlainTextLinks) {
        // create spans for auto recognized plain text links, these open in default browser
        Linkify.addLinks(hrefSpannable, Linkify.ALL)
    }

    // final span of links, default URLSpans will now be replaced and opened in custom tabs
    hrefSpannable = setCustomUrlSpans(context, hrefSpannable, urlClickListener)

    text = hrefSpannable
    movementMethod = LinkMovementMethod.getInstance()
}

/**
 * Iterates through all [URLSpan]s already in the given [SpannableString], and replaces them with a custom
 * [ClickableSpan] which uses [launchUrl] or [OnUrlClickListener]
 */
private fun setCustomUrlSpans(
    context: Context,
    spannableString: SpannableString?,
    urlClickListener: OnUrlClickListener?
): SpannableString {
    if (spannableString == null) return SpannableString("")

    for (urlSpan in spannableString.getSpans(0, spannableString.length, URLSpan::class.java)) {
        val newSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                urlClickListener?.onUrlClicked(urlSpan.url) ?: context.launchUrl(urlSpan.url)
            }
        }
        spannableString.setSpan(
            newSpan, spannableString.getSpanStart(urlSpan), spannableString.getSpanEnd(urlSpan),
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        spannableString.removeSpan(urlSpan)
    }
    return spannableString
}

interface OnUrlClickListener {
    fun onUrlClicked(url: String)
}

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
