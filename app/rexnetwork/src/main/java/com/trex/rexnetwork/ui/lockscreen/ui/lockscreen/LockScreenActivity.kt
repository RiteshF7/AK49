package com.trex.rexnetwork.ui.lockscreen.ui.lockscreen

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

class LockScreenActivity : ComponentActivity() {
    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?,
    ) {
        super.onCreate(savedInstanceState, persistentState)

        setContent {
            MaterialTheme(colorScheme = LockScreenColors) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    LockScreen(
                        deviceName = "Phone Locked!",
                        lockReason = "This device has been locked due to payment default. Please make the necessary payment to unlock the device.",
                        onGetUnlockCode = {},
                    )
                }
            }
        }
    }
}
