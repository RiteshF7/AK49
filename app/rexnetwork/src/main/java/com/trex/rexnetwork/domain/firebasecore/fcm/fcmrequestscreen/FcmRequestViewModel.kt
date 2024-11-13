package com.trex.rexnetwork.domain.firebasecore.fcm.fcmrequestscreen

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trex.rexnetwork.Constants
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.domain.repositories.SendActionMessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

// ViewModel to handle business logic
class FcmRequestViewModel : ViewModel() {
    private val _state = MutableStateFlow<FcmRequestState>(FcmRequestState.Idle)
    val state: StateFlow<FcmRequestState> = _state.asStateFlow()
    private val repo = SendActionMessageRepository()

    private val timeoutDuration = 60.seconds
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
                sendMessage(actionMessageDTO)

                // Start timeout timer
                startTimeoutTimer()
            } catch (e: Exception) {
                _state.value = FcmRequestState.Error("Failed to send message: ${e.message}")
            }
        }
    }

    fun sendMessage(message: ActionMessageDTO) {
        repo.sendActionMessage(message)
    }

    fun startTimeoutTimer() {
        timer?.cancel()
        timer =
            object : CountDownTimer(timeoutDuration.inWholeMilliseconds, 1000) {
                override fun onTick(millisUntilFinished: Long) {}

                override fun onFinish() {
                    if (_state.value is FcmRequestState.Loading) {
                        _state.value = FcmRequestState.Timeout
                        cleanup(false)
                    }
                }
            }.start()
    }

    fun handleFcmResponse(response: ActionMessageDTO) {
        val fcmResponseStatus = response.payload[Constants.KEY_RESPOSE_RESULT_STATUS]
        if (fcmResponseStatus.equals(Constants.RESPONSE_RESULT_SUCCESS)) {
            _state.value = FcmRequestState.Success(response)
        } else {
            _state.value = FcmRequestState.Error("Failed reaction result")
        }
        timer?.cancel()
        cleanup()
    }

    private fun cleanup(isrequestConsumed: Boolean = true) {
        requestId?.let {
            FcmResponseManager.unregisterCallback(it, isrequestConsumed)
            requestId = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        cleanup()
        timer?.cancel()
    }
}
