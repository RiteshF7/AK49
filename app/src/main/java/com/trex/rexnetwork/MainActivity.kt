package com.trex.rexnetwork

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.domain.repositories.SendActionMessageRepository
import com.trex.rexnetwork.ui.theme.RexNetworkTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RexNetworkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        Button(onClick = { send() }) {

                        }
                    }
                }
            }
        }
    }

    fun send() {


        val messageDTO =
            ActionMessageDTO(
                "some token",
                Actions.ACTION_UNLOCK_DEVICE,
                mapOf("some" to "some")
            )

        GlobalScope.launch {
            SendActionMessageRepository().sendActionMessage(messageDTO)
        }
    }
}
