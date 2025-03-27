//package com.example.jetpack
//
//import android.location.Geocoder
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.util.Log
//import android.widget.EditText
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.jetpack.R
//import com.google.android.material.floatingactionbutton.FloatingActionButton
//import com.ola.mapsdk.interfaces.OlaMapCallback
//import com.ola.mapsdk.model.OlaLatLng
//import com.ola.mapsdk.model.OlaMarkerOptions
//import com.ola.mapsdk.model.OlaPolylineOptions
//import com.ola.mapsdk.view.OlaMap
//import com.ola.mapsdk.view.OlaMapView
//import java.util.Locale
//import android.content.Intent  // Add this import
//import android.widget.Button
//import kotlin.math.*
//
//class MyRides : AppCompatActivity() {
//    lateinit var olamapView: OlaMapView
//    lateinit var fetchCurrentLocation: FloatingActionButton
//    lateinit var olaMap: OlaMap
//    lateinit var startLocationEditText: EditText
//    lateinit var destinationEditText: EditText
//     lateinit var distance:Button
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_select_place)
//        distance=findViewById(R.id.btnCalculateDistance)
//        olamapView = findViewById(R.id.mapView)
//        fetchCurrentLocation = findViewById(R.id.floatingActionButton2)
//        startLocationEditText = findViewById(R.id.editTextStartLocation)
//        destinationEditText = findViewById(R.id.editTextDestination)
//
//
//        olamapView.getMap(apiKey = "HTqPUTICMPtRVIEMWCpDthqqmnEIWqC7joX0yutE",
//            olaMapCallback = object : OlaMapCallback {
//                override fun onMapReady(map: OlaMap) {
//                    olaMap = map
//                }
//
//                override fun onMapError(error: String) {
//                    Toast.makeText(this@MyRides, "Map error: $error", Toast.LENGTH_SHORT).show()
//                }
//            }
//        )
//
//
//
//        fetchCurrentLocation.setOnClickListener {
//            val startLocation = startLocationEditText.text.toString().trim()
//            val destination = destinationEditText.text.toString().trim()
//            Log.v("startlocation",startLocation)
//            Log.v("endlocation",destination)
//
//            if (startLocation.isNotEmpty() && destination.isNotEmpty())
//            {
//                val startLatLng = getLatLngFromAddress(startLocation)
//                val destinationLatLng = getLatLngFromAddress(destination)
//
//                if (startLatLng != null && destinationLatLng != null) {
//                    drawRoute(startLatLng, destinationLatLng)
//                }
//                else
//                {
//                    Toast.makeText(this, "Invalid location entered", Toast.LENGTH_SHORT).show()
//                }
//            } else
//            {
//                Toast.makeText(this, "Please enter both source and destination", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        }
//    }
//
//
//
////    private fun calculateDistanceInKm(
////        lat1: Double, lon1: Double,
////        lat2: Double, lon2: Double
////    ): Double {
////        val earthRadius = 6371.0 // Radius of Earth in KM
////
////        val dLat = Math.toRadians(lat2 - lat1)
////        val dLon = Math.toRadians(lon2 - lon1)
////
////        val a = sin(dLat / 2).pow(2.0) +
////                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
////                sin(dLon / 2).pow(2.0)
////
////        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
////
////        return earthRadius * c
////    }
//
//
//    private fun getLatLngFromAddress(address: String): OlaLatLng? {
//        val geocoder = Geocoder(this, Locale.getDefault())
//        return try {
//            val locations = geocoder.getFromLocationName(address, 1)
//            if (locations != null && locations.isNotEmpty()) {
//                OlaLatLng(locations[0].latitude, locations[0].longitude)
//            } else null
//        } catch (e: Exception) {
//            null
//        }
//    }
//
//    private fun drawRoute(start: OlaLatLng, end: OlaLatLng) {
//        val points = arrayListOf(start, end)
//        val polylineOptions = OlaPolylineOptions.Builder()
//            .setPolylineId("route1")
//            .setPoints(points)
//            .build()
//
//        olaMap.addPolyline(polylineOptions)
//
//        val startMarker = OlaMarkerOptions.Builder()
//            .setMarkerId("startMarker")
//            .setPosition(start)
//            .setIsIconClickable(true)
//            .build()
//
//        val endMarker = OlaMarkerOptions.Builder()
//            .setMarkerId("endMarker")
//            .setPosition(end)
//            .setIsIconClickable(true)
//            .build()
//
//        olaMap.addMarker(startMarker)
//        olaMap.addMarker(endMarker)
//
//        olaMap.moveCameraToLatLong(start, 12.0, 1000)
//    }
//}

package com.example.jetpack


import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ola.mapsdk.interfaces.OlaMapCallback
import com.ola.mapsdk.model.OlaLatLng
import com.ola.mapsdk.model.OlaMarkerOptions
import com.ola.mapsdk.model.OlaPolylineOptions
import com.ola.mapsdk.view.OlaMap
import com.ola.mapsdk.view.OlaMapView
import java.util.Locale
import android.content.Intent
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import kotlin.math.*

