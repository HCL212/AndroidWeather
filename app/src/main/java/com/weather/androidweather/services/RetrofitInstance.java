package com.weather.androidweather.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Use Retrofit2 to access API
public class RetrofitInstance {

    private static Retrofit retrofit = null;
    private static String BASE_URL = "https://api.darksky.net/forecast/";

    public static WeatherService getWeatherService() {

        if (retrofit== null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(WeatherService.class);
    }
}
