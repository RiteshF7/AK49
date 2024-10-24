package com.trex.rexnetwork.domain.repositories

import android.util.Log
import com.trex.rexnetwork.data.ActionMessageDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SendActionMessageRepository : BaseRepository() {
    fun sendActionMessage(message: ActionMessageDTO) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("Sending message ::: ", "sendActionMessage: $message")
            apiService.sendOnlineMessage(message)
        }
    }
}
