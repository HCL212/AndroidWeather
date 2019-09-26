package com.example.androidweather.services;

import com.example.androidweather.R;
import com.example.androidweather.model.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WeatherService {

    @GET("{api_key}/{latitude},{longitude}")
    Call<Example> getWeatherData(
            @Path("api_key") String apiKey,
            @Path("latitude") Double latitude,
            @Path("longitude") Double longitude
    );
}
