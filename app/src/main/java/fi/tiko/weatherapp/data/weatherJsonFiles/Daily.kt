package fi.tiko.weatherapp.data.weatherJsonFiles


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class Daily(
    @JsonProperty("clouds")
    val clouds: Int?,
    @JsonProperty("dew_point")
    val dewPoint: Double?,
    @JsonProperty("dt")
    val dt: Int?,
    @JsonProperty("feels_like")
    val feelsLike: FeelsLike?,
    @JsonProperty("humidity")
    val humidity: Int?,
    @JsonProperty("moon_phase")
    val moonPhase: Double?,
    @JsonProperty("moonrise")
    val moonrise: Int?,
    @JsonProperty("moonset")
    val moonset: Int?,
    @JsonProperty("pop")
    val pop: Double?,
    @JsonProperty("pressure")
    val pressure: Int?,
    @JsonProperty("rain")
    val rain: Double?,
    @JsonProperty("sunrise")
    val sunrise: Int?,
    @JsonProperty("sunset")
    val sunset: Int?,
    @JsonProperty("temp")
    val temp: Temp?,
    @JsonProperty("uvi")
    val uvi: Double?,
    @JsonProperty("weather")
    val weather: List<Weather>?,
    @JsonProperty("wind_deg")
    val windDeg: Int?,
    @JsonProperty("wind_gust")
    val windGust: Double?,
    @JsonProperty("wind_speed")
    val windSpeed: Double?
) {
    fun getDtAsDate() : Date? {
        return if (dt != null) return Date(dt?.toLong() * 1000) else null
    }
}