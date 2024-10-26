package com.trex.rexnetwork.domain.firebasecore.firesstore

import android.util.Log
import com.trex.rexnetwork.data.BaseFirestoreResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class FcmToken(
    val token: String,
) : BaseFirestoreResponse {
    constructor() : this("")
}

class FCMTokenFirestore : FirestoreBase<FcmToken>("fcmToken") {
    override fun dataClass(): Class<FcmToken> = FcmToken::class.java

    fun saveFcmTokenToFirebase(
        tokenId: String,
        token: String,
    ) {
        val fcmToken = FcmToken(token)
        addOrUpdateDocument(
            tokenId,
            fcmToken,
            { Log.i("", "saveFcmToken: success") },
            { error -> Log.e("", "saveFcmToken: $error") },
        )
    }

    fun getFcmToken(
        tokenId: String,
        onSucces: (String) -> Unit,
    ) {
        getDocument(tokenId, dataClass(), {
            onSucces(it.token)
        }, { error ->
            Log.e("", "getFcmToken: $error")
        })
    }
}
