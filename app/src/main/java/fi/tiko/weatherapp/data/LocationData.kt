package fi.tiko.weatherapp.data

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.util.*

class LocationData(val context : Context, val activity: Activity) {
    private var PERMISSION_ID = 80
    private var fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private lateinit var locationRequest : LocationRequest


    // tries to get last known location, if success then calls lamda with location
    fun getLastLocation(callback : (location : Location, city : String) -> Unit) {
        if (checkLocationPermissions()) {

            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location : Location? = task.result
                    if (location == null) {
                        getNewLocation()
                    } else {
                        callback(location, getCityName(location))
                    }
                }
            } else {
                Toast.makeText(context, "Please enable your location service", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestLocationPermission()
        }
    }

    fun getCityName(location : Location) : String {
        var city = ""
        var geoCoder = Geocoder(context, Locale.getDefault())
        var address = geoCoder.getFromLocation(location.latitude, location.longitude, 1)

        city = address.get(0).locality
        return city
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation : Location = p0.lastLocation
            // callback(lastLocation)
            super.onLocationResult(p0)
        }
    }
    private fun getNewLocation() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }

    private fun checkLocationPermissions():Boolean {
        if (
            ActivityCompat.checkSelfPermission(context,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        return false
    }

    private fun requestLocationPermission(){
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
            PERMISSION_ID)
    }

    private fun isLocationEnabled() : Boolean {
        var locationManager : LocationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

}