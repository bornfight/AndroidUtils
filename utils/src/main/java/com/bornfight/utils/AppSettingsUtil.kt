@file:kotlin.jvm.JvmName("AppSettingsUtil")
@file:kotlin.jvm.JvmMultifileClass

package com.bornfight.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

/**
 * Created by tomislav on 25/04/2017.
 */

/**
 * Launches the Android settings interface for this application.
 */
fun startAppSettings(context: Context) {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", context.packageName, null)
    intent.data = uri
    context.startActivity(intent)
}
