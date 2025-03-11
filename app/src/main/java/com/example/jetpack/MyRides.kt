package com.example.jetpack

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import com.example.jetpack.ui.theme.YellowJC

// Sample Ride Data
data class Ride(val source: String, val destination: String, val date: String)

// Ride History Screen
@Composable
fun Ride() {
    val context = LocalContext.current

    val rideList = listOf(
        Ride("New York", "Los Angeles", "12 Feb 2025"),
        Ride("Chicago", "San Francisco", "10 Jan 2025"),
        Ride("Boston", "Miami", "5 Dec 2024"),
        Ride("Dallas", "Houston", "20 Nov 2024"),
        Ride("Seattle", "Denver", "1 Oct 2024")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))

        Text(text = "Ride History", fontSize = 30.sp, color = YellowJC)

        Spacer(modifier = Modifier.height(16.dp))

        // Ride Table (Scrollable)
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(rideList) { ride ->
                RideRow(ride) {
                    Toast.makeText(context, "Booking ${ride.source} to ${ride.destination} again!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

// Table Row UI
@Composable
fun RideRow(ride: Ride, onBookAgain: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "From: ${ride.source}", color = Color.White, fontSize = 16.sp)
                Text(text = "To: ${ride.destination}", color = Color.White, fontSize = 16.sp)
                Text(text = "Date: ${ride.date}", color = Color.Gray, fontSize = 14.sp)
            }

            Button(
                onClick = onBookAgain,
                colors = ButtonDefaults.buttonColors(containerColor = YellowJC)
            ) {
                Text(text = "Book Again", color = Color.Black)
            }
        }
    }
}

