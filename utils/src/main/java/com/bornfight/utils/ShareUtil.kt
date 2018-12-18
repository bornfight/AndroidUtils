package com.bornfight.utils

import android.content.Context
import android.content.Intent


/**
 * Created by tomislav on 17/01/2017.
 */

object ShareUtil {

    fun shareText(context: Context, shareContent: String, chooserText: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareContent)
        sharingIntent.type = "text/plain"
        context.startActivity(Intent.createChooser(sharingIntent, chooserText))
    }
}
