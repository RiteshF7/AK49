package com.trex.rexnetwork.domain.firebasecore.fcm

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.trex.rexnetwork.Constants

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private lateinit var fcmTokenManager: FCMTokenManager
    private lateinit var updater: IFCMTokenUpdater

    override fun onCreate() {
        super.onCreate()
        updater =
            if (packageName == "com.trex.rexandroidsecureclient") {
                ClientFCMTokenUpdater(applicationContext)
            } else {
                ShopFcmTokenUpdater(applicationContext)
            }
        fcmTokenManager = FCMTokenManager(applicationContext, updater)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val restartServiceIntent =
            Intent(applicationContext, MyFirebaseMessagingService::class.java).also {
                it.setPackage(packageName)
            }
        val restartServicePendingIntent: PendingIntent =
            PendingIntent.getService(
                this,
                1,
                restartServiceIntent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE,
            )
        applicationContext.getSystemService(Context.ALARM_SERVICE).let { it as? AlarmManager }?.set(
            AlarmManager.ELAPSED_REALTIME,
            System.currentTimeMillis() + 1000,
            restartServicePendingIntent,
        )
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel =
            NotificationChannel(
                CHANNEL_ID,
                "FCM Service Channel",
                NotificationManager.IMPORTANCE_HIGH,
            )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        val builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationCompat.Builder(this, CHANNEL_ID)
            } else {
                NotificationCompat.Builder(this)
            }

        return builder
            .setContentTitle("App is running")
            .setContentText("Maintaining connection to FCM")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()
    }

    companion object {
        private const val CHANNEL_ID = "fcm_service_channel"
        private const val NOTIFICATION_ID = 1
    }

    private fun startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            startForeground(NOTIFICATION_ID, createNotification())
        } else {
            val serviceIntent = Intent(this, MyFirebaseMessagingService::class.java)
            ContextCompat.startForegroundService(this, serviceIntent)
        }
    }
}
