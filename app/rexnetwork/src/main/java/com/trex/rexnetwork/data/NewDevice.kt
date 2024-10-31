package com.trex.rexnetwork.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewDevice(
    var shopId: String,
    var fcmToken: String,
    var imeiOne: String,
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
) : BaseFirestoreResponse {
    // No-argument constructor required for Firestore
    constructor() : this("", "", "")
}
