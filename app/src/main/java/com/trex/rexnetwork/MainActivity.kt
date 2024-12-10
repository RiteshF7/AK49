package com.trex.rexnetwork

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.trex.rexnetwork.domain.firebasecore.firesstore.MasterCodeFirestore
import com.trex.rexnetwork.ui.theme.RexNetworkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RexNetworkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier =
                            Modifier
                                .padding(innerPadding)
                                .background(color = Color.Black.copy(alpha = 0.85f)),
                    ) {
                        Box(modifier = Modifier.padding(10.dp)) {
                            ActionGrid()
                        }
                    }
                }
            }
        }
    }
}


