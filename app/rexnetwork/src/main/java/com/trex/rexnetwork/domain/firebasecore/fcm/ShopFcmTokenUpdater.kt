package com.trex.rexnetwork.domain.firebasecore.fcm

import android.content.Context
import com.trex.rexnetwork.domain.firebasecore.firesstore.ShopFirestore

class ShopFcmTokenUpdater(
    context: Context,
) : FCMTokenUpdater(context) {
    private val shopFirestore = ShopFirestore()

    override fun updateFirestoreFCMToken(token: String) {
        if (shopId == null) return
        shopFirestore.updateSingleField(
            shopId,
            fieldName,
            token,
            success,
            failed,
        )
    }
}
