package com.trex.rexnetwork.domain.repositories

import android.util.Log
import com.trex.rexnetwork.data.NewDevice
import com.trex.rexnetwork.domain.firebasecore.firesstore.DeviceFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterDeviceRepo : BaseRepository() {
    fun registerNewDevice(
        newDevice: NewDevice,
        onResult: (Boolean) -> Unit,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val deviceRepo = DeviceFirestore(newDevice.shopId)
            deviceRepo.createOrUpdateDevice(newDevice.deviceId, newDevice, {
                onResult(true)
            }, {
                Log.i("error creating device", "registerNewDevice: $it")
                onResult(false)
            })
        }
    }
}
