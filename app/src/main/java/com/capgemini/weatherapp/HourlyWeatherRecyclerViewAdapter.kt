package com.capgemini.weatherapp

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import com.capgemini.weatherapp.dummy.DummyContent.DummyItem
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class HourlyWeatherRecyclerViewAdapter(
        private val values: List<HourlyWeather>
)
    : RecyclerView.Adapter<HourlyWeatherRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_hourly_weather, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        val hourlyTime = Calendar.getInstance()
        hourlyTime.timeInMillis = item.dt.toLong()*1000
        val day=convertToTime(hourlyTime)

        val imageUrl = "https://openweathermap.org/img/wn/${item.weather[0].icon}.png"
        Glide.with(holder.itemView).load(Uri.parse(imageUrl)).into(holder.hourlyImage)

        holder.hourlyTime.text = day
        holder.hourlyTemp.text = item.temp.toString()
        holder.hourlySpeed.text = "${item.wind_speed}m/s"
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val hourlyTime = view.findViewById<TextView>(R.id.hourlyTimeT)
        val hourlyTemp = view.findViewById<TextView>(R.id.hourlyTempT)
        val hourlySpeed = view.findViewById<TextView>(R.id.hourlySpeedT)
        val hourlyImage = view.findViewById<ImageView>(R.id.hourlyImage)
    }

    private fun convertToTime(calendar: Calendar): String {
        var hour =calendar.get(Calendar.HOUR_OF_DAY)
        var end ="am"
        if(hour > 12){
            hour-=12
            end="pm"
        }
        var minute = calendar.get(Calendar.MINUTE)
        var time=""
        if(hour<10) time = "0$hour:" else time = "$hour:"
        if(minute<10) time += "0$minute" else time += "$minute"
        return time
    }
}