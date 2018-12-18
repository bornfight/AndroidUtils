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
    @JvmStatic
    fun launchUrl(
        context: Context,
        url: String,
        toolbarColorResId: Int = AttrResUtil.getResId(context, R.attr.colorPrimary),
        addDefaultShareMenuItem: Boolean = false
    ) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(context, toolbarColorResId))
        builder.enableUrlBarHiding()
        if (addDefaultShareMenuItem) {
            builder.addDefaultShareMenuItem()
        }
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }

}
