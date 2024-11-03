package com.trex.rexnetwork.domain.firebasecore.fcm.fcmrequestscreen

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trex.rexnetwork.Constants
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.utils.getExtraData
import kotlinx.parcelize.Parcelize

@Composable
fun ResultScreen(
    isSuccess: Boolean,
    message: String,
    onFinish: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Icon based on result
            Icon(
                painter =
                    painterResource(
                        id =
                            if (isSuccess) {
                                android.R.drawable.ic_dialog_info // Replace with your success icon
                            } else {
                                android.R.drawable.ic_dialog_alert // Replace with your failure icon
                            },
                    ),
                contentDescription = if (isSuccess) "Success" else "Failure",
                modifier = Modifier.size(80.dp),
                tint =
                    if (isSuccess) {
                        Color(0xFF4CAF50) // Success Green
                    } else {
                        Color(0xFFE53935) // Error Red
                    },
            )

            // Status Text
            Text(
                text = if (isSuccess) "Success!" else "Oops!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            // Message
            Text(
                text = message,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 32.dp),
            )

            // Finish Button
            Button(
                onClick = onFinish,
                modifier =
                    Modifier
                        .padding(top = 32.dp)
                        .fillMaxWidth()
                        .height(48.dp),
            ) {
                Text("Finish")
            }
        }
    }
}

@Parcelize
data class ActionResultStatus(
    val isSuccess: Boolean,
    val message: String,
) : Parcelable

// Example Usage in Activity
class FcmResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get parameters from intent
        val result = intent.getExtraData<ActionMessageDTO>()
        val isSuccess =
            result.payload[Constants.KEY_RESPOSE_RESULT_STATUS] == Constants.RESPONSE_RESULT_SUCCESS
        val message: String =
            result.payload[result.action.name] ?: "Action completed successfully!"

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    ResultScreen(
                        isSuccess = isSuccess,
                        message = message,
                        onFinish = { finish() },
                    )
                }
            }
        }
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun SuccessScreenPreview() {
    MaterialTheme {
        ResultScreen(
            isSuccess = true,
            message = "Your operation completed successfully!",
            onFinish = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FailureScreenPreview() {
    MaterialTheme {
        ResultScreen(
            isSuccess = false,
            message = "Something went wrong. Please try again.",
            onFinish = {},
        )
    }
}
