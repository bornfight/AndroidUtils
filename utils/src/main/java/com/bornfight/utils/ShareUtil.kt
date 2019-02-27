@file:kotlin.jvm.JvmName("ShareUtil")
@file:kotlin.jvm.JvmMultifileClass

package com.bornfight.utils

import android.content.Context
import android.content.Intent

/**
 * Created by tomislav on 17/01/2017.
 */

/** Launches a share intent with [Intent.ACTION_SEND] flag with the given content.
 *
 * @param shareContent the content to be shared (e.g. an URI)
 * @param chooserText title for [Intent.createChooser]]
 */
fun Context.shareText(shareContent: String, chooserText: String = "Share via…") {
    val sharingIntent = Intent(Intent.ACTION_SEND)
    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareContent)
    sharingIntent.type = "text/plain"
    startActivity(Intent.createChooser(sharingIntent, chooserText))
}

