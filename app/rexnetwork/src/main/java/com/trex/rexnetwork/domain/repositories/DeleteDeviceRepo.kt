package com.trex.rexnetwork.domain.repositories

import android.util.Log
import com.trex.rexnetwork.domain.firebasecore.firesstore.DeletedDeviceFirebase
import com.trex.rexnetwork.domain.firebasecore.firesstore.DeviceFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeleteDeviceRepo(
    shopId: String,
) : BaseRepository() {
    private val deviceFirebase = DeviceFirestore(shopId)
    private val deletedDeviceFirebase = DeletedDeviceFirebase(shopId)

    fun deleteDevice(
        deviceId: String,
        onSuccess: (Boolean) -> Unit,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            deviceFirebase.getSingleDevice(deviceId, { device ->

                deletedDeviceFirebase.createOrUpdateDevice(device.deviceId, device, {
                    deviceFirebase.deleteDevice(device.deviceId, {
                        onSuccess(true)
                    }, {
                        onSuccess(false)
                    })
                }, {
                    onSuccess(false)
                })
            }, {
                Log.e("", "deleteDevice: Failed to add device to deleted list!")
            })
        }
    }

    fun getDeletedDeviceCount(onSuccess: (String) -> Unit) {
        deletedDeviceFirebase.getAllDevices({ list ->
            if (list.isEmpty()) {
                onSuccess("0")
            } else {
                onSuccess(list.size.toString())
            }
        }, {
            onSuccess("0")
        })
    }
}
