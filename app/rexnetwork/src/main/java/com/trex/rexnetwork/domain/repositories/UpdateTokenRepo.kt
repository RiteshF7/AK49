package com.trex.rexnetwork.domain.repositories

import android.util.Log
import com.trex.rexnetwork.data.UpdateTokenRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateTokenRepo : BaseRepository() {
    fun updateFirestoreDeviceFcmToken(
        updateTokenRequest: UpdateTokenRequest,
        onResult: (Boolean) -> Unit,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("", "updateFirestoreDeviceFcmToken: $updateTokenRequest")
            val result = apiService.updateFcmToken(updateTokenRequest)
            if (result.isSuccessful) {
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    fun getDeviceFirestoreToken(deviceId: String?): String? {
        // todo implement it
        return "some token from server"
    }
}
