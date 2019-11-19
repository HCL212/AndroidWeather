package com.weather.androidweather.services;

import com.weather.androidweather.models.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

// Use retrofit2 to access dark sky API with API key, latitude, and longitude
public interface WeatherService {

    @GET("{api_key}/{latitude},{longitude}")
    Call<Example> getWeatherData(
            @Path("api_key") String apiKey,
            @Path("latitude") Double latitude,
            @Path("longitude") Double longitude
    );
}
