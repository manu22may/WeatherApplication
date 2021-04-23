package com.capgemini.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A fragment representing a list of Items.
 */
class HourlyWeatherFragment : Fragment() {

    private var columnCount = 1
    var latt="13.0827"
    var longi="80.2707"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = activity?.getSharedPreferences(
            "location",
            AppCompatActivity.MODE_PRIVATE
        )//creates file in internal memory
        longi=pref?.getString("longi","80.2707")!!
        latt=pref?.getString("lati","13.0827")!!

        val key = resources.getString(R.string.api_key)
        val request = OpenWeatherInterface.getInstance().getWeatherAll("https://api.openweathermap.org/data/2.5/onecall?lat=$latt&lon=$longi&units=metric&exclude=daily,current,minutely&appid=$key")

        request.enqueue(object :Callback<WeatherAll>{
            override fun onResponse(call: Call<WeatherAll>, response: Response<WeatherAll>) {
                if(response.isSuccessful) {
                    val hourly = response.body()!!
                    Log.d("Retrofit", "Fail : $hourly")
                    if (view is RecyclerView){
                        view.adapter = HourlyWeatherRecyclerViewAdapter(hourly.hourly)
                    }
                }
            }
            override fun onFailure(call: Call<WeatherAll>, t: Throwable) {
                Log.d("Retrofit","Fail : ${t.message}")
            }
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hourly_weather_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                HourlyWeatherFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}