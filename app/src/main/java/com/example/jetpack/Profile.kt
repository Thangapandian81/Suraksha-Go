package com.example.jetpack

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack.ui.theme.YellowJC
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun ProfileScreen(context: Context) {
    val sharedPreferences = remember { context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE) }
    val userId = sharedPreferences.getString("user_id",null)

    if (userId == null) {
        // Navigate to LoginActivity if user_id is null
        LaunchedEffect(Unit) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    } else {
        var userDetails by remember { mutableStateOf<User?>(null) }
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(userId) {
            userDetails = fetchUserDetails(userId)
            isLoading = false
        }

        if (isLoading) {
            LoadingScreen()
        } else {
            userDetails?.let { Profile(it) }
        }
    }
}

// Function to Fetch User Details from Firestore
suspend fun fetchUserDetails(userId:String): User? {
    return try {
        val db = FirebaseFirestore.getInstance()
        val document = db.collection("users").document(userId).get().await()
        if (document.exists()) {
            val data = document.data ?: return null

            val emergencyContact = data["emergencyContact"] as? Map<*, *>
            val emergencyEmail = emergencyContact?.get("email") as? String ?: "N/A"
            val emergencyRelation = emergencyContact?.get("relation") as? String ?: "N/A"

            User(
                name = data["name"] as? String ?: "N/A",
                email = data["email"] as? String ?: "N/A",
                address = data["address"] as? String ?: "N/A",
                phone = data["phone"] as? String ?: "N/A",
                pincode = data["pincode"] as? String ?: "N/A",
                emergencyEmail = emergencyEmail,
                emergencyRelation = emergencyRelation
            )
        } else {
            null
        }
    } catch (e: Exception) {
        Log.e("Profile", "Error fetching user details", e)
        null
    }
}

// Profile Composable
@Composable
fun Profile(user: User) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            Text(text = "Profile", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = YellowJC)

            Spacer(modifier = Modifier.height(16.dp))

            // User Image
            Image(
                painter = painterResource(id = R.drawable.profileboy), // Replace with actual image
                contentDescription = "User Image",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, YellowJC, CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // User Details
            Text(text = user.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = YellowJC)
            Text(text = user.email, fontSize = 16.sp, color = Color.White)

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                ProfileDetailItem("Address", user.address)
                ProfileDetailItem("Phone No", user.phone)
                ProfileDetailItem("Pin Code", user.pincode)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Emergency Contact Section
            Text(text = "Emergency Contact", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = YellowJC)

            Spacer(modifier = Modifier.height(12.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                ProfileDetailItem("Email Id", user.emergencyEmail)
                ProfileDetailItem("Relation", user.emergencyRelation)
            }
        }
    }
}

// Reusable Profile Detail Item
@Composable
fun ProfileDetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = YellowJC)
        Text(text = value, fontSize = 16.sp, color = Color.White)
    }
}

// Loading Screen
@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = YellowJC)
    }
}

// User Data Class
data class User(
    val name: String = "",
    val email: String = "",
    val address: String = "",
    val phone: String = "",
    val pincode: String = "",
    val emergencyEmail: String = "",
    val emergencyRelation: String = ""
)