class MyRides : AppCompatActivity() {
    lateinit var olamapView: OlaMapView
    lateinit var fetchCurrentLocation: FloatingActionButton
    lateinit var olaMap: OlaMap
    lateinit var startLocationEditText: EditText
    lateinit var destinationEditText: EditText
    lateinit var distanceButton: Button

    // Store LatLng globally
    private var startLatLng: OlaLatLng? = null
    private var destinationLatLng: OlaLatLng? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@MyRides, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        })
        setContentView(R.layout.activity_select_place)

        distanceButton = findViewById(R.id.btnCalculateDistance)
        olamapView = findViewById(R.id.mapView)
        fetchCurrentLocation = findViewById(R.id.floatingActionButton2)
        startLocationEditText = findViewById(R.id.editTextStartLocation)
        destinationEditText = findViewById(R.id.editTextDestination)

        // Initialize Ola Map
        olamapView.getMap(
            apiKey = "HTqPUTICMPtRVIEMWCpDthqqmnEIWqC7joX0yutE",
            olaMapCallback = object : OlaMapCallback {
                override fun onMapReady(map: OlaMap) {
                    olaMap = map
                }

                override fun onMapError(error: String) {
                    Toast.makeText(this@MyRides, "Map error: $error", Toast.LENGTH_SHORT).show()
                }
            }
        )
        var startLocation=""
        var destinationLocation=""
        // Floating button click → Draw route
        fetchCurrentLocation.setOnClickListener {
            startLocation = startLocationEditText.text.toString().trim()
            destinationLocation = destinationEditText.text.toString().trim()

            Log.v("startlocation", startLocation)
            Log.v("endlocation", destinationLocation)

            if (startLocation.isNotEmpty() && destinationLocation.isNotEmpty()) {
                startLatLng = getLatLngFromAddress(startLocation)
                destinationLatLng = getLatLngFromAddress(destinationLocation)

                if (startLatLng != null && destinationLatLng != null) {
                    drawRoute(startLatLng!!, destinationLatLng!!)
                    Toast.makeText(this, "Route drawn. Click 'Get Distance'", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Invalid location entered", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter both source and destination", Toast.LENGTH_SHORT).show()
            }
        }

        // Distance button click → Calculate & Pass to Calculate.kt
        distanceButton.setOnClickListener {
            if (startLatLng != null && destinationLatLng != null) {
                val distanceInKm = calculateDistanceInKm(
                    startLatLng!!.latitude, startLatLng!!.longitude,
                    destinationLatLng!!.latitude, destinationLatLng!!.longitude
                )

                // Fare calculation: ₹3 per km
                val costPerKm = 3.0
                val totalFare = (distanceInKm * costPerKm)+1.50+0.30

                // Estimate time: assuming 40 km/h average → time in minutes
                val estimatedTimeMinutes = (distanceInKm / 40.0) * 60
                val estimatedTimeString = "%.0f mins".format(estimatedTimeMinutes)

                // Pass all data to Calculate.kt
                val intent = Intent(this@MyRides, Calculate::class.java)
                intent.putExtra("DISTANCE_KM", distanceInKm)
                intent.putExtra("TOTAL_FARE", totalFare)
                intent.putExtra("ESTIMATED_TIME", estimatedTimeString)
                intent.putExtra("Source",startLocation)
                intent.putExtra("desti",destinationLocation)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please draw route first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Calculate Haversine Distance in KM
    private fun calculateDistanceInKm(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val earthRadius = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2.0) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2.0)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

    // Convert address to LatLng
    private fun getLatLngFromAddress(address: String): OlaLatLng? {
        val geocoder = Geocoder(this, Locale.getDefault())
        return try {
            val locations = geocoder.getFromLocationName(address, 1)
            if (locations != null && locations.isNotEmpty()) {
                OlaLatLng(locations[0].latitude, locations[0].longitude)
            } else null
        } catch (e: Exception) {
            null
        }
    }

    // Draw route on Ola map
    private fun drawRoute(start: OlaLatLng, end: OlaLatLng) {
        val points = arrayListOf(start, end)
        val polylineOptions = OlaPolylineOptions.Builder()
            .setPolylineId("route1")
            .setPoints(points)
            .build()

        olaMap.addPolyline(polylineOptions)

        val startMarker = OlaMarkerOptions.Builder()
            .setMarkerId("startMarker")
            .setPosition(start)
            .setIsIconClickable(true)
            .build()

        val endMarker = OlaMarkerOptions.Builder()
            .setMarkerId("endMarker")
            .setPosition(end)
            .setIsIconClickable(true)
            .build()

        olaMap.addMarker(startMarker)
        olaMap.addMarker(endMarker)

        olaMap.moveCameraToLatLong(start, 12.0, 1000)

    }
}

