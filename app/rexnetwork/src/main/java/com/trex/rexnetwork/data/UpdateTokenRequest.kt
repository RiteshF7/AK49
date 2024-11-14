package com.trex.rexnetwork.data

data class UpdateTokenRequest(
    val token: String,
    val shopId: String,
    val deviceId: String,
)
