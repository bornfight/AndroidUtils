@file:kotlin.jvm.JvmName("FormUtil")
@file:kotlin.jvm.JvmMultifileClass

package com.bornfight.utils

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout
import java.util.*

/**
 * Created by tomislav on 27/05/16.
 *
 * Contains methods used for form validation. (password, name, email, phone, birthday)
 */

fun isPasswordValid(password: String, minLength: Int): Boolean {
    return password.length >= minLength
}

fun checkPasswordsMatch(password: String, retype: String): Boolean {
    return password == retype
}

fun isNameValid(name: String): Boolean {
    return !TextUtils.isEmpty(name.trim())
}

fun isEmailValid(email: String): Boolean {
    return !TextUtils.isEmpty(email.trim()) && Patterns.EMAIL_ADDRESS.matcher(email.trim { it <= ' ' }).matches()
}

fun isPhoneValid(phone: String): Boolean {
    return !TextUtils.isEmpty(phone.trim()) && Patterns.PHONE.matcher(phone).matches()
}

fun isBirthdayValid(date: String): Boolean {
    return !TextUtils.isEmpty(date.trim())
}

/**
 * Checks whether the date difference between the current moment and the given date is larger than 18 years.
 */
fun isBirthdayValid(year: Int, month: Int, day: Int): Boolean {
    val ageGate = Calendar.getInstance()
    ageGate.add(Calendar.YEAR, -18)
    ageGate.add(Calendar.MINUTE, 1)
    val age = Calendar.getInstance()
    age.set(Calendar.YEAR, year)
    age.set(Calendar.MONTH, month)
    age.set(Calendar.DAY_OF_MONTH, day)

    return age.before(ageGate)
}

/**
 * Checks whether the given [TextInputLayout]s are empty, sets the given error to the ones that are.
 */
fun checkInputLayoutsEmpty(emptyError: String, vararg inputLayouts: TextInputLayout): Boolean {
    var empty = false
    for (textInputLayout in inputLayouts) {
        if (textInputLayout.editText?.text.toString().trim { it <= ' ' }.isEmpty()) {
            textInputLayout.error = emptyError
            empty = true
        }
    }
    return empty
}

/**
 * Adds an asterisk symbol to the given [TextInputLayout] hints.
 */
fun addAsteriskToHint(vararg textInputLayouts: TextInputLayout) {
    for (textInputLayout in textInputLayouts) {
        textInputLayout.hint = textInputLayout.hint.toString() + "*"
    }
}

/**
 * Adds a [TextWatcher] instance to [android.widget.EditText.addTextChangedListener].
 * The purpose of this is to validate the text as new text is typed. It clears the errors as soon as
 * the text becomes valid, and sets the given error otherwise.
 */
fun setValidationWatcher(inputLayout: TextInputLayout, error: String, validate: (text: String) -> Boolean) {
    inputLayout.editText?.addTextChangedListener(object : TextWatcher {

        var validationSucceeded = false

        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (p0.isNullOrEmpty()) {
                validationSucceeded = false
                inputLayout.isErrorEnabled = false
            }

            if (validate(p0.toString())) {
                inputLayout.isErrorEnabled = false
                validationSucceeded = true
            } else if (validationSucceeded) {
                inputLayout.error = error
            }
        }
    })
}
