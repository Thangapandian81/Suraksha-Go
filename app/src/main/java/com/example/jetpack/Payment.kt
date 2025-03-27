package com.example.jetpack

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack.ui.theme.YellowJC
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// ✅ Data Model for Ride
data class RideInfo(  // ✅ Changed from Ride to RideInfo
    val rideId: String,
    val amount: String,
    val date: String,
    val source: String,
    val destination: String
)

// ✅ Rides Screen - Fetches & Displays Paid Rides for Logged-in User
@Composable
fun Payment() {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid // Get the currently logged-in user ID

    var rideList by remember { mutableStateOf<List<RideInfo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) } // Loading state

    // ✅ Fetch Rides from Firestore
    LaunchedEffect(userId) {
        if (userId != null) {
            db.collection("rides")
                .whereEqualTo("userId", userId) // Filter for only this user's rides
                .whereEqualTo("paymentStatus", true) // Fetch only paid rides
                .get()
                .addOnSuccessListener { result ->
                    val rides = result.documents.mapNotNull { doc ->
                        val rideId = doc.getString("rideId") ?: "Unknown"
                        val amount = doc.getDouble("totalFare")?.let { "Rs.%.2f".format(it) } ?: "Rs.0.00"
                        val date = doc.getString("date") ?: "Unknown"
                        val source = doc.getString("source") ?: "Unknown"
                        val destination = doc.getString("destination") ?: "Unknown"

                        RideInfo(rideId, amount, date, source, destination)
                    }
                    rideList = rides
                    isLoading = false
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error fetching rides", e)
                    isLoading = false
                }
        }
    }

    // ✅ UI Layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text(text = "Payment History", fontSize = 30.sp, color = YellowJC)

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Show Loading Indicator
        if (isLoading) {
            CircularProgressIndicator(color = YellowJC)
        }
        // ✅ Show Message if No Rides Found
        else if (rideList.isEmpty()) {
            Text(text = "No Paid Rides Found", color = Color.Gray, fontSize = 18.sp)
        }
        // ✅ Display Ride List
        else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(rideList) { ride ->
                    RideRow(ride)
                }
            }
        }
    }
}

// ✅ Table Row UI
@Composable
fun RideRow(ride: RideInfo){
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Ride ID: ${ride.rideId}", color = Color.White, fontSize = 16.sp)
            Text(text = "Amount: ${ride.amount}", color = Color.White, fontSize = 16.sp)
            Text(text = "Date: ${ride.date}", color = Color.Gray, fontSize = 14.sp)
            Text(text = "From: ${ride.source}", color = Color.LightGray, fontSize = 14.sp)
            Text(text = "To: ${ride.destination}", color = Color.LightGray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
            ) {
                Text(text = "Paid", color = Color.Black)
            }
        }
    }
}
