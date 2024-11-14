package com.trex.rexnetwork.domain.firebasecore.fcm

import android.content.Context
import com.trex.rexnetwork.data.UpdateTokenRequest
import com.trex.rexnetwork.domain.firebasecore.firesstore.DeviceFirestore
import com.trex.rexnetwork.domain.repositories.UpdateTokenRepo

class ClientFCMTokenUpdater(
    context: Context,
) : FCMTokenUpdater(context) {
    private lateinit var deviceFirestore: DeviceFirestore
    private val deviceId = mshardPref.getDeviceId()

    override fun updateFirestoreFCMToken(token: String) {
        if (deviceId == null) {
            failed(Exception( "Device id is null"))
            return
        }
        if (shopId == null) {
            failed(Exception( "Shop id is null"))
            return
        }
        val request =
            UpdateTokenRequest(
                token,
                shopId,
                deviceId,
            )
        UpdateTokenRepo().updateFirestoreDeviceFcmToken(request) { isSuccess ->
            if (isSuccess) {
                success()
            } else {
                failed(Exception( "Error from server!"))
            }
        }
    }
}
