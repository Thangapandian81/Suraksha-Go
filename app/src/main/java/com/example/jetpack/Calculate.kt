package com.example.jetpack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class Calculate : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate)

        val distanceInKm = intent.getDoubleExtra("DISTANCE_KM", 0.0)
        val totalFare = intent.getDoubleExtra("TOTAL_FARE", 0.0)
        val estimatedTime = intent.getStringExtra("ESTIMATED_TIME") ?: "N/A"
        val source = intent.getStringExtra("Source") ?: "Unknown"
        val desti = intent.getStringExtra("desti") ?: "Unknown"
        val userId = auth.currentUser?.uid ?: "Guest"

        // UI references
        val distanceValue = findViewById<TextView>(R.id.distance_value)
        val timeValue = findViewById<TextView>(R.id.time_value)
        val fareValue = findViewById<TextView>(R.id.fare_value)
        val payButton = findViewById<Button>(R.id.online_payment_button)

        // Set data in TextViews
        distanceValue.text = "%.2f km".format(distanceInKm)
        timeValue.text = estimatedTime
        fareValue.text = "₹%.2f".format(totalFare)

        // Generate unique ride ID
        val rideId = db.collection("rides").document().id

        // Get current date
        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        // Ride data
        val rideData = hashMapOf(
            "rideId" to rideId,
            "userId" to userId,
            "distanceInKm" to distanceInKm,
            "totalFare" to totalFare,
            "estimatedTime" to estimatedTime,
            "source" to source,
            "destination" to desti,
            "paymentStatus" to false, // Default payment status: false
            "date" to currentDate
        )

        // Store data in Firestore
        db.collection("rides").document(rideId)
            .set(rideData)
            .addOnSuccessListener {
                Toast.makeText(this, "Ride details stored successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to store ride details!", Toast.LENGTH_SHORT).show()
            }

        // Handle payment button click
        payButton.setOnClickListener {
            val intent = Intent(this, OnlinePayment::class.java)
            intent.putExtra("TOTAL_AMOUNT", totalFare)
            intent.putExtra("RIDE_ID", rideId) // Pass ride ID for updating payment status later
            startActivity(intent)
        }
    }
}
