package com.trex.rexnetwork.domain.firebasecore.fcm

import android.content.Context
import com.trex.rexnetwork.data.NewDevice
import com.trex.rexnetwork.data.UpdateTokenRequest
import com.trex.rexnetwork.domain.firebasecore.firesstore.DeviceFirestore
import com.trex.rexnetwork.domain.repositories.UpdateTokenRepo

class ClientFCMTokenUpdater(
    context: Context,
) : FCMTokenUpdater(context) {
    private val deviceId = mshardPref.getDeviceId()
    private val updateTokenRepo = UpdateTokenRepo()

    override fun updateFirestoreFCMToken(token: String) {
        if (deviceId == null) {
            failed(Exception("Device id is null"))
            return
        }
        if (shopId == null) {
            failed(Exception("Shop id is null"))
            return
        }
        val request =
            UpdateTokenRequest(
                token,
                shopId,
                deviceId,
            )
        updateTokenRepo.updateFirestoreDeviceFcmToken(request) { isSuccess ->
            if (isSuccess) {
                success()
            } else {
                failed(Exception("Error from server!"))
            }
        }
    }

    override fun getCurrentFirestoreFCMToken(
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        if (shopId == null) throw RuntimeException("shop id not found!")
        deviceId?.let { id ->
            DeviceFirestore(shopId).getSingleField(
                id,
                NewDevice::fcmToken.name,
                { onSuccess(it.toString()) },
                onFailure,
            )
        }
    }
}
