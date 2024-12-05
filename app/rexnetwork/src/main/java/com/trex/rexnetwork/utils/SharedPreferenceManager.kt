package com.trex.rexnetwork.utils

import android.content.Context
import android.util.Log

class SharedPreferenceManager(
    context: Context,
) {
    private val TAG = "SHARED_PREF"
    private val keyFCMToken = "KEY_FCM_TOKEN"
    private val keyIMEI: String = "KEY_IMEI"
    private val keyShopId: String = "KEY_SHOP_ID"
    private val keyDeviceId: String = "KEY_DEVICE_ID"
    private val keyDeviceRegStatus: String = "KEY_IS_REG_COMPLETED"
    private val keyMasterUnlockCode: String = "KEY_MASTER_UNLOCK_CODE"
    private val sharePrefKey = "${context.packageName}.SHARED_PREF"
    private val sharedPreferences = context.getSharedPreferences(sharePrefKey, Context.MODE_PRIVATE)

    private fun saveString(
        key: String,
        value: String,
    ) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    private fun saveBoolean(
        key: String,
        value: Boolean,
    ) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    private fun getString(key: String): String? {
        if (prefContains(key)) {
            return sharedPreferences.getString(key, null)
        } else {
            Log.e(TAG, "getString: $key not found in shared prefs")
            return null
        }
    }

    private fun getBoolean(key: String): Boolean {
        if (prefContains(key)) {
            return sharedPreferences.getBoolean(key, false)
        } else {
            Log.e(TAG, "getString: $key not found in shared prefs")
            return false
        }
    }

    fun prefContains(key: String): Boolean = sharedPreferences.contains(key)

    fun clearPreference(onComplete: (Boolean) -> Unit) {
        sharedPreferences
            .edit()
            .clear()
            .commit()
            .apply {
                onComplete(true)
            }
    }

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

    fun saveDeviceId(deviceId: String) {
        saveString(keyDeviceId, deviceId)
    }

    fun getDeviceId() = getString(keyDeviceId)

    fun saveRegCompleteStatus(status: String) {
        saveString(keyDeviceRegStatus, status)
    }

    fun setMasterUnlockCode(code: String) {
        saveString(keyMasterUnlockCode, code)
    }

    fun getMasterUnlockCode(): String = getString(keyMasterUnlockCode) ?: ""

    fun getRegCompleteStatus(): String = getString(keyDeviceRegStatus) ?: ""
}
