package com.nahuel.weatherapp.service

import com.nahuel.weatherapp.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.openweathermap.org/data/2.5/weather?q=bingöl&appid=72f167b8e0bc363f54f7afd688040025
interface WeatherAPI {

    @GET("data/2.5/weather?appid=72f167b8e0bc363f54f7afd688040025&units=metric")
    fun getData(
        @Query("q") cityName: String
    ): Single<WeatherModel>

}