package com.example.jetpack

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.jetpack.ui.theme.JetPackTheme
import com.example.jetpack.ui.theme.YellowJC
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        // Read SharedPreferences BEFORE Jetpack Compose UI loads
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", null)

        // If user is already logged in, redirect to MainActivity and finish LoginActivity
        if (userId != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish() // Prevents going back to LoginActivity
            return
        }

        // Load Login UI if userId is null
        setContent {
            JetPackTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    LoginScreen()
                }
            }
        }
    }
}


@Composable
fun LoginScreen() {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo at the top
        Image(
            painter = painterResource(id = R.drawable.logo), // Replace with your actual logo resource
            contentDescription = "App Logo",
            modifier = Modifier
                .size(230.dp)
        )

        Text("Login",color= YellowJC,fontSize = 24.sp,fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email", color = YellowJC) }, modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = YellowJC,
                unfocusedBorderColor = YellowJC
            )
        )

        OutlinedTextField(
            value = password,onValueChange = { password = it },
            label = { Text("Password", color = YellowJC) }, modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = YellowJC,
                unfocusedBorderColor = YellowJC
            ),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    isLoading = true

                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userId = auth.currentUser?.uid
                                if (userId != null) {
                                    db.collection("users").document(userId).get()
                                        .addOnSuccessListener { document ->
                                            val storedUserId = document.getLong("id")?.toInt()
                                            if (storedUserId != null) {
                                                // Store user_id in SharedPreferences
                                                sharedPreferences.edit().putString("user_id",userId).apply()
                                                sharedPreferences.edit().putInt("id",storedUserId).apply()
                                                sharedPreferences.edit().putString("profile_name",document.getString("name").toString()).apply()
                                                sharedPreferences.edit().putString("profile_email",document.getString("email").toString()).apply()

                                                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                                                    val intent = Intent(context, MainActivity::class.java)
                                                    context.startActivity(intent)
                                            } else {
                                                Toast.makeText(context, "User ID not found", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "Failed to fetch user ID", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            } else {
                                Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                            }
                            isLoading = false
                        }
                },
                colors = ButtonDefaults.buttonColors(containerColor = YellowJC),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // "Don't have an account? Sign up" link
        Row {
            Text("Don't have an account? ", color = YellowJC)
            Text(
                text = "Sign up",
                color = YellowJC,
                modifier = Modifier
                    .clickable {
                    val intent = Intent(context, SignupActivity::class.java)
                    context.startActivity(intent)
                }
            )
        }
    }
}

