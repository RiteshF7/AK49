package com.trex.rexnetwork.domain.repositories

import com.trex.rexnetwork.RetrofitClient
import com.trex.rexnetwork.RexKtorServer

open class BaseRepository {
    protected val apiService: RexKtorServer = RetrofitClient.getBuilder
}