package com.trex.rexnetwork.domain.firebasecore.fcm

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.trex.rexnetwork.utils.SharedPreferenceManager

class FCMTokenManager(
    context: Context,
    private val updater: IFCMTokenUpdater,
) {
    private val mShardPref = SharedPreferenceManager(context)
    private var currentToken = mShardPref.getFCMToken()

    fun saveFcmToken(fcmToken: String) {
        mShardPref.saveFcmToken(fcmToken)
        updater.updateFirestoreFCMToken(fcmToken)
        currentToken = fcmToken
    }

    fun refreshToken(token: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("", "Fetching FCM token failed! ${task.exception}")
                return@addOnCompleteListener
            }
            val tokenFromServer = task.result
            if (token != tokenFromServer) {
                saveFcmToken(tokenFromServer)
            }
        }
    }

    fun getFcmToken(): String = currentToken ?: throw NullPointerException("Client fcm token is null")
}
