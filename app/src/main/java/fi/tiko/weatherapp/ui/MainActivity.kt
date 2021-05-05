package fi.tiko.weatherapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import fi.tiko.weatherapp.R
import fi.tiko.weatherapp.data.EnvironmentVariables
import fi.tiko.weatherapp.data.Request
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    var city = "Tampere" // change to mobile location
    lateinit var address : TextView
    lateinit var temperature : TextView
    lateinit var updatedAt : TextView
    lateinit var status : TextView
    lateinit var sunrise : TextView
    lateinit var sunset : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("Test1", "App launched!")
        address = findViewById(R.id.address)
        temperature = findViewById(R.id.temp)
        updatedAt = findViewById(R.id.updated_at)
        status = findViewById(R.id.status)
        sunrise = findViewById(R.id.sunrise)
        sunset = findViewById(R.id.sunset)

        updateUi()

    }

    private fun updateUi() {
        thread {
            val jsonObj = Request("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=${EnvironmentVariables().apiKey}").getJsonObj()
            val updateTime : Long = jsonObj.getLong("dt")
            val sys = jsonObj.getJSONObject("sys")
            runOnUiThread {
                Toast.makeText(this, "Request performed", Toast.LENGTH_SHORT).show()
                address.text = city
                temperature.text = jsonObj.getJSONObject("main").getString("temp")+"°C"
                updatedAt.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(updateTime*1000))
                status.text = jsonObj.getJSONArray("weather").getJSONObject(0).getString("description")
                sunrise.text = "Sunrise: " + SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sys.getLong("sunrise")*1000))
                sunset.text = "Sunset: " + SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sys.getLong("sunset")*1000))


            }
        }
    }

    fun openSettings(view : View) {
        Log.d("Test1", "open settings")
        val intent = Intent(this, SettingsActivity::class.java).apply {
            putExtra("currentCity", city)
        }
        startActivityForResult(intent, 10)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 10) {
            if(resultCode == RESULT_OK) {
                val value = data?.extras?.getString("city")
                Log.d("Test1", value)
                if (value != null && value != city) city = value
                updateUi()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }
}