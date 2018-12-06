package com.bornfight.android.utils

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.view.View

/**
 * Created by tomislav on 05/05/2017.
 */

object StyleUtil {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun enabledContentBehindStatusBar(activity: Activity, statusBarColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val w = activity.window // in Activity's onCreate() for instance
            w.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            w.statusBarColor = statusBarColor
        }

    }
}
