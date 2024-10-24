package com.trex.rexnetwork

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.trex.rexnetwork.data.ActionMessageDTOMapper
import com.trex.rexnetwork.domain.repositories.SendActionMessageRepository

class PayloadReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        //THIS IS TEST
//        val actionMessageString =
//            intent.getStringExtra(Constants.KEY_PAYLOAD_DATA)
//
//        if (actionMessageString.isNullOrBlank()) {
//            logErrorMessage("message not found in payload!")
//            return
//        }
//        val actionMessageDTO = ActionMessageDTOMapper.fromJsonToDTO(actionMessageString)
//
//        Log.i("", "onReceive: ${actionMessageDTO.action}")

        // try to send back to shop
        // make this receiver in both apps
        // send from shop app suing sdk
        // receive from client app
        // send back data to shop using sdk
        // receive in shop app
        // make actionExecuters for shop and client app and perform action needed  !!
        // ypou are doing good dont take too much stress on yourself
        // everything will work out just keed trying and beleive in yourself
//        val shopFcmDto =
//            SendActionMessageRepository().sendActionMessage(actionMessageDTO)
    }

    fun logErrorMessage(errorMessage: String) {
        Log.e("error receiving payload!", "logErrorMessage: $errorMessage")
    }
}
