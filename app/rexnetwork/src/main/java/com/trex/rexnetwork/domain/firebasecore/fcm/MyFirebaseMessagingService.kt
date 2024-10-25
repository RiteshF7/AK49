package com.trex.rexnetwork.domain.firebasecore.fcm

import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.trex.rexnetwork.Constants

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private lateinit var fcmTokenManager: FCMTokenManager

    override fun onCreate() {
        super.onCreate()
        val updater =
            if (packageName == "com.trex.rexandroidsecureclient") {
                ClientFCMTokenUpdater(applicationContext)
            } else {
                ShopFcmTokenUpdater(applicationContext)
            }
        fcmTokenManager = FCMTokenManager(applicationContext, updater)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        startForegroundService()

        // Acquire a wake lock to ensure processing even when the device is asleep
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp:FCMWakeLock")
        wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/)

        try {
            val payload = remoteMessage.data
            Log.i("PayloadHandler", "Payload: $payload")
            handlePayload(payload)
        } finally {
            wakeLock.release()
        }
    }

    fun handlePayload(payload: Map<String, String>) {
        val actionString = payload[Constants.KEY_PAYLOAD_DATA]
        val packageName = applicationContext.packageName

        if (actionString.isNullOrEmpty()) {
            Log.w("PayloadHandler", "Action string is null or empty")
            return
        }
        actionString.let { extra ->
            val intent =
                Intent("$packageName.${Constants.KEY_BROADCAST_PAYLOAD_ACTION}").apply {
                    putExtra(Constants.KEY_PAYLOAD_DATA, extra)
                }
            sendBroadcast(intent)
        }
    }

    override fun onNewToken(token: String) {
        fcmTokenManager.saveFcmToken(token)
    }

    private fun startForegroundService() {
        val serviceIntent = Intent(this, ForegroundService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }
}
