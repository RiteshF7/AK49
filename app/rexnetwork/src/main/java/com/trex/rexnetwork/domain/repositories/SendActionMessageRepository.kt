package com.trex.rexnetwork.domain.repositories

import com.trex.rexnetwork.data.ActionMessageDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SendActionMessageRepository : BaseRepository() {
    fun sendActionMessage(message: ActionMessageDTO) {
        CoroutineScope(Dispatchers.IO).launch {
            apiService.sendOnlineMessage(message)
        }
    }
}
