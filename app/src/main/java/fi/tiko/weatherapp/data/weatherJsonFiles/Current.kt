package fi.tiko.weatherapp.data.weatherJsonFiles


import com.fasterxml.jackson.annotation.JsonProperty
import java.text.SimpleDateFormat
import java.util.*

data class Current(
    @JsonProperty("clouds")
    val clouds: Int?,
    @JsonProperty("dew_point")
    val dewPoint: Double?,
    @JsonProperty("dt")
    val dt: Int?,
    @JsonProperty("feels_like")
    val feelsLike: Double?,
    @JsonProperty("humidity")
    val humidity: Int?,
    @JsonProperty("pressure")
    val pressure: Int?,
    @JsonProperty("sunrise")
    val sunrise: Int?,
    @JsonProperty("sunset")
    val sunset: Int?,
    @JsonProperty("temp")
    val temp: Double?,
    @JsonProperty("uvi")
    val uvi: Double?,
    @JsonProperty("visibility")
    val visibility: Int?,
    @JsonProperty("weather")
    val weather: List<Weather>?,
    @JsonProperty("wind_deg")
    val windDeg: Int?,
    @JsonProperty("wind_gust")
    val windGust: Double?,
    @JsonProperty("wind_speed")
    val windSpeed: Double?
) {
    fun getUpdateTime() : String? {
        if (dt != null) return SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(dt.toLong() * 1000))
        return null
    }
    fun getSunriseTime() : String? {
        if (sunrise != null) return SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise?.toLong() * 1000))
        return null
    }
    fun getSunsetTime() : String? {
        if (sunset != null) return SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset?.toLong() * 1000))
        return null
    }
}