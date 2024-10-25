package com.trex.rexnetwork.utils

import android.content.Context
import android.util.Log

class SharedPreferenceManager(
    private val context: Context,
) {
    private val TAG = "SHARED_PREF"
    private val keyFCMToken = "KEY_FCM_TOKEN"
    private val keyIMEI: String = "KEY_IMEI"
    private val keyShopId: String = "KEY_SHOP_ID"
    private val sharePrefKey = "${context.packageName}.SHARED_PREF"
    private val sharedPreferences = context.getSharedPreferences(sharePrefKey, Context.MODE_PRIVATE)

    private fun saveString(
        key: String,
        value: String,
    ) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    private fun getString(key: String): String? {
        if (prefContains(key)) {
            return sharedPreferences.getString(key, null)
        } else {
            Log.e(TAG, "getString: $key not found in shared prefs")
            return null
        }
    }

    fun prefContains(key: String): Boolean = sharedPreferences.contains(key)

    fun saveFcmToken(fcmToken: String) {
        saveString(keyFCMToken, fcmToken)
    }

    fun getFCMToken() = getString(keyFCMToken)

    fun saveImei(imei: String) {
        saveString(keyIMEI, imei)
    }

    fun getIMEI() = getString(keyIMEI)

    fun saveShopId(shopId: String) {
        saveString(keyShopId, shopId)
    }

    fun getShopId() = getString(keyShopId)
}
