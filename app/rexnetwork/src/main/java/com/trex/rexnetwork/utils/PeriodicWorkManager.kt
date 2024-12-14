package com.trex.rexnetwork.utils

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.trex.rexnetwork.domain.firebasecore.fcm.ClientFCMTokenUpdater
import com.trex.rexnetwork.domain.firebasecore.fcm.FCMTokenManager
import com.trex.rexnetwork.domain.firebasecore.fcm.ShopFcmTokenUpdater
import java.util.concurrent.TimeUnit

class PeriodicWorkManager(
    private val context: Context,
    workerParameters: WorkerParameters,
) : Worker(context, workerParameters) {
    private val sharedPreferenceManager = SharedPreferenceManager(context)

    override fun doWork(): Result {
        // Your task logic here
        if (applicationContext.packageName == "com.trex.rexandroidsecureclient") {
            sharedPreferenceManager.getShopId()?.let { shopId ->
                sharedPreferenceManager.getDeviceId()?.let { deviceId ->
                    FCMTokenManager(
                        context = context,
                        ClientFCMTokenUpdater(context),
                    ).refreshToken { }
                }
            }
        } else {
            sharedPreferenceManager.getShopId()?.let {
                FCMTokenManager(context, ShopFcmTokenUpdater(context)).refreshToken { }
            }
        }
        return Result.success()
    }

    companion object {
        fun startPeriodicWork(context: Context) {
            val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

            val periodicWorkRequest =
                PeriodicWorkRequestBuilder<PeriodicWorkManager>(
                    1,
                    TimeUnit.HOURS, // Repeat interval
                    15,
                    TimeUnit.MINUTES, // Flex interval
                ).setConstraints(constraints).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "periodic_task",
                ExistingPeriodicWorkPolicy.UPDATE,
                periodicWorkRequest,
            )
        }
    }
}
