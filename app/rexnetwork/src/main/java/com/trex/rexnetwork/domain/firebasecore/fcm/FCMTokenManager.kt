package com.trex.rexnetwork.domain.firebasecore.fcm

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.trex.rexnetwork.utils.SharedPreferenceManager

class FCMTokenManager(
    private val context: Context,
    private val updater: IFCMTokenUpdater,
) {
    private val mShardPref = SharedPreferenceManager(context)
    private var currentToken = mShardPref.getFCMToken()

    fun saveFcmToken(fcmToken: String) {
        mShardPref.saveFcmToken(fcmToken)
        updater.updateFirestoreFCMToken(fcmToken)
        currentToken = fcmToken
    }

    fun refreshToken(onComplete: (String) -> Unit) {
        // Then attempt to get FCM token
        getFCMToken(context = context) { result ->
            result.fold(
                onSuccess = { token ->
                    saveFcmToken(token)
                    onComplete(token)
                },
                onFailure = { exception ->
                    Log.e("FCM", "Failed to get token", exception)
                },
            )
        }
    }

    private fun getFCMToken(
        context: Context,
        callback: (Result<String>) -> Unit,
    ) {
        try {
            FirebaseMessaging
                .getInstance()
                .token
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        callback(Result.success(task.result!!))
                    } else {
                        // Force refresh Firebase Installation
                        refreshFirebaseInstallation {
                            // Retry getting token after refresh
                            retryTokenFetch(context, callback)
                        }
                    }
                }.addOnFailureListener { exception ->
                    Log.e("FCM", "Token fetch failed", exception)
                    callback(Result.failure(exception))
                }
        } catch (e: Exception) {
            Log.e("FCM", "Error getting FCM token", e)
            callback(Result.failure(e))
        }
    }

    private fun refreshFirebaseInstallation(onComplete: () -> Unit) {
        FirebaseInstallations
            .getInstance()
            .delete()
            .addOnCompleteListener {
                FirebaseInstallations
                    .getInstance()
                    .id
                    .addOnCompleteListener {
                        onComplete()
                    }
            }
    }

    private fun retryTokenFetch(
        context: Context,
        callback: (Result<String>) -> Unit,
    ) {
        // Retry with delay
        Handler(Looper.getMainLooper()).postDelayed({
            getFCMToken(context, callback)
        }, 5000)
    }
}
