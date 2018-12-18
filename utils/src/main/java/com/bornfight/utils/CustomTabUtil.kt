package com.bornfight.utils

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.bornfight.android.utils.R


/**
 * Created by tomislav on 17/01/2017.
 */

object CustomTabUtil {
    /**
     * Launches the url in custom tab screen if supported or launches in default browser
     */
    fun launchUrl(context: Context, url: String) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(context, AttrResUtil.getResId(context, R.attr.colorPrimary)))
        builder.enableUrlBarHiding()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }

}
