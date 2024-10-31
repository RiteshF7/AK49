package com.trex.rexnetwork.data

import android.content.Intent
import android.os.Parcelable
import android.util.Log
import com.google.gson.Gson
import com.trex.rexnetwork.Constants
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class ActionMessageDTO(
    val fcmToken: String,
    val action: Actions,
    val payload: Map<String, String> = mapOf(),
    val sendOffline: Boolean = false,
    val requestId: String = UUID.randomUUID().toString(),
) : Parcelable

object ActionMessageDTOMapper {
    fun getMessageDTOFromIntent(intent: Intent): ActionMessageDTO? {
        val actionMessageString = getPayloadString(intent)

        if (actionMessageString.isNullOrBlank()) {
            Log.e("", "message not found in payload!")
            return null
        }
        val actionMessageDTO = fromJsonToDTO(actionMessageString)
        if (actionMessageDTO == null) {
            Log.e("", "unable to convert json to MESSAGE_DTO object using GSON !")
            return null
        }
        return actionMessageDTO
    }

    fun getPayloadString(intent: Intent): String? = intent.getStringExtra(Constants.KEY_PAYLOAD_DATA)

    fun fromJsonToDTO(json: String): ActionMessageDTO? {
        try {
            val actionMessageDTO = Gson().fromJson(json, ActionMessageDTO::class.java)
            return actionMessageDTO
        } catch (e: Exception) {
            return null
        }
    }
}
