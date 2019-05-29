@file:kotlin.jvm.JvmName("AppSettingsUtil")
@file:kotlin.jvm.JvmMultifileClass

package com.bornfight.utils

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.bornfight.android.utils.R

/**
 * Created by ianic on 01/03/2019.
 */

/**
 * Launches the Android settings interface for this application.
 */
fun Context.startAppSettings() {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}

/**
 * Launches the url in a custom tab screen if supported, otherwise falls back to the default browser.
 *
 * @param url the url to be shared
 * @param toolbarColorResId default toolbar color resource id
 * @param addDefaultShareMenuItem [CustomTabsIntent.Builder.addDefaultShareMenuItem]
 *
 * @see CustomTabsIntent
 */
fun Context.launchUrl(
    url: String,
    toolbarColorResId: Int = getResId(R.attr.colorPrimary),
    addDefaultShareMenuItem: Boolean = false
) {
    val builder = CustomTabsIntent.Builder()
    builder.setToolbarColor(ContextCompat.getColor(this, toolbarColorResId))
    builder.enableUrlBarHiding()
    if (addDefaultShareMenuItem) {
        builder.addDefaultShareMenuItem()
    }
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(this, Uri.parse(url))
}


/**
 * Starts the download of a file from the given url to local storage ([Environment.DIRECTORY_DOWNLOADS]).
 * @param title [DownloadManager.Request.setTitle]]
 * @param title [DownloadManager.Request.setDescription]]
 * @param title [DownloadManager.Request.setDestinationInExternalPublicDir]]
 * @param url URI passed as a string.
 */
fun Context.downloadFile(title: String, description: String, fileNameExt: String, url: String) {
    val request = DownloadManager.Request(Uri.parse(url))
    request.setDescription(description)
    request.setTitle(title)
    request.allowScanningByMediaScanner()
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileNameExt)

    // get download service and enqueue file
    val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
    manager?.enqueue(request)
}

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

/**
 * Returns the resource id integer for the given attribute resource, from the current theme fetched via context.
 */
fun Context.getResId(@AttrRes attrId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrId, typedValue, true)
    return typedValue.data
}