package fi.tiko.weatherapp.data

import android.util.Log
import java.net.URL

class Request(private val url: String){
    fun run() {
        val jsonData = URL(url).readText()
        Log.d("Test1", jsonData)
    }
}