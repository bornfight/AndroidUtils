@file:kotlin.jvm.JvmName("AnalyticsUtil")
@file:kotlin.jvm.JvmMultifileClass

package com.bornfight.utils

import android.os.Bundle
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
