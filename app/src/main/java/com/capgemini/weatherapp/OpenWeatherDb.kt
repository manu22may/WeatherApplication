package com.capgemini.weatherapp

data class CurrentWeather(
    val temp :Double,
    val humidity:Int,
    val wind_speed:Double,
    val dt :Int,
    val sunrise:Int,
    val sunset :Int,
    val weather:List<dailyweatherdetails>
)

data class WeatherAll(
    val lat:Double,
    val lon:Double,
    val timezone:String,
    val current: CurrentWeather,
    val daily: List<DailyWeather>
)

data class DailyWeather(
        val dt :Int,
        val temp:dailytemp,
        val weather:List<dailyweatherdetails>
)
data class dailyweatherdetails(
        val main:String,
        val description:String,
        val icon:String,
)
data class dailytemp(
        val min :Double,
        val max :Double
)