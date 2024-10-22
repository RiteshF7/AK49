package com.trex.rexnetwork.data

import com.google.gson.Gson
import com.plcoding.data.Actions

data class ActionMessageDTO(
    val fcmToken: String,
    val actions: Actions,
    val payload: Map<String, String> = mapOf(),
    val sendOffline: Boolean = false,
)

object ActionMessageDTOFactory {

    fun fromJsonToDTO(json: String): ActionMessageDTO? {
        val actionMessageDTO = Gson().fromJson(json, ActionMessageDTO::class.java)
        return actionMessageDTO
    }

}
