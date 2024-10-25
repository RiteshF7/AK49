package com.trex.rexnetwork.domain.firebasecore.fcm

import android.content.Context
import com.trex.rexnetwork.domain.firebasecore.firesstore.DeviceFirestore

class ClientFCMTokenUpdater(
    context: Context,
) : FCMTokenUpdater(context) {
    private lateinit var deviceFirestore: DeviceFirestore
    private val imei = mshardPref.getIMEI()

    init {
        shopId?.let { DeviceFirestore(it) }
    }

    override fun updateFirestoreFCMToken(token: String) {
        if (imei == null) return
        deviceFirestore.updateSingleField(
            imei,
            fieldName,
            token,
            success,
            failed,
        )
    }
}
