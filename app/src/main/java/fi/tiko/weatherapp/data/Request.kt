package fi.tiko.weatherapp.data

import com.fasterxml.jackson.databind.ObjectMapper
import fi.tiko.weatherapp.data.weatherJsonFiles.WeatherJsonObject
import org.json.JSONObject
import java.net.URL

class Request(private val url: String){
    fun getJsonAsString() : String? {
        return URL(url).readText()
    }
    fun getJsonObj() : JSONObject {
        return JSONObject(URL(url).readText())
    }

    fun getWeatherJsonObj() : WeatherJsonObject? {
        return ObjectMapper().readValue(getJsonAsString(), WeatherJsonObject::class.java)
    }
}