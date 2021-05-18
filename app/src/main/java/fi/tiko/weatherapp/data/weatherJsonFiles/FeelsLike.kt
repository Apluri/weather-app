package fi.tiko.weatherapp.data.weatherJsonFiles


import com.fasterxml.jackson.annotation.JsonProperty

data class FeelsLike(
    @JsonProperty("day")
    val day: Double?,
    @JsonProperty("eve")
    val eve: Double?,
    @JsonProperty("morn")
    val morn: Double?,
    @JsonProperty("night")
    val night: Double?
)