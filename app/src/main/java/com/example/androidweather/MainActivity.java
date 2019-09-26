package com.example.androidweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.androidweather.model.Example;
import com.example.androidweather.services.RetrofitInstance;
import com.example.androidweather.services.WeatherService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    // TODO: store api key private
    // TODO: get user lat and long from GPS
    // TODO: daily forecast on bottom, in collection view
    // TODO: use weather icons
    // TODO: cache date in room or realm database, able to refresh data
    // TODO: readme
    // TODO: FUTURE: details of each day after selecting
    // TODO: FUTURE: user add locations

    private Double currTemp = 0.0;
    private Double latitude = 40.712776;
    private Double longitude = -74.005974;

    // DARK SKY API KEY IS STORED IN GRADLE
    //private String key = BuildConfig.DarkSkyKey;

    // FOR DEMO PURPOSES, DARK SKY API KEY IS INCLUDED IN MAINACTIVITY
    private String key = "2daafb9d2b0e6083c298882e99e100aa";

    private TextView tempView;
    private TextView weatherStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempView = findViewById(R.id.temp);
        weatherStatus = findViewById(R.id.status);

        getForecast();
    }

    public Object getForecast() {
        final WeatherService weatherService = RetrofitInstance.getWeatherService();
        Call<Example> call = weatherService.getWeatherData(key, latitude, longitude);

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                Example forecast = response.body();

                if (forecast != null && forecast.getCurrently() != null){
                    currTemp = forecast.getCurrently().getTemperature();
                    String tempString = Double.toString(currTemp);
                    tempView.setText(tempString+"°F");

                    weatherStatus.setText(forecast.getCurrently().getSummary());
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
