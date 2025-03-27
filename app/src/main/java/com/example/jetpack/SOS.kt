package com.example.jetpack

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.AsyncTask
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.location.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import java.util.Properties

@Composable
fun SOS() {
    val context = LocalContext.current
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var emergencyEmail by remember { mutableStateOf<String?>(null) }
    var emergencyPhone by remember { mutableStateOf<String?>(null) }
    var location by remember { mutableStateOf<Location?>(null) }

    // Fetch Emergency Email from Firestore
    LaunchedEffect(userId) {
        userId?.let { uid ->
            FirebaseFirestore.getInstance().collection("users").document(uid)
                .get()
                .addOnSuccessListener { document ->
                    val emergencyContact = document.get("emergencyContact") as? Map<*, *>
                    emergencyEmail = emergencyContact?.get("email") as? String
                    emergencyPhone = emergencyContact?.get("whatsapp")as? String
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to fetch emergency email", Toast.LENGTH_SHORT).show()
                }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // SOS Button
        Button(
            onClick = {
                getLocation(context) { lat, lon ->
                    if (lat != null && lon != null) {
                        location = Location("").apply {
                            latitude = lat
                            longitude = lon
                        }
                        if (emergencyEmail != null && emergencyPhone != null) {
                            SendEmailTask(context, emergencyEmail!!, emergencyPhone!!, lat, lon).execute()
                        } else {
                            Toast.makeText(context, "Emergency contact details missing", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else {
                        Toast.makeText(context, "Unable to get location", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            shape = CircleShape,
            modifier = Modifier.size(160.dp)
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

// Function to Get Live GPS Location
@SuppressLint("MissingPermission")
fun getLocation(context: Context, callback: (Double?, Double?) -> Unit) {
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationProviderClient.lastLocation
        .addOnSuccessListener { location ->
            if (location != null) {
                callback(location.latitude, location.longitude)
            } else {
                callback(null, null)
            }
        }
        .addOnFailureListener {
            callback(null, null)
        }
}

// AsyncTask to Send Email using SMTP
class SendEmailTask(
    private val context: Context,
    private val recipientEmail: String,
    private val emergencyPhone: String,
    private val latitude: Double,
    private val longitude: Double
) : AsyncTask<Void, Void, Boolean>() {

    override fun doInBackground(vararg params: Void?): Boolean {
        val senderEmail = "ethangapandian01@gmail.com" // Replace with your email
        val senderPassword = "lftedjenkuekvqqv" // Use an App Password

        val properties = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587")
        }

        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(senderEmail, senderPassword)
            }
        })

        return try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(senderEmail))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail))
                subject = "🚨 SOS Alert - Help Needed!"
                setText("Emergency! Please help! \n\nLive Location: https://maps.google.com/?q=$latitude,$longitude")

                // Set High Importance (Might trigger sound notifications in some email clients)
                addHeader("X-Priority", "1")   // High priority
                addHeader("Importance", "high")
                addHeader("X-MSMail-Priority", "High") // For Outlook
            }
            Transport.send(message)
            true
        } catch (e: MessagingException) {
            e.printStackTrace()
            false
        }
    }

    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        if (result) {
            Toast.makeText(context, "✅ SOS Email Sent Successfully!", Toast.LENGTH_LONG).show()
            val phoneNumber = "+91$emergencyPhone"
            val message = "🚨 SOS ALERT! Help Needed. Live Location: https://maps.google.com/?q=$latitude,$longitude"
            val whatsappUrl = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}"

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(whatsappUrl)
            intent.setPackage("com.whatsapp") // Open directly in WhatsApp

            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "❌ Failed to Send SOS Email!", Toast.LENGTH_LONG).show()
        }
    }
}
