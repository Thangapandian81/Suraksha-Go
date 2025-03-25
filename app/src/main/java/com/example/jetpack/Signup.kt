package com.example.jetpack

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack.ui.theme.YellowJC
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.ui.text.input.VisualTransformation
import com.example.jetpack.ui.theme.JetPackTheme


class SignupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    Signup()
                }
            }
        }
    }
}

@Composable
fun Signup() {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var emergencyEmail by remember { mutableStateOf("") }
    var relation by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sign Up", color = YellowJC, fontSize = 24.sp,fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))

        @Composable
        fun inputField(value: String, onChange: (String) -> Unit, label: String, keyboardType: KeyboardType = KeyboardType.Text, isPassword: Boolean = false) {
            OutlinedTextField(
                value = value,
                onValueChange = onChange,
                label = { Text(label, color = YellowJC) },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = YellowJC,
                    unfocusedBorderColor = YellowJC
                )
            )
        }

        inputField(name, { name = it }, "Full Name")
        inputField(email, { email = it }, "Email", KeyboardType.Email)
        inputField(phone, { phone = it }, "Phone Number", KeyboardType.Phone)
        inputField(address, { address = it }, "Address")
        inputField(pincode, { pincode = it }, "Pincode", KeyboardType.Number)

        inputField(password, {
            password = it
            passwordError = password != confirmPassword && confirmPassword.isNotEmpty()
        }, "Password", KeyboardType.Password, true)

        inputField(confirmPassword, {
            confirmPassword = it
            passwordError = password != confirmPassword
        }, "Confirm Password", KeyboardType.Password, true)

        if (passwordError) {
            Text("Passwords do not match", color = Color.Red, fontSize = 12.sp)
        }

        Text("Emergency Contact", color = YellowJC, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
        inputField(emergencyEmail, { emergencyEmail = it }, "Emergency Contact Email", KeyboardType.Email)
        inputField(relation, { relation = it }, "Relation (e.g., Father, Friend)")

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() &&
                        confirmPassword.isNotEmpty() && phone.isNotEmpty() &&
                        address.isNotEmpty() && pincode.isNotEmpty() &&
                        emergencyEmail.isNotEmpty() && relation.isNotEmpty()
                    ) {
                        if (password == confirmPassword) {
                            isLoading = true

                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val userId = auth.currentUser?.uid
                                        if (userId != null) {
                                            fetchLastUserId(db) { lastUserId ->
                                                val newUserId = lastUserId + 1
                                                val userMap = hashMapOf(
                                                    "id" to newUserId,
                                                    "name" to name,
                                                    "email" to email,
                                                    "phone" to phone,
                                                    "address" to address,
                                                    "pincode" to pincode,
                                                    "emergencyContact" to mapOf(
                                                        "email" to emergencyEmail,
                                                        "relation" to relation
                                                    )
                                                )

                                                db.collection("users").document(userId.toString()).set(userMap)
                                                    .addOnSuccessListener {
                                                        alertMessage = "Signup Successful!"
                                                        showDialog = true
                                                    }
                                                    .addOnFailureListener {
                                                        alertMessage = "Error saving data"
                                                        showDialog = true
                                                    }
                                                    .addOnCompleteListener {
                                                        isLoading = false
                                                    }
                                            }
                                        }
                                    } else {
                                        alertMessage = "Signup Failed"
                                        showDialog = true
                                        isLoading = false
                                    }
                                }
                        }
                    } else {
                        alertMessage = "Please fill all fields"
                        showDialog = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = YellowJC),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up" , color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Already have an account? Login",
            color = YellowJC,
            modifier = Modifier.clickable {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            }
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    if (alertMessage == "Signup Successful!") {
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                    }
                }) {
                    Text("OK")
                }
            },
            title = { Text("Signup Status") },
            text = { Text(alertMessage) }
        )
    }
}

/**
 * Fetches the last user ID from Firestore and increments it.
 */
fun fetchLastUserId(db: FirebaseFirestore, onComplete: (Int) -> Unit) {
    db.collection("users")
        .orderBy("id", com.google.firebase.firestore.Query.Direction.DESCENDING)
        .limit(1)
        .get()
        .addOnSuccessListener { documents ->
            val lastId = if (!documents.isEmpty) {
                documents.documents[0].getLong("id")?.toInt() ?: 0
            } else {
                0
            }
            onComplete(lastId)
        }
        .addOnFailureListener {
            onComplete(0)
        }
}
