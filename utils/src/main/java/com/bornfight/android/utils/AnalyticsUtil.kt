package com.bornfight.android.utils

import android.os.Bundle

import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Created by tomislav on 12/05/2017.
 */

object AnalyticsUtil {

    fun logFirebaseEvent(firebaseAnalytics: FirebaseAnalytics, eventName: String, contentType: String, content: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType)
        bundle.putString(FirebaseAnalytics.Param.CONTENT, content)
        firebaseAnalytics.logEvent(eventName, bundle)
    }

    fun logAnswersEvent(answers: Answers, eventName: String, category: String, content: String) {
        val ce = CustomEvent(eventName)
        ce.putCustomAttribute("category", category)
        ce.putCustomAttribute("content", content)
        answers.logCustom(ce)
    }
}
