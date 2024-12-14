package com.trex.rexnetwork.domain.firebasecore.firesstore

import com.trex.rexnetwork.data.NewDevice

class DeviceFirestore(
    shopId: String,
) : FirestoreBase<NewDevice>("shops/$shopId/devices") {
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

    fun updateDueDate(
        deviceId: String,
        newDueDate: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        updateSingleField(deviceId, NewDevice::dueDate.name, newDueDate, onSuccess, onFailure)
    }

    // Update lock status of a device
    fun updateLockStatus(
        deviceId: String,
        lockStatus: Boolean,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        updateSingleField(deviceId, NewDevice::deviceLockStatus.name, lockStatus, onSuccess, onFailure)
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
