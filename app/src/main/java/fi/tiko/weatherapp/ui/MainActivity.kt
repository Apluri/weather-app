package fi.tiko.weatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import fi.tiko.weatherapp.R
import fi.tiko.weatherapp.data.EnvironmentVariables
import fi.tiko.weatherapp.data.Request
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("Test1", "App launched!")
        thread {
            Request("https://api.openweathermap.org/data/2.5/weather?q=tampere&appid=${EnvironmentVariables().apiKey}").run()
            runOnUiThread {
                Toast.makeText(this, "Request performed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}