package fi.tiko.weatherapp.ui

import android.content.Intent
import android.location.Geocoder
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
import fi.tiko.weatherapp.data.weatherJsonFiles.WeatherJsonObject
import java.lang.Exception
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val apiKey = EnvironmentVariables().apiKey

    var city = ""
    var lat : Double = 0.0
    var lon : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO implement scenarios when gps data fails
         LocationData(this,this).getLastLocation {  location, city ->
             lat = location.latitude
             lon = location.longitude
             this.city = city
             updateUi()
        }

    }

    private fun getIconUrl(icon : String) : String {
        return "https://openweathermap.org/img/wn/$icon@2x.png"
    }
    private fun updateUi() {
        thread {
            val weatherJson : WeatherJsonObject? = Request("https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$lon&units=metric&exclude=minutely,hourly,alerts&appid=$apiKey")
                .getWeatherJsonObj()
            if (weatherJson != null) {
                updateCurrentWeatherView(weatherJson)
                updateDailyWeatherView(weatherJson)
            }
        }
    }

    private fun updateCurrentWeatherView(weatherJson : WeatherJsonObject) {
        val temp = weatherJson.current?.temp?.toInt().toString() // remove decimals
        val updateTime = weatherJson.current?.getUpdateTime()
        val icon = weatherJson.current?.weather?.get(0)?.icon
        val sunrise = "Sunrise: " + weatherJson.current?.getSunriseTime()
        val sunset = "Sunset: " + weatherJson.current?.getSunsetTime()

        runOnUiThread {
            findViewById<TextView>(R.id.address).text = city
            findViewById<TextView>(R.id.temp).text = temp
            findViewById<TextView>(R.id.updated_at).text = updateTime
            Glide.with(this)
                    .load(getIconUrl(icon!!))
                    .placeholder(R.drawable.error)
                    .into(findViewById<ImageView>(R.id.statusMain))
            findViewById<TextView>(R.id.sunrise).text = sunrise
            findViewById<TextView>(R.id.sunset).text = sunset
        }
    }


    private fun updateDailyWeatherView(weatherJson : WeatherJsonObject) {
        var forecastList = mutableListOf<WeatherRowModel>()

        var count = 0
        weatherJson.daily?.forEach { forecast ->
            var day : String? = when (count) {
                0 -> "Today"
                1 -> "Tomorrow"
                else -> getDayOfWeek(forecast?.getDtAsDate()!!)
            }
            count += 1
            // remove decimals
            val max = forecast?.temp?.max?.toInt()
            val min = forecast?.temp?.min?.toInt()
            val temperatures = "$max°C / $min°C"

            val icon = forecast?.weather?.get(0)?.icon

            forecastList.add(WeatherRowModel(day?: "error", temperatures, getIconUrl(icon!!)))
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
        val intent = Intent(this, SettingsActivity::class.java)
        startActivityForResult(intent, 10)
    }

    private fun changeLocation(cityName : String) {
        try {
            var geocoder = Geocoder(this, Locale.getDefault())
            var adresses = geocoder.getFromLocationName(cityName, 1)

            if (adresses.isNotEmpty()) {
                lat = adresses[0].latitude
                lon = adresses[0].longitude
                // uppercase first letter
                city = cityName.toLowerCase()
                city = city[0].toUpperCase() + city.removeRange(0..0)
                updateUi()
            } else {
                Toast.makeText(this, "Could not find location with name $cityName", Toast.LENGTH_SHORT).show()
            }

        } catch (e : Exception) {
            Log.d("Test2", e.toString())
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 10) {
            if(resultCode == RESULT_OK) {
                val cityName = data?.extras?.getString("city")
                // update if city changes
                if (cityName != null && cityName.toLowerCase() != city.toLowerCase()) {
                    changeLocation(cityName)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }
}