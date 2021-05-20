package fi.tiko.weatherapp.ui

import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import fi.tiko.weatherapp.R
import fi.tiko.weatherapp.data.EnvironmentVariables
import fi.tiko.weatherapp.data.LocationData
import fi.tiko.weatherapp.data.Request
import fi.tiko.weatherapp.data.weatherJsonFiles.WeatherJsonObject
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val apiKey = EnvironmentVariables().apiKey
    private val settingsResultCode = 10

    var city : String = ""
    var latitude : Double = 0.0
    var longitude : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         LocationData(this,this).getLastKnownLocation { location, city ->
             latitude = location.latitude
             longitude = location.longitude
             this.city = city
             updateUi()
         }

    }

    private fun getIconUrl(icon : String) : String {
        return "https://openweathermap.org/img/wn/$icon@2x.png"
    }

    private fun updateUi() {
        thread {
            val weatherJson : WeatherJsonObject? = Request("https://api.openweathermap.org/data/2.5/onecall?lat=$latitude&lon=$longitude&units=metric&exclude=minutely,hourly,alerts&appid=$apiKey")
                .getWeatherJsonObj()
            if (weatherJson != null) {
                updateCurrentWeatherView(weatherJson)
                updateDailyWeatherView(weatherJson)
            } else {
                runOnUiThread {
                    Toast.makeText(this, "Error while fetching data", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // current weather view is the upper section of the main activity
    private fun updateCurrentWeatherView(weatherJson : WeatherJsonObject) {
        val temp = weatherJson.current?.temp?.toInt().toString()  + "°C"// remove decimals
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

    // updates the lower section of the main activity
    private fun updateDailyWeatherView(weatherJson : WeatherJsonObject) {
        var forecastList = mutableListOf<WeatherRowModel>()

        weatherJson.daily?.forEachIndexed { index, forecast ->
            var day : String? = when (index) {
                0 -> "Today"
                1 -> "Tomorrow"
                else -> getDayOfWeek(forecast?.getDtAsDate()!!)
            }

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
        startActivityForResult(intent, settingsResultCode)
    }

    private fun changeLocation(cityName : String) {
        var geocoder = Geocoder(this, Locale.getDefault())
        var adresses = geocoder.getFromLocationName(cityName, 1)
        if (adresses.isNotEmpty()) {
            latitude = adresses[0].latitude
            longitude = adresses[0].longitude
            city = cityName.capitalize()
            updateUi()
        } else {
            Toast.makeText(this, "Could not find location with name $cityName", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == settingsResultCode && resultCode == RESULT_OK) {
            val cityName = data?.extras?.getString("city")
            if (cityName != null && !isCityNamesSame(cityName)) {
                changeLocation(cityName)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
8
    }

    private fun isCityNamesSame(cityName: String) = cityName.equals(city, ignoreCase = true)
}