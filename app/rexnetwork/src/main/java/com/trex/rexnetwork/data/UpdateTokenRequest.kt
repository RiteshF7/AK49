package com.trex.rexnetwork.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateTokenRequest(
    val token: String,
    val shopId: String,
    val deviceId: String,
):Parcelable
