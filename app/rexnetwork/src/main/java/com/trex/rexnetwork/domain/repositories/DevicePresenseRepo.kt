package com.trex.rexnetwork.domain.repositories

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class DeviceRegistration(
    val deviceId: String,
    val shopId: String,
)

class DevicePresenceRepo : BaseRepository() {
    fun registerPresenceMonitoring(devicePresenceStatus: DeviceRegistration) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.startPresenceMonitoring(devicePresenceStatus)
            if (response.isSuccessful) {
                Log.i(TAG, "registerPresenceMonitoring: ${response.body()}")
            } else {
                Log.e(TAG, "registerPresenceMonitoring: error in monitoring device!")
            }
        }
    }

    fun stopPresenceMonitoring(deviceId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.stopPresenceMonitoring(deviceId)
            if (response.isSuccessful) {
                Log.i(TAG, "registerPresenceMonitoring: ${response.body()}")
            } else {
                Log.e(TAG, "registerPresenceMonitoring: errors stopping monitoring device!")
            }
        }
    }

    companion object {
        const val TAG = "MONITORING REPOSITORY "
    }
}
