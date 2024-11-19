package com.trex.rexnetwork.domain.firebasecore.fcm

import android.content.Context
import com.trex.rexnetwork.domain.firebasecore.firesstore.FCMTokenFirestore
import com.trex.rexnetwork.domain.firebasecore.firesstore.Shop
import com.trex.rexnetwork.domain.firebasecore.firesstore.ShopFirestore

class ShopFcmTokenUpdater(
    context: Context,
) : FCMTokenUpdater(context) {
    private val fcmTokenManagerFirestore = FCMTokenFirestore()

    override fun updateFirestoreFCMToken(token: String) {
        if (shopId == null) return
        fcmTokenManagerFirestore.saveFcmTokenToFirebase(shopId, token)
    }

    override fun getCurrentFirestoreFCMToken(
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        if (shopId == null) throw RuntimeException("Shop id not found!")
        ShopFirestore().getSingleField(
            shopId,
            Shop::fcmToken.name,
            { onSuccess(it.toString()) },
            onFailure,
        )
    }
}
