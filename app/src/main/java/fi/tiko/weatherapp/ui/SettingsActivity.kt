package fi.tiko.weatherapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import fi.tiko.weatherapp.R


class SettingsActivity : AppCompatActivity() {
    lateinit var city : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        Log.d("Test1", "Settings opened")
        city = findViewById(R.id.editCity)
    }

    fun save(view : View) {
        val intent = Intent()
        intent.putExtra("city", city.text.toString())
        setResult(RESULT_OK, intent);
        finish()
    }
}