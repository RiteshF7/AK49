package com.trex.rexnetwork.domain.firebasecore.fcm.fcmrequestscreen

import com.trex.rexnetwork.data.ActionMessageDTO

// FCM Response Manager (Singleton)
object FcmResponseManager {
    private val callbacks = mutableMapOf<String, (ActionMessageDTO) -> Unit>()
    private val expiredRequests = mutableMapOf<String, String>()

    fun registerCallback(
        requestId: String,
        callback: (ActionMessageDTO) -> Unit,
    ) {
        callbacks[requestId] = callback
    }

    fun unregisterCallback(
        requestId: String,
        isRequestConsumed: Boolean,
    ) {
        if (!isRequestConsumed) expiredRequests[requestId] = ""
        callbacks.remove(requestId)
    }

    fun handleResponse(
        requestId: String,
        response: ActionMessageDTO,
    ) {
        if (expiredRequests.contains(requestId)) {
            expiredRequests.remove(requestId)
            return
        }
        callbacks[requestId]?.invoke(response)
    }

    fun hasRequest(requestId: String) = callbacks.contains(requestId) || expiredRequests.contains(requestId)
}
