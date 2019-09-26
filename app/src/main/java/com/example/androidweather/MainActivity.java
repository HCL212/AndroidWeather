package com.example.androidweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.androidweather.model.Example;
import com.example.androidweather.services.RetrofitInstance;
import com.example.androidweather.services.WeatherService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private Double currTemp = 0.0;
    private Double latitude = 40.712776;
    private Double longitude = -74.005974;
    private String key = "2daafb9d2b0e6083c298882e99e100aa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getForecast();
    }

    public Object getForecast() {
        WeatherService weatherService = RetrofitInstance.getWeatherService();
        Call<Example> call = weatherService.getWeatherData(key, latitude, longitude);

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                Example forecast = response.body();

                if (forecast != null && forecast.getCurrently() != null){
                    currTemp = forecast.getCurrently().getTemperature();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                System.out.println(t);
            }
        });
        return currTemp;
    }
}
