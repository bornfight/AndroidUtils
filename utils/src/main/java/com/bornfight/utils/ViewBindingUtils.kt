package com.bornfight.utils

import androidx.fragment.app.Fragment

/**
 * Cleans the field reference once the view is destroyed.
 * There is also an option of setting initial value.
 */
fun <T : Any> Fragment.autoCleaned(initializer: (() -> T)? = null): AutoCleanedValue<T> {
    return AutoCleanedValue(this, initializer)
}