package com.trex.rexnetwork.domain.firebasecore

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.trex.rexnetwork.data.NewDevice
import com.trex.rexnetwork.utils.SharedPreferenceManager

class FCMTokenManager(
    context: Context,
) {
    private val mShardPref = SharedPreferenceManager(context)
    private lateinit var deviceFirestore: DeviceFirestore

    init {
        updateIfTokenOutdated()
        mShardPref.getShopId()?.let { shopId ->
            deviceFirestore = DeviceFirestore(shopId)
        } ?: {
            Log.e("", "error getting shop id! ")
        }
    }

    fun saveFcmToken(fcmToken: String) {
        val currentFcm = mShardPref.getFCMToken()
        if (currentFcm == null) {
            mShardPref.saveFcmToken(fcmToken)
        } else {
            updateFcmToken(fcmToken)
        }
    }

    private fun updateIfTokenOutdated() {
        mShardPref.getFCMToken()?.let { localToken ->
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("", "Fetching FCM token failed! ${task.exception}")
                    return@addOnCompleteListener
                }
                val currentToken = task.result
                if (currentToken != localToken) {
                    updateFcmToken(currentToken)
                }
            }
        }
    }

    private fun updateFcmToken(fcmToken: String) {
        mShardPref.getIMEI()?.let { imei ->
            deviceFirestore.updateSingleField(
                imei,
                NewDevice::fcmToken.name,
                fcmToken,
                {
                    mShardPref.saveFcmToken(fcmToken)
                    Log.i("", "updateFcmToken: Updated FCM TOKEN SUCCESS")
                },
                { error ->
                    Log.i("", "updateFcmToken: Updated FCM TOKEN FAILED ${error.message}")
                },
            )
        }
    }
}
