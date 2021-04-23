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
class MyWeatherRecyclerViewAdapter(
        private val values: List<DailyWeather>
)
    : RecyclerView.Adapter<MyWeatherRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_daily_weather, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        val dailyTime = Calendar.getInstance()
        dailyTime.timeInMillis = item.dt.toLong()*1000
        val day=dailyTime.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG, Locale.getDefault())

        val imageUrl = "https://openweathermap.org/img/wn/${item.weather[0].icon}.png"
        Glide.with(holder.itemView).load(Uri.parse(imageUrl)).into(holder.image)

        holder.day.text=day.toString()
        holder.minTemp.text = "${item.temp.min}°C"
        holder.maxTemp.text = "${item.temp.max}°C"
        holder.main.text="${item.weather[0].main}"
        holder.desc.setText("${item.weather[0].description.capitalize()}")

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val minTemp =view.findViewById<TextView>(R.id.dailyMinTempT)
        val maxTemp =view.findViewById<TextView>(R.id.dailyMaxTempT)
        val day =view.findViewById<TextView>(R.id.dailyDayT)
        val main =view.findViewById<TextView>(R.id.dailyMainT)
        val desc =view.findViewById<TextView>(R.id.dailyDescT)
        val image =view.findViewById<ImageView>(R.id.dailyImage)

/*        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }*/
    }
}