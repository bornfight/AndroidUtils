package com.bornfight.utils

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout
import java.util.*

/**
 * Created by tomislav on 27/05/16.
 */
object FormUtil {

    @JvmStatic
    fun isPasswordValid(password: String, minLength: Int): Boolean {
        return password.length >= minLength
    }

    @JvmStatic
    fun checkPasswordsMatch(password: String, retype: String): Boolean {
        return password == retype
    }

    @JvmStatic
    fun isNameValid(name: String): Boolean {
        return !TextUtils.isEmpty(name.trim())
    }

    @JvmStatic
    fun isBirthdayValid(date: String): Boolean {
        return !TextUtils.isEmpty(date.trim())
    }

    @JvmStatic
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


    @JvmStatic
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

    @JvmStatic
    fun isEmailValid(email: String): Boolean {
        return !TextUtils.isEmpty(email.trim()) && Patterns.EMAIL_ADDRESS.matcher(email.trim { it <= ' ' }).matches()
    }

    @JvmStatic
    fun isPhoneValid(phone: String): Boolean {
        return !TextUtils.isEmpty(phone.trim()) && Patterns.PHONE.matcher(phone).matches()
    }

    @JvmStatic
    fun addAsteriskToHint(vararg textInputLayouts: TextInputLayout) {
        for (textInputLayout in textInputLayouts) {
            textInputLayout.hint = textInputLayout.hint.toString() + "*"
        }
    }

    @JvmStatic
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


}
