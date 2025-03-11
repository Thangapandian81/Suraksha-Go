package com.example.jetpack

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SOS() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // SOS Image
//        Image(
//            painter = painterResource(id = R.drawable.sos), // Replace with your SOS image
//            contentDescription = "SOS Alert",
//            modifier = Modifier.size(150.dp)
//        )

        // SOS Button
        Button(
            onClick = {
                Toast.makeText(context, "⚠️ SOS Alert Sent!", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            shape = CircleShape,
            modifier = Modifier
                .size(160.dp) // Circular button
        ) {
            Text(text = "SOS", fontSize = 24.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.width(40.dp))

        Text(
            text = "Click Here to Send Alert!",
            fontSize = 18.sp,
            color = Color.Red
        )
    }
}
