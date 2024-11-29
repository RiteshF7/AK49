package com.trex.rexnetwork.data

import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize
import kotlin.random.Random

@Parcelize
data class NewDevice(
    var shopId: String,
    var fcmToken: String,
    var deviceId: String = "",
    var imeiOne: String = "",
    val manufacturer: String = "",
    val brand: String = "",
    var modelNumber: String = "",
    val androidVersion: String = "",
    var imeiTwo: String = "000",
    val isRegComplete: Boolean = false,
    val deviceCode: String = "",
    var costumerName: String = "",
    var costumerPhone: String = "",
    var emiPerMonth: String = "",
    var dueDate: String = "",
    var durationInMonths: String = "",
    val isDeviceLocked: Boolean = false,
    val isCameraLocked: Boolean = false,
    val isCallLocked: Boolean = false,
    val masterCode: String = Random.nextInt(1, 100000).toString(),
    val unlockCode: String = Random.nextInt(1, 100000).toString(),
    val createdAt: Timestamp = Timestamp.now(),
) : BaseFirestoreResponse {
    constructor() : this("", "")
}
