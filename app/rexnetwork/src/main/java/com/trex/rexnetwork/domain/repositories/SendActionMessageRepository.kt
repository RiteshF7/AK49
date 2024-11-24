package com.trex.rexnetwork.domain.repositories

import android.util.Log
import com.trex.rexnetwork.RetrofitClient
import com.trex.rexnetwork.data.ActionMessageDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SendActionMessageRepository : BaseRepository() {
    fun sendActionMessage(message: ActionMessageDTO) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("Sending message ::: ", "sendActionMessage: $message")
//            RetrofitClient.sendOnlineMessageSafely(message)
            apiService.sendOnlineMessage(message)
        }
    }

    suspend fun verifyCode(
        code: String,
        shopId: String,
        deviceId: String,
    ): Boolean =
        try {
            val response = apiService.verifyCode(code, shopId, deviceId)

            if (response.isSuccessful) {
                response.body() ?: false
            } else {
                println("Error: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
}
