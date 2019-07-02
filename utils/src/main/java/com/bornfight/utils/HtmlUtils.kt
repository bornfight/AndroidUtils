@file:kotlin.jvm.JvmName("HtmlUtil")
@file:kotlin.jvm.JvmMultifileClass

package com.bornfight.utils

import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.SpannedString
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by tomislav on 17/02/2017.
 */

/**
 * Returns [Html.fromHtml], depending on the SDK version. (the deprecated version if < [Build.VERSION_CODES.N] )
 */
fun String?.spanHtml(): Spanned {
    if (this == null) return SpannedString("")

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(this)
    }
}

/**
 * Prepares the custom data for [MultipartBody.Part.createFormData]
 *
 * @param contentUri the [URI] content to be added to the form
 * @param mediaType string variant of the [MediaType] for [RequestBody]] (e.g. image/\*)
 * @param formName [MultipartBody.Part.createFormData] name ]
 * @return [MultipartBody.Part.createFormData] result
 */
fun createFormData(contentUri: Uri, mediaType: String, formName: String): MultipartBody.Part {
    val imageFile = File(contentUri.path)
    val fileReqBody = RequestBody.create(MediaType.parse(mediaType), imageFile)
    return MultipartBody.Part.createFormData(formName, imageFile.name, fileReqBody)
}