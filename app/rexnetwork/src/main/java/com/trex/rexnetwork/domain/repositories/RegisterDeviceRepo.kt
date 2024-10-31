package com.trex.rexnetwork.domain.repositories

import com.trex.rexnetwork.data.NewDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterDeviceRepo : BaseRepository() {

    fun registerNewDevice(
        newDevice: NewDevice,
        onResult: (Boolean) -> Unit,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.registerNewDevice(newDevice)
            onResult(response.isSuccessful)
        }
    }
}
