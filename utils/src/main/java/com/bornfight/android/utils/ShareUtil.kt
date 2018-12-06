package com.bornfight.android.utils

import android.content.Context
import android.content.Intent


/**
 * Created by tomislav on 17/01/2017.
 */

object ShareUtil {

    fun shareUrl(context: Context, shareText: String, url: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, url)
        sharingIntent.type = "text/plain"
        context.startActivity(Intent.createChooser(sharingIntent, shareText))
    }
}
