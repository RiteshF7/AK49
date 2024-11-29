package com.trex.rexnetwork.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.google.firebase.FirebaseApp
import com.google.firebase.installations.FirebaseInstallations

class FirebaseInstallationManager {
    private var retryCount = 0
    private val maxRetries = 3
    private val baseDelay = 2000L // 2 seconds

    fun initializeFirebase(
        context: Context,
        onComplete: (Boolean) -> Unit,
    ) {
        if (FirebaseApp.getApps(context).isNotEmpty()) {
            retryFirebaseInstallation(onComplete)
        } else {
            FirebaseApp.initializeApp(context)
            retryFirebaseInstallation(onComplete)
        }
    }

    private fun retryFirebaseInstallation(onComplete: (Boolean) -> Unit) {
        if (retryCount >= maxRetries) {
            onComplete(false)
            return
        }

        FirebaseInstallations
            .getInstance()
            .id
            .addOnSuccessListener {
                onComplete(true)
                retryCount = 0
            }.addOnFailureListener {
                retryCount++
                val delay = baseDelay * (1 shl retryCount) // Exponential backoff
                Handler(Looper.getMainLooper()).postDelayed({
                    retryFirebaseInstallation(onComplete)
                }, delay)
            }
    }
}
