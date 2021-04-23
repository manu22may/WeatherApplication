package com.capgemini.weatherapp

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface OpenWeatherInterface {

    @GET
    fun getWeatherAll(@Url url:String): Call<WeatherAll>

    companion object{

        val BASE_URL="https://api.openweathermap.org/"

        fun getInstance():OpenWeatherInterface{
            val builder =Retrofit.Builder()
            builder.addConverterFactory(GsonConverterFactory.create())
            builder.baseUrl(BASE_URL)
            val retrofit =builder.build()
            return retrofit.create(OpenWeatherInterface::class.java)

        }

    }
}