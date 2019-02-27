@file:kotlin.jvm.JvmName("MultiPartUtil")
@file:kotlin.jvm.JvmMultifileClass

package com.bornfight.utils

import android.net.Uri
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by tomislav on 23/02/2017.
 */

/**
 * Prepares the custom data for [MultipartBody.Part.createFormData]
 *
 * @param contentUri the [URI] content to be added to the form
 * @param mediaType string variant of the [MediaType] for [RequestBody]] (e.g. image/\*)
 * @param formName [MultipartBody.Part.createFormData] name ]
 * @return [MultipartBody.Part.createFormData] result
 */
fun MultipartBody.Part.createFormData(contentUri: Uri, mediaType: String, formName: String): MultipartBody.Part {
    val imageFile = File(contentUri.path)
    val fileReqBody = RequestBody.create(MediaType.parse(mediaType), imageFile)
    return MultipartBody.Part.createFormData(formName, imageFile.name, fileReqBody)
}