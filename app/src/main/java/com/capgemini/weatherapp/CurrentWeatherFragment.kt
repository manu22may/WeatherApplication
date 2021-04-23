package com.capgemini.weatherapp

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_current_weather.*
import kotlinx.android.synthetic.main.fragment_current_weather.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CurrentWeatherFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val request = OpenWeatherInterface.getInstance().getWeatherAll("https://api.openweathermap.org/data/2.5/onecall?lat=$latt&lon=$longi&units=metric&exclude=hourly,daily,minutely&appid=$key")

        progressBar.visibility=View.VISIBLE
        request.enqueue(object : Callback<WeatherAll> {
            override fun onResponse(call: Call<WeatherAll>, response: Response<WeatherAll>) {
                Log.d("Retrofit","Success ${response.body()}")
                if(response.isSuccessful) {
                    val addr = getMyAddress()
                    val weather = response.body()?.current
                    view.currentDateT.text="${Date(weather?.dt?.toLong()!!*1000).toString()}\n$addr"
                    val calendar = Calendar.getInstance()
                    val h=calendar.get(Calendar.HOUR_OF_DAY)
                    when(h){//backgrounnd color
                        in 5..12->currentParent.setBackgroundColor(resources.getColor(R.color.morning))
                        in 12..16->currentParent.setBackgroundColor(resources.getColor(R.color.noon))
                        in 16..18->currentParent.setBackgroundColor(resources.getColor(R.color.evening))
                        in 18..23->currentParent.setBackgroundColor(resources.getColor(R.color.night))
                        in 0..5 ->currentParent.setBackgroundColor(resources.getColor(R.color.night))
                    }

                    val sunrise =Calendar.getInstance()
                    sunrise.timeInMillis = weather.sunrise.toLong()*1000
                    view.currentSunriseT.text=convertToTime(sunrise)
                    val sunset =Calendar.getInstance()
                    sunset.timeInMillis = weather.sunset.toLong()*1000
                    view.currentSunsetT.text=convertToTime(sunset)

                    view.currentTempT.text = "${weather.temp.toInt()} °C"
                    view.currentHumT.text = "${weather.humidity}%"
                    view.currentSpeedT.text = "${weather.wind_speed} m/s"

                    val imageUrl = "https://openweathermap.org/img/wn/${weather.weather[0].icon}.png"
                    Glide.with(view).load(Uri.parse(imageUrl)).into(view.currentWeatherImage)
                    view.currentMainT.text = "${weather.weather[0].main},\n${weather.weather[0].description} "

                    view.progressBar.visibility=View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<WeatherAll>, t: Throwable) {
                Log.d("Retrofit","Fail : ${t.message}")
            }
        })

    }

    private fun getMyAddress(): String {
        var addressList= mutableListOf<Address>()
        activity?.apply {
            val gCoder= Geocoder(this)
            addressList=gCoder.getFromLocation(latt.toDouble(),longi.toDouble(),1)
        }
        if(addressList.isNotEmpty()){
            val addr= addressList[0] //Address class
            return "${addr.locality} , ${addr.countryName}"
        }
        else return ""
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
        return time+" $end"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_current_weather, container, false)
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                CurrentWeatherFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}