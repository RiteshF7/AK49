package com.trex.rexnetwork.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeviceInfo(
    val fcmToken: String,
    val deviceModel: String,
) : Parcelable
