package com.trex.rexnetwork.domain.firebasecore.firesstore

import com.trex.rexnetwork.data.NewDevice

class DeletedDeviceFirebase(
    shopId: String,
) : FirestoreBase<NewDevice>("shops/$shopId/deleteddevices") {
    override fun dataClass(): Class<NewDevice> = NewDevice::class.java

    // Create or update a device
    fun createOrUpdateDevice(
        deviceId: String? = null,
        device: NewDevice,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        addOrUpdateDocument(deviceId, device, onSuccess, onFailure)
    }

    // Update lock status of a device
    fun updateLockStatus(
        deviceId: String,
        lockStatus: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        updateSingleField(deviceId, "lockStatus", lockStatus, onSuccess, onFailure)
    }

    // Delete a device
    fun deleteDevice(
        deviceId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        deleteDocument(deviceId, onSuccess, onFailure)
    }

    fun getAllDevices(
        onSuccess: (List<NewDevice>) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        getAllDocuments(dataClass(), onSuccess, onFailure)
    }

    fun getSingleDevice(
        deviceId: String,
        onSuccess: (NewDevice) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        getDocument(deviceId, dataClass(), onSuccess, onFailure)
    }
}
