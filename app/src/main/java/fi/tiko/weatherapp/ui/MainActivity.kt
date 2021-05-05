package fi.tiko.weatherapp.ui

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
    val city = "Tampere"
    lateinit var temperature : TextView
    lateinit var status : TextView
    lateinit var sunrise : TextView
    lateinit var sunset : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("Test1", "App launched!")
        findViewById<TextView>(R.id.address).text = city
        temperature = findViewById(R.id.temp)
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
                temperature.text = jsonObj.getJSONObject("main").getString("temp")+"°C"
                findViewById<TextView>(R.id.updated_at).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(updateTime*1000))
                status.text = jsonObj.getJSONArray("weather").getJSONObject(0).getString("description")
                sunrise.text = "Sunrise: " + SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sys.getLong("sunrise")*1000))
                sunset.text = "Sunset: " + SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sys.getLong("sunset")*1000))


            }
        }
    }

    // TODO: 05/05/2021 Open settings activity
    fun openSettings(view : View) {
        Log.d("Test1", "open settings")
    }
}