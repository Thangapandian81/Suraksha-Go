package com.example.jetpack

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Star
import com.example.jetpack.ui.theme.YellowJC

@Composable
fun Insurance() {
//    val context = LocalContext.current
    val insuranceProviders = remember {
        listOf("Ackro","PolicyBazaar", "HDFC Ergo", "Tata AIG", "ICICILombard")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = "Ride Insurance",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = YellowJC
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Insurance Information
        Card(
            modifier = Modifier.fillMaxWidth().background(Color.Black),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.background(Color.Black).padding(16.dp)) {

                Text(
                    text = "Why Insurance?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = YellowJC
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Insurance protects you during your ride, ensuring financial security.",
                    fontSize = 16.sp,
                    color = Color.LightGray
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // List of Insurance Providers
        Text(
            text = "Insurance Providers",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = YellowJC
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)  // Prevents layout crash
        ) {
            items(insuranceProviders.size) { index ->
                InsuranceCard(insuranceProviders[index])
            }
        }
    }
}

@Composable
fun InsuranceCard(providerName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 8.dp)
            .clickable { /* Handle Click */ },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color.DarkGray)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
//                painter = painterResource(id = Icons.Filled.Star),
                imageVector= Filled.Security,// Use a valid drawable
                contentDescription = "Insurance Logo",
                modifier = Modifier.size(40.dp),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = providerName,
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


