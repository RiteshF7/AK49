package com.trex.rexnetwork.data

import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewDevice(
    var shopId: String,
    var fcmToken: String,
    var deviceId:String="",
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
    val unlockCode: String = "000000",
) : BaseFirestoreResponse {
    constructor() : this("", "")
}
