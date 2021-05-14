package fi.tiko.weatherapp.ui

import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import fi.tiko.weatherapp.R
import fi.tiko.weatherapp.data.EnvironmentVariables
import fi.tiko.weatherapp.data.LocationData
import fi.tiko.weatherapp.data.Request
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val apiKey = EnvironmentVariables().apiKey

    // TODO: 07/05/2021 Changing city does not do anything anymore due new api call, implement conversion to lat and long
    var city = ""

    var lat : Double = 0.0
    var lon : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("Test1", "On create")
        // TODO implement scenarios when gps data fails
         LocationData(this,this).getLastLocation {  location, city ->
             lat = location.latitude
             lon = location.longitude
             this.city = city
             updateUi()
        }

    }

    private fun updateUi() {
        thread {
            val weatherInfo : JSONObject = Request("https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$lon&units=metric&exclude=minutely,hourly,alerts&appid=$apiKey").getJsonObj()
            val currentWeather : JSONObject = weatherInfo.getJSONObject("current")
            val dailyWeather : JSONArray = weatherInfo.getJSONArray("daily")
            updateCurrentWeatherView(currentWeather)
            updateDailyWeatherView(dailyWeather)
        }
    }

    private fun updateCurrentWeatherView(currentWeather : JSONObject) {
        val temp = currentWeather.getString("temp").toDouble().toInt().toString()+"°C" // remove decimals
        val updateTime = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(currentWeather.getLong("dt")*1000))
        val icon = currentWeather.getJSONArray("weather").getJSONObject(0).getString("icon")
        val sunrise = "Sunrise: " + SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(currentWeather.getLong("sunrise")*1000))
        val sunset = "Sunset: " + SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(currentWeather.getLong("sunset")*1000))
        runOnUiThread {
            Toast.makeText(this, "Request performed", Toast.LENGTH_SHORT).show()
            findViewById<TextView>(R.id.address).text = city
            findViewById<TextView>(R.id.temp).text = temp
            findViewById<TextView>(R.id.updated_at).text = updateTime
            Glide.with(this)
                    .load("https://openweathermap.org/img/wn/$icon@2x.png")
                    .placeholder(R.drawable.error)
                    .into(findViewById<ImageView>(R.id.statusMain))
            findViewById<TextView>(R.id.sunrise).text = sunrise
            findViewById<TextView>(R.id.sunset).text = sunset
        }
    }

    private fun updateDailyWeatherView(dailyWeather : JSONArray) {
        var forecastList = mutableListOf<WeatherRowModel>()

        // fill forecastList with weather rows
        for (i in 0 until dailyWeather.length()) {
            val weatherData = dailyWeather.getJSONObject(i)

            var day : String? = when (i) {
                0 -> "Today"
                1 -> "Tomorrow"
                else -> getDayOfWeek(Date(weatherData.getLong("dt") * 1000))
            }

            val temp = weatherData.getJSONObject("temp")
            // remove decimals
            val max = temp.getString("max").toDouble().toInt()
            val min = temp.getString("min").toDouble().toInt()
            val temperatures = "$max°C / $min°C"

            val icon = weatherData.getJSONArray("weather").getJSONObject(0).getString("icon")
            val statusImgUrl = "https://openweathermap.org/img/wn/$icon@2x.png"

            forecastList.add(WeatherRowModel(day?: "error", temperatures, statusImgUrl))

        }
        runOnUiThread {
            findViewById<ListView>(R.id.dailyList).adapter = WeatherAdapter(this, R.layout.row, forecastList)
        }
    }

    private fun getDayOfWeek(date : Date) : String? {
        return when (date.day) {
            0 -> "Sunday"
            1 -> "Monday"
            2 -> "Tuesday"
            3 -> "Wednesday"
            4 -> "Thursday"
            5 -> "Friday"
            6 -> "Saturday"
            else -> null
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
                // update if city changes
                if (value != null && value != city) {
                    city = value             
                    updateUi()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }
}