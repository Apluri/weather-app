package fi.tiko.weatherapp.ui

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import fi.tiko.weatherapp.R
import fi.tiko.weatherapp.data.LocationData


class SettingsActivity : AppCompatActivity() {
    lateinit var city : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        city = findViewById(R.id.editCity)
    }

    fun save(view : View) {
        if (city.text.isNotEmpty()) {
            val intent = Intent()
            intent.putExtra("city", city.text.toString())
            setResult(RESULT_OK, intent)
            finish()
        } else {
            onBackPressed()
        }
    }

    fun getLocation(view : View) {
        LocationData(this,this).getLastKnownLocation { _, city ->
            this.city.setText(city)
        }
    }

}