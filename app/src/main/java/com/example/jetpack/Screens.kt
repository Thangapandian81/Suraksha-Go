package com.example.jetpack

sealed class Screens(val screen:String){
    data object Home:Screens("home")
    data object Profile:Screens("profile")
    data object Settings:Screens("settings")
    data object MyRides:Screens("myrides")
    data object Payment:Screens("payment")
    data object Insurance:Screens("insurance")
    data object SOS:Screens("sos")
    data object Rating:Screens("rating")
}