package com.trex.rexnetwork.domain.firebasecore.fcm.fcmrequestscreen

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trex.rexnetwork.data.ActionMessageDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

// ViewModel to handle business logic
class FcmRequestViewModel : ViewModel() {
    private val _state = MutableStateFlow<FcmRequestState>(FcmRequestState.Idle)
    val state: StateFlow<FcmRequestState> = _state.asStateFlow()

    private val timeoutDuration = 30.seconds
    private var requestId: String? = null
    private var timer: CountDownTimer? = null

    fun sendFcmRequest(actionMessageDTO: ActionMessageDTO) {
        viewModelScope.launch {
            try {
                _state.value = FcmRequestState.Loading
                requestId = actionMessageDTO.requestId

                // Register for response
                FcmResponseManager.registerCallback(requestId!!) { response ->
                    handleFcmResponse(response)
                }

                // Build and Send FCM message
                val message = buildMessage(actionMessageDTO)
                sendMessage(message)

                // Start timeout timer
                startTimeoutTimer()
            } catch (e: Exception) {
                _state.value = FcmRequestState.Error("Failed to send message: ${e.message}")
            }
        }
    }

    fun buildMessage(actionMessageDTO: ActionMessageDTO): String {
        // Replace this with actual logic to build a message string
        return "Sending action: ${actionMessageDTO.action} with payload: ${actionMessageDTO.payload} to ${actionMessageDTO.fcmToken}"
    }

    suspend fun sendMessage(message: String) {
        // Replace with actual send logic using FirebaseMessaging or another method
        println("Sending message: $message")
    }

    fun startTimeoutTimer() {
        timer?.cancel()
        timer =
            object : CountDownTimer(timeoutDuration.inWholeMilliseconds, 1000) {
                override fun onTick(millisUntilFinished: Long) {}

                override fun onFinish() {
                    if (_state.value is FcmRequestState.Loading) {
                        _state.value = FcmRequestState.Timeout
                        cleanup()
                    }
                }
            }.start()
    }

    fun handleFcmResponse(response: String) {
        timer?.cancel()
        _state.value = FcmRequestState.Success(response)
        cleanup()
    }

    private fun cleanup() {
        requestId?.let {
            FcmResponseManager.unregisterCallback(it)
            requestId = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        cleanup()
        timer?.cancel()
    }
}
