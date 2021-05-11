package fi.tiko.weatherapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import fi.tiko.weatherapp.R
import fi.tiko.weatherapp.data.EnvironmentVariables
import fi.tiko.weatherapp.data.Request
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val apiKey = EnvironmentVariables().apiKey

    // TODO: 07/05/2021 Changing city does not do anything anymore due new api call, implement conversion to lat and long
    var city = "Nokia" // change to mobile location

    // TODO: 07/05/2021 change to dynamically change based on location or by city given
    var lat = 61.48
    var lon = 23.50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("Test1", "App launched!")
        updateUi()

        // Test data for listview
        var listView = findViewById<ListView>(R.id.dailyList)
        var list = mutableListOf<WeatherRowModel>()
        list.add(WeatherRowModel("Today", "4°C / -1°C", "Cloudy"))
        list.add(WeatherRowModel("Tomorrow", "14°C / 4°C", "Sunny"))
        list.add(WeatherRowModel("Wednesday", "12°C / 5°C", "Broken clouds"))
        list.add(WeatherRowModel("Thursday", "6°C / -4°C", "Rainy"))
        list.add(WeatherRowModel("Friday", "14°C / 4°C", "Sunny"))
        list.add(WeatherRowModel("Saturday", "17°C / 8°C", "Sunny"))
        list.add(WeatherRowModel("Sunday", "25°C / 19°C", "Sunny"))



        listView.adapter = WeatherAdapter(this, R.layout.row, list)



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
        val temp = currentWeather.getString("temp")+"°C"
        val updateTime = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(currentWeather.getLong("dt")*1000))
        val weatherDescription = currentWeather.getJSONArray("weather").getJSONObject(0).getString("description")
        val sunrise = "Sunrise: " + SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(currentWeather.getLong("sunrise")*1000))
        val sunset = "Sunset: " + SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(currentWeather.getLong("sunset")*1000))
        runOnUiThread {
            Toast.makeText(this, "Request performed", Toast.LENGTH_SHORT).show()
            findViewById<TextView>(R.id.address).text = city
            findViewById<TextView>(R.id.temp).text = temp
            findViewById<TextView>(R.id.updated_at).text = updateTime
            findViewById<TextView>(R.id.status).text = weatherDescription
            findViewById<TextView>(R.id.sunrise).text = sunrise
            findViewById<TextView>(R.id.sunset).text = sunset
        }
    }

    private fun updateDailyWeatherView(dailyWeather : JSONArray) {
        // TODO: 07/05/2021 Implement this
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