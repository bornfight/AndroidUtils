@file:kotlin.jvm.JvmName("DownloadUtil")
@file:kotlin.jvm.JvmMultifileClass

package com.bornfight.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

/**
 * Created by tomislav on 04/05/2017.
 */

/**
 * Starts the download of a file from the given url to local storage ([Environment.DIRECTORY_DOWNLOADS]).
 * @param title [DownloadManager.Request.setTitle]]
 * @param title [DownloadManager.Request.setDescription]]
 * @param title [DownloadManager.Request.setDestinationInExternalPublicDir]]
 * @param url URI passed as a string.
 */
fun downloadFile(context: Context, title: String, description: String, fileNameExt: String, url: String) {
    val request = DownloadManager.Request(Uri.parse(url))
    request.setDescription(description)
    request.setTitle(title)
    request.allowScanningByMediaScanner()
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileNameExt)

    // get download service and enqueue file
    val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
    manager?.enqueue(request)
}
