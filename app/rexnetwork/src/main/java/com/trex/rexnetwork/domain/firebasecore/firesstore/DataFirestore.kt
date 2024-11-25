package com.trex.rexnetwork.domain.firebasecore.firesstore

import com.trex.rexnetwork.data.BaseFirestoreResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class FirestoreExtraData(
    val url: String="",
    val checksum: String="",
) : BaseFirestoreResponse
class DataFirestore : FirestoreBase<FirestoreExtraData>("data") {
    override fun dataClass(): Class<FirestoreExtraData> = FirestoreExtraData::class.java

    fun getExtraData(
        onSuccess: (FirestoreExtraData) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        getDocument(
            "apk_v1",
            dataClass(),
            onSuccess,
            onFailure,
        )
    }
}
