package com.trex.rexnetwork.domain.firebasecore.fcm.fcmrequestscreen

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.Actions
import com.trex.rexnetwork.utils.getExtraData

// State class to manage UI state
sealed class FcmRequestState {
    object Idle : FcmRequestState()

    object Loading : FcmRequestState()

    data class Error(
        val message: String,
    ) : FcmRequestState()

    data class Success(
        val response: ActionMessageDTO,
    ) : FcmRequestState()

    object Timeout : FcmRequestState()
}

// FCM Response Manager (Singleton)
object FcmResponseManager {
    private val callbacks = mutableMapOf<String, (ActionMessageDTO) -> Unit>()

    fun registerCallback(
        requestId: String,
        callback: (ActionMessageDTO) -> Unit,
    ) {
        callbacks[requestId] = callback
    }

    fun unregisterCallback(requestId: String) {
        callbacks.remove(requestId)
    }

    fun handleResponse(
        requestId: String,
        response: ActionMessageDTO,
    ) {
        callbacks[requestId]?.invoke(response)
    }
}

class FcmRequestActivity : ComponentActivity() {
    private val viewModel: FcmRequestViewModel by viewModels()
    private lateinit var messageData: ActionMessageDTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        messageData = intent.getExtraData<ActionMessageDTO>()

        setContent {
            FcmRequestScreen(
                viewModel = viewModel,
                actionMessageDTO = messageData,
                onComplete = {
                    finish()
                },
            )
        }
    }
}

@Composable
fun FcmRequestScreen(
    viewModel: FcmRequestViewModel,
    actionMessageDTO: ActionMessageDTO,
    onComplete: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    var showRetryDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.sendFcmRequest(actionMessageDTO)
    }

    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        when (val currentState = state) {
            is FcmRequestState.Loading -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    CircularProgressIndicator()
                    Text("Waiting for response...")
                }
            }

            is FcmRequestState.Error -> {
                ErrorContent(
                    error = currentState.message,
                    onRetry = { viewModel.sendFcmRequest(actionMessageDTO) },
                    onCancel = onComplete,
                )
            }

            is FcmRequestState.Success -> {
                LaunchedEffect(Unit) {
                    startResultActivity(currentState.response)
                    onComplete()
                }
            }

            is FcmRequestState.Timeout -> {
                showRetryDialog = true
            }

            is FcmRequestState.Idle -> {
            }
        }
    }

    if (showRetryDialog) {
        RetryDialog(
            onRetry = {
                showRetryDialog = false
                viewModel.sendFcmRequest(actionMessageDTO)
            },
            onCancel = onComplete,
        )
    }
}

fun startResultActivity(response: ActionMessageDTO) {
    //show success screen
    Log.i("some!!!", "startResultActivity: Completed flow")
}

@Composable
fun ErrorContent(
    error: String,
    onRetry: () -> Unit,
    onCancel: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = error,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(onClick = onRetry) {
                Text("Retry")
            }
            OutlinedButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
    }
}

@Composable
fun RetryDialog(
    onRetry: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text("Request Timeout") },
        text = { Text("No response received. Would you like to try again?") },
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text("Retry")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        },
    )
}
