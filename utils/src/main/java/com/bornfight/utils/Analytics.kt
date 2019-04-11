@file:kotlin.jvm.JvmName("AnalyticsUtil")
@file:kotlin.jvm.JvmMultifileClass

package com.bornfight.utils

import android.os.Bundle
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Logs a [FirebaseAnalytics] event.
 *
 * @param the analytics object fetched via [FirebaseAnalytics.getInstance]
 * @param eventName event name for [FirebaseAnalytics.logEvent]
 * @param contentType [FirebaseAnalytics.Param.CONTENT_TYPE]
 * @param content [FirebaseAnalytics.Param.CONTENT]
 */
fun logFirebaseEvent(
    firebaseAnalytics: FirebaseAnalytics,
    eventName: String,
    contentType: String,
    content: String
) {
    val bundle = Bundle()
    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType)
    bundle.putString(FirebaseAnalytics.Param.CONTENT, content)
    firebaseAnalytics.logEvent(eventName, bundle)
}

/**
 * Logs a Crashlytics [Answers] event.
 * @param answers analytics object fetched via [Answers.getInstance]
 * @param eventName event name for [CustomEvent]
 * @param customAttributes a map of custom attributes for this event
 */
fun logAnswersEvent(answers: Answers, eventName: String, customAttributes: Map<String, String>) {
    val ce = CustomEvent(eventName)
    customAttributes.entries.forEach { (attributeKey, attributeValue) ->
        ce.putCustomAttribute(
            attributeKey, attributeValue.substring(0, Math.min(attributeValue.length, 99))
        )
    }
    answers.logCustom(ce)
}
