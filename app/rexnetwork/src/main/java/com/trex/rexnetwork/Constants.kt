package com.trex.rexnetwork

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

object Constants {
    const val BASE_URL = "http://192.168.0.165:8080/"

    //    const val BASE_URL = "http://10.0.2.2:8080"
    const val KEY_PAYLOAD_DATA = "ActionMessageDTO"
    const val KEY_BROADCAST_PAYLOAD_ACTION = "ACTION_PAYLOAD_RECEIVED"
    const val FCM_TOKEN = ""

    fun getFcmToken(onCompleteListener: (String) -> Unit) {
        var fcmToken = FCM_TOKEN
        if (fcmToken.isNotBlank()) {
            onCompleteListener(fcmToken)
            return
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            onCompleteListener(fcmToken)
        }
    }
}
