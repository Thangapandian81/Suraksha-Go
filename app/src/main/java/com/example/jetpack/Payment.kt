package com.example.jetpack

import android.widget.Toast
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
import com.example.jetpack.ui.theme.YellowJC

// Sample Payment Data
data class Payment(val rideId: String, val amount: String, val date: String, val status: String)

// Payment History Screen
@Composable
fun Payment() {
    val context = LocalContext.current

    val paymentList = listOf(
        Payment("#1234", "Rs.50.00", "12 Feb 2025", "Paid"),
        Payment("#5678", "Rs.75.00", "10 Jan 2025", "Pending"),
        Payment("#9101", "Rs.40.00", "5 Dec 2024", "Failed"),
        Payment("#1121", "Rs.60.00", "20 Nov 2024", "Paid"),
        Payment("#3141", "Rs.85.00", "1 Oct 2024", "Paid")
    )

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

        // Payment Table (Scrollable)
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(paymentList) { payment ->
                PaymentRow(payment) {
                    Toast.makeText(context, "Viewing payment details for ${payment.rideId}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

// Table Row UI
@Composable
fun PaymentRow(payment: Payment, onViewDetails: () -> Unit) {
    val statusColor = when (payment.status) {
        "Paid" -> Color.Green
        "Pending" -> YellowJC
        "Failed" -> Color.Red
        else -> Color.Gray
    }

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
                Text(text = "Ride ID: ${payment.rideId}", color = Color.White, fontSize = 16.sp)
                Text(text = "Amount: ${payment.amount}", color = Color.White, fontSize = 16.sp)
                Text(text = "Date: ${payment.date}", color = Color.Gray, fontSize = 14.sp)
            }

            Button(
                onClick = onViewDetails,
                colors = ButtonDefaults.buttonColors(containerColor = statusColor)
            ) {
                Text(text = payment.status, color = Color.Black)
            }
        }
    }
}
