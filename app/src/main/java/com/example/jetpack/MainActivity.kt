package com.example.jetpack

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
//import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpack.ui.theme.JetPackTheme
import com.example.jetpack.ui.theme.YellowJC
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            JetPackTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavDrawer()
                }
            }
        }
    }


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NavDrawer() {
        val navigationController = rememberNavController()
        val coroutineScope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val context = LocalContext.current.applicationContext

        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = true,
            drawerContent = {
                ModalDrawerSheet(modifier = Modifier.background(Color.Black)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(Color.Black) // Ensure full black background
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.profileboy), // Replace with actual image
                                contentDescription = "User Image",
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, YellowJC, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "John Doe",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = YellowJC
                                )
                                Text(
                                    text = "johndoe@example.com",
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Divider(color = YellowJC)

                    NavigationDrawerItem(
                        label = { Text(text = "Home", color = YellowJC) },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "home",
                                tint = YellowJC
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            navigationController.navigate(Screens.Home.screen) {
                                popUpTo(0)
                            }
                        })
                    NavigationDrawerItem(label = { Text(text = "MyRides", color = YellowJC) },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = "myrides",
                                tint = YellowJC
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            navigationController.navigate(Screens.MyRides.screen) {
                                popUpTo(0)
                            }
                        })
                    NavigationDrawerItem(label = { Text(text = "Payment", color = YellowJC) },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.AccountBalanceWallet,
                                contentDescription = "payment",
                                tint = YellowJC
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            navigationController.navigate(Screens.Payment.screen) {
                                popUpTo(0)
                            }
                        })
                    NavigationDrawerItem(label = { Text(text = "Insurance", color = YellowJC) },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Security,
                                contentDescription = "insurance",
                                tint = YellowJC
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            navigationController.navigate(Screens.Insurance.screen) {
                                popUpTo(0)
                            }
                        })
                    NavigationDrawerItem(label = { Text(text = "SOS", color = YellowJC) },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = "sos",
                                tint = YellowJC
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            navigationController.navigate(Screens.SOS.screen) {
                                popUpTo(0)
                            }
                        })
                    NavigationDrawerItem(label = { Text(text = "Profile", color = YellowJC) },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "profile",
                                tint = YellowJC
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            navigationController.navigate(Screens.Profile.screen) {
                                popUpTo(0)
                            }
                        })
                    NavigationDrawerItem(label = { Text(text = "Rating", color = YellowJC) },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "rating",
                                tint = YellowJC
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            navigationController.navigate(Screens.Rating.screen) {
                                popUpTo(0)
                            }
                        })
                    NavigationDrawerItem(label = { Text(text = "Settings", color = YellowJC) },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "settings",
                                tint = YellowJC
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            navigationController.navigate(Screens.Settings.screen) {
                                popUpTo(0)
                            }
                        })
                    NavigationDrawerItem(label = { Text(text = "Logout", color = YellowJC) },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "logout",
                                tint = YellowJC
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()
                        })
                }
            },
        ) {
            Scaffold(
                topBar = {
                    val coroutineScope = rememberCoroutineScope()
                    TopAppBar(
                        title = { Text(text = "Ride Booking") },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = YellowJC,
                            titleContentColor = Color.White,
                            navigationIconContentColor = Color.White
                        ),
                        navigationIcon = {
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(
                                    Icons.Rounded.Menu, contentDescription = "MenuButton"
                                )
                            }
                        },
                    )
                }
            ) {
                NavHost(
                    navController = navigationController,
                    startDestination = Screens.Home.screen
                ) {
                    composable(Screens.Home.screen) {
                        Home(
                            drawerState = drawerState,
                            navController = navigationController
                        )
                    }
                    composable(Screens.MyRides.screen) {
                        RideLauncher()  // ✅ Which opens Ride Activity
                    }


                    composable(Screens.Payment.screen) { Payment() }
                    composable(Screens.Insurance.screen) { Insurance() }
                    composable(Screens.SOS.screen) { SOS() }
                    composable(Screens.Rating.screen) { Rating() }
                    composable(Screens.Profile.screen) { Profile() }
                    composable(Screens.Settings.screen) { Settings() }

                }

            }
        }
    }

    @Composable
    fun RideLauncher() {
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            val intent = Intent(context, MyRides::class.java)  // ✅ This is correct
            context.startActivity(intent)
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Opening Ride Screen...")
        }
    }
}




