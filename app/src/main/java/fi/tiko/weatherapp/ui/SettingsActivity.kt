package fi.tiko.weatherapp.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import fi.tiko.weatherapp.R
import java.util.jar.Manifest


class SettingsActivity : AppCompatActivity() {
    lateinit var city : EditText
    private var PERMISSION_ID = 80

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locactionRequest : LocationRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        Log.d("Test1", "Settings opened")
        city = findViewById(R.id.editCity)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

    }


    private fun getLastLocation(){
        if (checkLocationPermissions()) {

            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location : Location? = task.result
                    if (location == null) {
                        getNewLocation()
                    } else {
                        Log.d("Test1", "Lat: ${location.latitude.toString()} Lon: ${location.longitude.toString()} ")
                    }
                }
            } else {
                Toast.makeText(this, "Please enable your location service", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestLocationPermission()
        }
    }


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation : Location = p0.lastLocation
            Log.d("Test1", "New location Lat: ${lastLocation.latitude.toString()} Lon: ${lastLocation.longitude.toString()} ")
            super.onLocationResult(p0)
        }
    }
    private fun getNewLocation() {
        locactionRequest = LocationRequest()
        locactionRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locactionRequest.interval = 0
        locactionRequest.fastestInterval = 0
        locactionRequest.numUpdates = 2
        fusedLocationProviderClient!!.requestLocationUpdates(
                locactionRequest, locationCallback, Looper.myLooper()
        )
    }

    private fun checkLocationPermissions():Boolean {
        if (
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        return false
    }

    private fun requestLocationPermission(){
        ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSION_ID)
    }

    private fun isLocationEnabled() : Boolean {
        var locationManager : LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Test1", "You have permissions")
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun save(view : View) {
        val intent = Intent()
        intent.putExtra("city", city.text.toString())
        setResult(RESULT_OK, intent);
        finish()
    }

    fun getLocation(view: View) {
        getLastLocation()
        Log.d("Test1", "Get loc pressed")
    }
}