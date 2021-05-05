package fi.tiko.weatherapp.data

import android.util.Log
import org.json.JSONObject
import java.net.URL

class Request(private val url: String){
    fun getJsonAsString() : String? {
        return URL(url).readText()
    }
    fun getJsonObj() : JSONObject {
        return JSONObject(URL(url).readText())
    }
}