package fi.tiko.weatherapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import fi.tiko.weatherapp.R

class WeatherAdapter (var mCtx: Context, var resources: Int, var items: List<WeatherRowModel>): ArrayAdapter<WeatherRowModel>(mCtx, resources, items){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater:LayoutInflater = LayoutInflater.from(mCtx)
        val view:View = layoutInflater.inflate(resources, null)

        val dayOfWeather:TextView = view.findViewById(R.id.dayOfWeather)
        val temperatures:TextView = view.findViewById(R.id.temperatures)
        val statusImg:TextView = view.findViewById(R.id.statusImg)

        val mItem:WeatherRowModel = items[position]
        dayOfWeather.text = mItem.day
        statusImg.text = mItem.statusImg
        temperatures.text = mItem.temperatures

        return view
    }
}