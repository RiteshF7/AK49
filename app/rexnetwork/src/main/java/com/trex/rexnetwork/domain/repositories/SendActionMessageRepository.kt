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

    fun updateMasterCode(
        shopId:String,
        deviceId: String,
        onSuccess: (String) -> Unit,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.updateMasterCode(shopId,deviceId)

                if (response.isSuccessful) {
                    val newCode = response.body() ?: ""
                    onSuccess(newCode)
                } else {
                    println("Error: ${response.code()}")
                    onSuccess("")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onSuccess("")
            }
        }
    }

    suspend fun getUnlockCode(
        shopId: String,
        deviceId: String,
    ): String =
        try {
            val response = apiService.getUnlockCode(shopId, deviceId)

            if (response.isSuccessful) {
                response.body() ?: ""
            } else {
                println("Error: ${response.code()}")
                ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
}
