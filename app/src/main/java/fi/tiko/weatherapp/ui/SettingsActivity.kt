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
import fi.tiko.weatherapp.data.LocationData
import java.util.jar.Manifest


class SettingsActivity : AppCompatActivity() {
    lateinit var city : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        Log.d("Test1", "Settings opened")
        city = findViewById(R.id.editCity)
    }

    fun save(view : View) {
        val intent = Intent()
        intent.putExtra("city", city.text.toString())
        setResult(RESULT_OK, intent);
        finish()
    }

    fun getLocation(view : View) {
        LocationData(this,this).getLastLocation { _, city ->
            this.city.setText(city)
        }
    }

}