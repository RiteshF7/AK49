package com.trex.rexnetwork.data

import com.google.gson.Gson

data class ActionMessageDTO(
    val fcmToken: String,
    val action: Actions,
    val payload: Map<String, String> = mapOf(),
    val sendOffline: Boolean = false,
)

object ActionMessageDTOMapper {
    fun fromJsonToDTO(json: String): ActionMessageDTO {
        try {
            val actionMessageDTO = Gson().fromJson(json, ActionMessageDTO::class.java)
            return actionMessageDTO
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid JSON format")
        }
    }
}
