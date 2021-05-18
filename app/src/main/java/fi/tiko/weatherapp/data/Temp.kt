package fi.tiko.weatherapp.data


import com.fasterxml.jackson.annotation.JsonProperty

data class Temp(
    @JsonProperty("day")
    val day: Double?,
    @JsonProperty("eve")
    val eve: Double?,
    @JsonProperty("max")
    val max: Double?,
    @JsonProperty("min")
    val min: Double?,
    @JsonProperty("morn")
    val morn: Double?,
    @JsonProperty("night")
    val night: Double?
)