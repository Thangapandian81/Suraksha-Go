package com.example.jetpack

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack.ui.theme.YellowJC
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Rating() {
    val context = LocalContext.current
    var appRating by remember { mutableStateOf(0) }
    var rideRating by remember { mutableStateOf(0) }
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid // Get current user ID

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text(text = "Rate Us", fontSize = 26.sp, color = YellowJC)

        Spacer(modifier = Modifier.height(24.dp))

        // App Rating
        Text(text = "Rate the App", fontSize = 20.sp, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        StarRating(rating = appRating) { appRating = it }

        Spacer(modifier = Modifier.height(24.dp))

        // Ride Rating
        Text(text = "Rate Your Ride", fontSize = 20.sp, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        StarRating(rating = rideRating) { rideRating = it }

        Spacer(modifier = Modifier.height(32.dp))

        // Submit Button
        Button(
            onClick = {
                if (userId != null) {
                    val ratingData = hashMapOf(
                        "userId" to userId,
                        "appRating" to appRating,
                        "rideRating" to rideRating,
                        "timestamp" to System.currentTimeMillis()
                    )

                    db.collection("ratings").document(userId)
                        .set(ratingData)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Thank you for your feedback!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed to submit rating", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(context, "User not logged in!", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = YellowJC),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Submit", fontSize = 18.sp, color = Color.Black)
        }
    }
}

// ⭐ Star Rating Component
@Composable
fun StarRating(rating: Int, onRatingChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            IconButton(onClick = { onRatingChange(i) }) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Star $i",
                    tint = if (i <= rating) YellowJC else Color.Gray,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}
