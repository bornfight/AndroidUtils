package com.bornfight.android.utils

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.text.util.Linkify
import android.view.View
import android.widget.TextView

/**
 * Created by tomislav on 20/03/2018.
 */

object TextViewUtil {

    /**
     * First spans the `"a href"` html tags, then uses [Linkify.addLinks] to span plain text links (web,
     * phone, email...)
     * Then replaces [URLSpan]s with custom ones which open the links with [CustomTabUtil.launchUrl] or your
     * custom [TextViewUtil.OnUrlClickListener] will be triggered
     *
     * @param textView           sets the spanned text to the `textView`
     * @param htmlContent        html content that should be spanned and added to `textView`
     * @param spanPlainTextLinks whether to span plain text links with [Linkify.ALL]
     * @param urlClickListener   custom URL click listener, overrides CustomTabUtil.launchUrl method
     */
    fun spanText(
        textView: TextView,
        htmlContent: String?,
        spanPlainTextLinks: Boolean,
        urlClickListener: OnUrlClickListener? = null
    ) {
        // create spans for <a href> tags, these open in default browser
        var hrefSpannable =
            setCustomUrlSpans(textView.context, SpannableString(HtmlUtil.span(htmlContent)), urlClickListener)

        if (spanPlainTextLinks) {
            // create spans for auto recognized plain text links, these open in default browser
            Linkify.addLinks(hrefSpannable, Linkify.ALL)
        }

        // final span of links, default URLSpans will now be replaced and opened in custom tabs
        hrefSpannable = setCustomUrlSpans(textView.context, hrefSpannable, urlClickListener)

        textView.text = hrefSpannable
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setCustomUrlSpans(
        context: Context,
        spannableString: SpannableString?,
        urlClickListener: OnUrlClickListener?
    ): SpannableString {
        if (spannableString == null) return SpannableString("")

        for (urlSpan in spannableString.getSpans(0, spannableString.length, URLSpan::class.java)) {
            val newSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    urlClickListener?.onUrlClicked(urlSpan.url) ?: CustomTabUtil.launchUrl(context, urlSpan.url)
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
}
