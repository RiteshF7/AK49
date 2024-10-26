package com.trex.rexnetwork.domain.firebasecore.fcm

import android.content.Context
import com.trex.rexnetwork.domain.firebasecore.firesstore.FCMTokenFirestore

class ShopFcmTokenUpdater(
    context: Context,
) : FCMTokenUpdater(context) {
    private val fcmTokenManagerFirestore = FCMTokenFirestore()

    override fun updateFirestoreFCMToken(token: String) {
        if (shopId == null) return
        fcmTokenManagerFirestore.saveFcmTokenToFirebase(shopId, token)
    }
}
