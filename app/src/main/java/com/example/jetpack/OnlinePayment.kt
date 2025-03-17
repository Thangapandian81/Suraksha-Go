package com.example.jetpack


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class OnlinePayment : ComponentActivity(), PaymentResultListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val totalAmountDouble = intent.getDoubleExtra("TOTAL_AMOUNT", 0.0)
        val totalAmountInt = totalAmountDouble.toInt()  // Convert to Int for Razorpay (INR to paisa)

        setContent {
            OnlinePaymentScreen(totalAmountInt)
        }
    }

    @Composable
    fun OnlinePaymentScreen(totalAmount: Int) {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            startPayment(totalAmount, coroutineScope, context)
        }

        Scaffold { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

    private fun startPayment(
        amount: Int,
        coroutineScope: CoroutineScope,
        context: Context
    ) {
        val activity: Activity = context as Activity
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_WfSM1DjLjw38FI") // Your Razorpay test/live key

        try {
            val options = JSONObject().apply {
                put("name", "Laundryes")
                put("description", "Laundry Service Payment")
                put("currency", "INR")
                put("amount", amount * 100)  // Convert INR to paisa
                put("prefill.email", "laundryes@gmail.com")
                put("prefill.contact", "9626825027")
                put("image", "https://raw.githubusercontent.com/Sri-10ran/LaundryesApp/refs/heads/master/app/src/main/res/drawable/logo.webp")
            }

            checkout.open(activity, options)

        } catch (e: Exception) {
            coroutineScope.launch {
                Toast.makeText(context, "Payment Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        Toast.makeText(this, "Payment Successful: $razorpayPaymentID", Toast.LENGTH_LONG).show()

        // Redirect to MainActivity
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    override fun onPaymentError(code: Int, response: String?) {
        Toast.makeText(this, "Payment Failed: $response", Toast.LENGTH_LONG).show()
        finish()
    }
}
