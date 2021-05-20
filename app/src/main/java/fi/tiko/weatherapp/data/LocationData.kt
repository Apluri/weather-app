package fi.tiko.weatherapp.data

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.util.*

class LocationData(private val mainContext : Context, private val activity: Activity) {
    private val PERMISSION_ID = 80
    private val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mainContext)

    fun getLastKnownLocation(callback : (location : Location, city : String) -> Unit) {
        if (checkLocationPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location : Location? = task.result
                    if (location != null) callback(location, getCityName(location))
                }
            } else {
                Toast.makeText(mainContext, "Please enable your location service", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun getCityName(location : Location) : String {
        var city = ""
        var geoCoder = Geocoder(mainContext, Locale.getDefault())
        var address = geoCoder.getFromLocation(location.latitude, location.longitude, 1)

        city = address[0].locality
        return city
    }

    private fun checkLocationPermissions():Boolean {
        if (
            ActivityCompat.checkSelfPermission(mainContext,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(mainContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
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