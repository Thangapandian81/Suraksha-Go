package com.example.jetpack

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.Color.Companion
import com.example.jetpack.ui.theme.YellowJC

@Composable
fun Settings() {
    val context = LocalContext.current
    val appVersion = getAppVersion(context)
    val isDarkMode = remember { mutableStateOf(false) }
    val isNotificationsEnabled = remember { mutableStateOf(true) }
    val selectedLanguage = remember { mutableStateOf("English") }

    val languages = listOf("English")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // Background color
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text(text = "Settings", fontSize = 30.sp, color = YellowJC, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // App Version
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Info, contentDescription = "App Version", tint = YellowJC)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "App Version: $appVersion", fontSize = 18.sp, color = Color.White)
        }

        Divider(color = Color.Gray)

        // Theme Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isDarkMode.value = !isDarkMode.value }
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Brightness4, contentDescription = "Theme", tint = YellowJC)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Dark Mode", fontSize = 18.sp, color = Color.White)
            Spacer(modifier = Modifier.weight(1f))
        }

        Divider(color = Color.Gray)

        // Notifications Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isNotificationsEnabled.value = !isNotificationsEnabled.value }
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = YellowJC)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Enable Notifications", fontSize = 18.sp, color = Color.White)
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = isNotificationsEnabled.value, onCheckedChange = { isNotificationsEnabled.value = it })
        }

        Divider(color = Color.Gray)

        // Language Selection
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Language, contentDescription = "Language", tint = YellowJC)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Language", fontSize = 18.sp, color = Color.White)
            Spacer(modifier = Modifier.weight(1f))
            DropdownMenuBox(selectedLanguage, languages)
        }

        Divider(color = Color.Gray)

        // Permissions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { openAppSettings(context) }
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Lock, contentDescription = "Permissions", tint = YellowJC)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Manage Permissions", fontSize = 18.sp, color = Color.White)
        }

        Divider(color = Color.Gray)

        // Logout
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable {
//                    Toast
//                        .makeText(context, "Logging out...", Toast.LENGTH_SHORT)
//                        .show()
//                }
//                .padding(vertical = 10.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.Red)
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(text = "Logout", fontSize = 18.sp, color = Color.Red)
//        }
    }
}

// Function to Get App Version
fun getAppVersion(context: Context): String {
    return try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName ?: "1.0"
    } catch (e: Exception) {
        "Unknown"
    }
}

// Function to Open App Permissions
fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.parse("package:${context.packageName}")
    }
    context.startActivity(intent)
}

// Language Selection Dropdown
@Composable
fun DropdownMenuBox(selectedLanguage: MutableState<String>, languages: List<String>) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .background(Color.Black)
        .border(1.dp, Color.Yellow, RoundedCornerShape(8.dp))
        .clickable { expanded = true }
        .padding(horizontal = 12.dp, vertical = 8.dp)) {
        Text(text = selectedLanguage.value, color = Color.White)
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            languages.forEach { language ->
                DropdownMenuItem(
                    text = { Text(language, color = Color.Black) },
                    onClick = {
                        selectedLanguage.value = language
                        expanded = false
                    }
                )
            }
        }
    }
}


