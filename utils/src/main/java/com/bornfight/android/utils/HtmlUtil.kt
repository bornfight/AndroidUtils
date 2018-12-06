package com.bornfight.android.utils

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.SpannedString

/**
 * Created by tomislav on 17/02/2017.
 */

object HtmlUtil {

    @JvmStatic
    fun span(string: String?): Spanned {
        if (string == null) return SpannedString("")

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(string, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(string)
        }
    }
}
