package com.example.jetpack

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Security
//import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jetpack.ui.theme.YellowJC
import kotlinx.coroutines.launch

// Data class for menu items
data class MenuItem(val title: String, val icon: ImageVector, val route: String)

// List of sidebar menu items
val menuItems = listOf(
    MenuItem("Home", Icons.Default.Home, Screens.Home.screen),
    MenuItem("Book Ride", Icons.Default.Place, Screens.MyRides.screen),
    MenuItem("My Rides", Icons.Default.Place, Screens.MyRides.screen),
    MenuItem("Payment", Icons.Filled.AccountBalanceWallet, Screens.Payment.screen),
    MenuItem("Insurance", Icons.Filled.Security, Screens.Insurance.screen),
    MenuItem("SOS", Icons.Filled.Warning, Screens.SOS.screen),
    MenuItem("Profile", Icons.Default.Person, Screens.ProfileScreen.screen),
//    MenuItem("Settings", Icons.Default.Settings, Screens.Settings.screen),
    MenuItem("Rating", Icons.Filled.Star, Screens.Rating.screen)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController, drawerState: DrawerState) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Suraksha Go") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = YellowJC,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch { drawerState.open() }
                    }) {
                        Icon(Icons.Rounded.Menu, contentDescription = "Menu Button")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
        ) {
            Text(
                text = "Dashboard",
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                color = YellowJC
            )

            // Grid View for Menu Items
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(menuItems) { item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = YellowJC),
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .height(130.dp)
                            .clickable { navController.navigate(item.route) }, // Navigation
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = Color.Black,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = item.title, color = Color.Black, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}


