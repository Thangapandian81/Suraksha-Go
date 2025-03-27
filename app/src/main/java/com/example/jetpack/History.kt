package com.example.jetpack

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack.ui.theme.YellowJC
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Ride Data Model
data class Ride(
    val rideId: String,
    val userId: String,
    val source: String,
    val destination: String,
    val date: String
)

// Ride History Screen
@Composable
fun History() {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid // Get logged-in user ID

    var rideList by remember { mutableStateOf<List<Ride>>(emptyList()) }

    // Fetch Rides from Firestore
    LaunchedEffect(userId) {
        userId?.let { uid ->
            db.collection("rides")
                .get()
                .addOnSuccessListener { result ->
                    val rides = result.documents.mapNotNull { doc ->
                        val rideUserId = doc.getString("userId") ?: ""
                        if (rideUserId == uid) { // Filter only logged-in user's rides
                            val rideId = doc.getString("rideId") ?: "Unknown"
                            val source = doc.getString("source") ?: "Unknown"
                            val destination = doc.getString("destination") ?: "Unknown"
                            val date = doc.getString("date") ?: "Unknown"

                            Ride(rideId, rideUserId, source, destination, date)
                        } else null
                    }
                    rideList = rides
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error fetching rides", e)
                }
        }
    }

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

        // Show message if no rides found
        if (rideList.isEmpty()) {
            Text(text = "No Ride History Found", color = Color.Gray, fontSize = 18.sp)
        } else {
            // Display rides in a list
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(rideList) { ride ->
                    RideRow(ride) {
                        Toast.makeText(
                            context,
                            "Booking ${ride.source} to ${ride.destination} again!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}

// Ride List Item
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

//            Button(
//                onClick = onBookAgain,
//                colors = ButtonDefaults.buttonColors(containerColor = YellowJC)
//            ) {
//                Text(text = "Book Again", color = Color.Black)
//            }
        }
    }
}
