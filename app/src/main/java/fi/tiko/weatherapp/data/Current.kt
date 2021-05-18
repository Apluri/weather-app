package fi.tiko.weatherapp.data


import com.fasterxml.jackson.annotation.JsonProperty

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
)