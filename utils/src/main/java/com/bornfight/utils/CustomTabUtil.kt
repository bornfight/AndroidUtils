@file:kotlin.jvm.JvmName("CustomTabUtil")
@file:kotlin.jvm.JvmMultifileClass

package com.bornfight.utils

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.bornfight.android.utils.R

/**
 * Created by tomislav on 17/01/2017.
 */

/**
 * Launches the url in a custom tab screen if supported, otherwise falls back to the default browser.
 *
 * @param context
 * @param url the url to be shared
 * @param toolbarColorResId default toolbar color resource id
 * @param addDefaultShareMenuItem [CustomTabsIntent.Builder.addDefaultShareMenuItem]
 *
 * @see CustomTabsIntent
 */
fun launchUrl(
    context: Context,
    url: String,
    toolbarColorResId: Int = getResId(context, R.attr.colorPrimary),
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
