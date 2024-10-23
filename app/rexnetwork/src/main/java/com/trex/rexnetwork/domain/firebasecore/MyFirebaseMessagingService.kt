package com.trex.rexnetwork.domain.firebasecore

import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onCreate() {
        super.onCreate()
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
        val actionString = payload["ActionMessageDTO"]
        val intent =
            Intent("com.trex.ACTION_PAYLOAD_RECEIVED").apply {
                putExtra("payload", HashMap(payload)) // Send payload as extra
            }
        if (actionString.isNullOrEmpty()) {
            Log.w("PayloadHandler", "Action string is null or empty")
            return
        }
    }

    override fun onNewToken(token: String) {
//        CommonConstants.fcmToken = token
    }

    private fun startForegroundService() {
        val serviceIntent = Intent(this, ForegroundService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }
}
