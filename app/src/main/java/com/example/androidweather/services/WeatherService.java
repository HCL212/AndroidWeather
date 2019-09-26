package com.example.androidweather.services;

import com.example.androidweather.R;
import com.example.androidweather.model.Example;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetWeatherService {

    @GET("R.string.dark_sky_key,{latitude},{longitude}")
    Call<Example> getWeatherData();
}
