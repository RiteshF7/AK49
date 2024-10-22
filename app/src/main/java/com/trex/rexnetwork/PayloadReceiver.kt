package com.trex.rexnetwork

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.ActionMessageDTOFactory
import com.trex.rexnetwork.domain.repositories.SendActionMessageRepository

class PayloadReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val actionMessageString =
            intent.getStringExtra(ActionMessageDTO::class.java.simpleName)

        if (actionMessageString.isNullOrBlank()) {
            logErrorMessage("message not found in payload!")
            return
        }
        val actionMessageDTO = ActionMessageDTOFactory.fromJsonToDTO(actionMessageString)

        if (actionMessageDTO == null) {
            logErrorMessage("unable to cast from string to dto using gson!")
            return
        }

        Log.i("", "onReceive: ${actionMessageDTO.actions}")

        //try to send back to shop
        val shopFcmDto =
            SendActionMessageRepository().sendActionMessage(actionMessageDTO)

    }

    fun logErrorMessage(errorMessage: String) {
        Log.e("error receiving payload!", "logErrorMessage: ${errorMessage}")
    }
}
