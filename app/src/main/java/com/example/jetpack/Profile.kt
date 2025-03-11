package com.example.jetpack

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack.ui.theme.YellowJC
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background

@Composable
fun Profile() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            // User Image
            Image(
                painter = painterResource(id =R.drawable.profileboy), // Replace with actual image
                contentDescription = "User Image",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, YellowJC, CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // User Details
            Text(text = "John Doe", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = YellowJC)
            Text(text = "johndoe@example.com", fontSize = 16.sp, color = Color.White)

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                ProfileDetailItem("Address", "123, Street Name, City, Country")
                ProfileDetailItem("Phone No", "+91 9876543210")
                ProfileDetailItem("Pin Code", "560001")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Emergency Contact Section
            Text(text = "Emergency Contact", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = YellowJC)

            Spacer(modifier = Modifier.height(12.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                ProfileDetailItem("Phone No", "+91 9876543211")
                ProfileDetailItem("Relation", "Brother")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Update Button
//            Button(
//                onClick = { /* Handle Update Action */ },
//                colors = ButtonDefaults.buttonColors(containerColor = YellowJC)
//            ) {
//                Text(text = "Update", color = Color.White, fontSize = 18.sp)
//            }
        }
    }
}



// Reusable Profile Detail Item
@Composable
fun ProfileDetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = YellowJC)
        Text(text = value, fontSize = 16.sp, color =Color.White)
    }
}

