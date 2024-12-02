package com.trex.rexnetwork

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun preview() {
    ActionGrid()
}

@Composable
fun ActionGrid() {
    val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5)
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(list) { item ->
            ActionCard()
        }
    }
}

@Composable
fun ActionCard() {
    Card(
        modifier =
            Modifier
                .padding(6.dp),
        shape = RoundedCornerShape(6.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.1f),
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .padding(20.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            RoundShadowIcon(Icons.Rounded.Add)
            Spacer(Modifier.padding(5.dp))
            Text(
                textAlign = TextAlign.Center,
                text = "Add customer",
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                softWrap = true,
                color = Color.White,
            )
        }
    }
}

@Composable
fun RoundShadowIcon(icon: ImageVector) {
    val mPrimaryColor = colorResource(R.color.primary)
    Card(
        shape = RoundedCornerShape(60.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = mPrimaryColor.copy(alpha = 0.1f),
            ),
    ) {
        Icon(
            modifier =
                Modifier
                    .height(40.dp)
                    .width(40.dp),
            imageVector = icon,
            tint = mPrimaryColor,
            contentDescription = "",
        )
    }
}
