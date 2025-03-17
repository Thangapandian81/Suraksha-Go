package com.example.jetpack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Calculate : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate)

        val distanceInKm = intent.getDoubleExtra("DISTANCE_KM", 0.0)
        val totalFare = intent.getDoubleExtra("TOTAL_FARE", 0.0)
        val estimatedTime = intent.getStringExtra("ESTIMATED_TIME") ?: "N/A"

        // UI references
        val distanceValue = findViewById<TextView>(R.id.distance_value)
        val timeValue = findViewById<TextView>(R.id.time_value)
        val fareValue = findViewById<TextView>(R.id.fare_value)
        val payButton = findViewById<Button>(R.id.online_payment_button)

        // Set data in TextViews
        distanceValue.text = "%.2f km".format(distanceInKm)
        timeValue.text = estimatedTime
        fareValue.text = "₹%.2f".format(totalFare)

        payButton.setOnClickListener {
            val intent = Intent(this, OnlinePayment::class.java)
            intent.putExtra("TOTAL_AMOUNT", totalFare)
            startActivity(intent)
        }
    }
}
