package fi.tiko.weatherapp.data


import com.fasterxml.jackson.annotation.JsonProperty

data class WeatherJsonObject(
    @JsonProperty("current")
    val current: Current?,
    @JsonProperty("daily")
    val daily: List<Daily>?,
    @JsonProperty("lat")
    val lat: Int?,
    @JsonProperty("lon")
    val lon: Int?,
    @JsonProperty("timezone")
    val timezone: String?,
    @JsonProperty("timezone_offset")
    val timezoneOffset: Int?
)