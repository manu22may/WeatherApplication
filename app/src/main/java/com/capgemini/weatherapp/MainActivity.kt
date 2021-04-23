package com.capgemini.weatherapp

import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        displayCurrentWeather()

        floatingActionButton.setOnClickListener {
            val pMenu = PopupMenu(this,it)
            val menu = pMenu.menu
            menu.add("Current Weather")
            menu.add("Hourly Forecast")
            menu.add("Daily Forecast")
            menu.add("Change Weather Location")
            pMenu.show()
            pMenu.setOnMenuItemClickListener {
                when(it.title){
                    "Current Weather"->{
                        displayCurrentWeather()
                        true
                    }
                    "Hourly Forecast"->{
                        displayHourlyWeather()
                        true
                    }
                    "Daily Forecast"->{
                        displayDailyWeather()
                        true
                    }
                    "Change Weather Location"->{
                        changeWeatherLocation()
                        true}
                    else->{false}
                }
            }
        }




/*        val key = resources.getString(R.string.api_key)
        val request = OpenWeatherInterface.getInstance().getWeatherAll("https://api.openweathermap.org/data/2.5/onecall?lat=33.441792&lon=-94.037689&exclude=hourly,daily&appid=9ff634aced5342a274e1882a50f3960f")
        request.enqueue(object :Callback<WeatherAll>{
            override fun onResponse(call: Call<WeatherAll>, response: Response<WeatherAll>) {
                Log.d("Retrofit","Success ${response.body()}")

            }

            override fun onFailure(call: Call<WeatherAll>, t: Throwable) {
                Log.d("Retrofit","Fail")
            }

        })*/
    }

    private fun displayHourlyWeather() {
        val frag = HourlyWeatherFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.parentL,frag)
            .addToBackStack(null)
            .commit()
    }

    private fun changeWeatherLocation() {
        val frag = LocationFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.parentL,frag)
            .addToBackStack(null)
            .commit()
    }

    private fun displayDailyWeather() {
        val frag = DailyWeatherFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.parentL,frag)
                .commit()
    }

    private fun displayCurrentWeather() {
        val frag = CurrentWeatherFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.parentL,frag)
                .commit()
    }
}