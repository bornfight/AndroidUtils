@file:kotlin.jvm.JvmName("HtmlUtil")
@file:kotlin.jvm.JvmMultifileClass

package com.bornfight.utils

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.SpannedString

/**
 * Created by tomislav on 17/02/2017.
 */

/**
 * Returns [Html.fromHtml], depending on the SDK version. (the deprecated version if < [Build.VERSION_CODES.N] )
 */
fun String?.span(): Spanned {
    if (this == null) return SpannedString("")

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(this)
    }
}
