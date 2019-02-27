@file:kotlin.jvm.JvmName("AttrResUtil")
@file:kotlin.jvm.JvmMultifileClass

package com.bornfight.utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes

/**
 * Returns the resource id integer for the given attribute resource, from the current theme fetched via context.
 */
fun getResId(context: Context, @AttrRes attrId: Int): Int {
    val typedValue = TypedValue()
    val theme = context.theme
    theme.resolveAttribute(attrId, typedValue, true)
    return typedValue.data
}