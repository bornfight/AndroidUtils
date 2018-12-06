package com.bornfight.android.utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes


object AttrResUtil{

    @JvmStatic
    fun getResId(context: Context, @AttrRes attrId: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(attrId, typedValue, true)
        return typedValue.data
    }
}