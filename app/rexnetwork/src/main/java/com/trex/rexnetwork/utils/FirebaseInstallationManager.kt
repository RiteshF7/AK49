package com.trex.rexnetwork.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.provider.Settings.Global.getString
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.installations.FirebaseInstallations
import org.json.JSONObject

class FirebaseInstallationManager {
    private var retryCount = 0
    private val maxRetries = 3
    private val baseDelay = 2000L // 2 seconds

    fun getFirebaseOptions(context: Context): FirebaseOptions {
        try {
            // Read google-services.json from assets
            val inputStream = context.assets.open("google-services.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            // Parse JSON
            val jsonObject = JSONObject(jsonString)
            val projectInfo = jsonObject.getJSONObject("project_info")
            val client = jsonObject.getJSONArray("client").getJSONObject(0)
            val clientInfo = client.getJSONObject("client_info")
            val apiKey = client.getJSONArray("api_key").getJSONObject(0)

            // Extract values
            val projectId = projectInfo.getString("project_id")
            val applicationId =
                clientInfo
                    .getJSONObject("android_client_info")
                    .getString("package_name")
            val apiKeyStr = apiKey.getString("current_key")
            val storageBucket = projectInfo.getString("storage_bucket")

            // Build and return Firebase options
            return FirebaseOptions
                .Builder()
                .setProjectId(projectId)
                .setApplicationId(applicationId)
                .setApiKey(apiKeyStr)
                .setStorageBucket(storageBucket)
                .build()
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Failed to parse google-services.json", e)
        }
    }

    fun initializeFirebase(
        context: Context,
        onComplete: (Boolean) -> Unit,
    ) {
        if (FirebaseApp.getApps(context).isNotEmpty()) {
            retryFirebaseInstallation(onComplete)
        } else {
            val options = getFirebaseOptions(context)
            FirebaseApp.initializeApp(context, options)
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
