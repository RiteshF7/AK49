package com.trex.rexnetwork.domain.firebasecore.fcm.fcmrequestscreen

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
    modifier: Modifier = Modifier,
) {
    // Animation for the icon pulse effect
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse,
            ),
        label = "",
    )

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f)),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Status Icon with pulse animation
            Box(
                modifier =
                    Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Warning,
                    contentDescription = if (isSuccess) "Success Icon" else "Error Icon",
                    modifier = Modifier.size(64.dp),
                    tint = if (isSuccess) Color(0xFF4CAF50) else Color(0xFFE53935),
                )
            }

            // Status Text
            Text(
                text = if (isSuccess) "Success!" else "Oops!",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp),
            )

            // Message Card
            Surface(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.1f),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = if (isSuccess) "OPERATION COMPLETED" else "OPERATION FAILED",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = message,
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )
                }
            }

            // Finish Button
            Button(
                onClick = onFinish,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(56.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                    ),
                shape = RoundedCornerShape(28.dp),
            ) {
                Text(
                    text = "FINISH",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                )
            }
        }
    }
}

@Preview
@Composable
fun ResultScreenPreview() {
    MaterialTheme(
        colorScheme = colors,
    ) {
        ResultScreen(
            isSuccess = true,
            message = "Your operation has been completed successfully. You can now proceed.",
            onFinish = {},
        )
    }
}

// Theme color definitions (if needed)
val colors =
    lightColorScheme(
        primary = Color(0xFF00DC7F),
        secondary = Color(0xFF03A9F4),
        tertiary = Color(0xFF00BCD4),
        background = Color.Black,
        surface = Color.Black,
        error = Color(0xFFB00020),
    )

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
            MaterialTheme(colorScheme = colors) {
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
