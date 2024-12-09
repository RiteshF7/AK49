package com.trex.rexnetwork.domain.repositories

import com.trex.rexnetwork.RexKtorServer
import retrofit2.Response

class FileRepository : BaseRepository() {
    suspend fun getRascFileUrl(): Response<RexKtorServer.FileUrlResponse> = apiService.getRascApkUrl()
}
