package com.bornfight.android.utils

import android.net.Uri
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by tomislav on 23/02/2017.
 */

object MultipartUtil {

    fun buildPart(contentUri: Uri, mediaType: String, formName: String): MultipartBody.Part {
        val imageFile = File(contentUri.path)
        val fileReqBody = RequestBody.create(MediaType.parse(mediaType), imageFile)
        return MultipartBody.Part.createFormData(formName, imageFile.name, fileReqBody)
    }

}
