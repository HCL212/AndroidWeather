package com.weather.androidweather.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.weather.androidweather.R;
import com.weather.androidweather.adapters.WeeklyForecastAdapter;
import com.weather.androidweather.models.Datum__;
import com.weather.androidweather.models.Example;
import com.weather.androidweather.services.RetrofitInstance;
import com.weather.androidweather.services.WeatherService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationListener, WeeklyForecastAdapter.ItemClickListener {

    // TODO: FUTURE: details of each day after selecting (show in fragment)
    // TODO: FUTURE: user add locations

    // DARK SKY API KEY IS STORED IN GRADLE
    //private String key = BuildConfig.DarkSkyKey;

    // FOR DEMO PURPOSES, DARK SKY API KEY IS INCLUDED IN MAINACTIVITY
    private String key = "2daafb9d2b0e6083c298882e99e100aa";

    private LocationManager locationManager;
    private String provider;

    private RealmList<Datum__> weeklyData = new RealmList<>();
    private RecyclerView recyclerView;
    private WeeklyForecastAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Double currTemp;
    private Double latitude;
    private Double longitude;

    private ImageButton refreshButton;
    private TextView locationName;
    private TextView latitudeCoords;
    private TextView longitudeCoords;
    private TextView tempView;
    private TextView weatherStatus;
    private TextView darkSky;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Used to cache data in realm
        // Initialize
        Realm.init(this);

        refreshButton = findViewById(R.id.refresh_button);
        locationName = findViewById(R.id.address);
        latitudeCoords = findViewById(R.id.latitude);
        longitudeCoords = findViewById(R.id.longitude);
        tempView = findViewById(R.id.temp);
        weatherStatus = findViewById(R.id.status);
        darkSky = findViewById(R.id.darksky);
        recyclerView = findViewById(R.id.forecast_recycler_view);

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // Use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Specify an adapter
        mAdapter = new WeeklyForecastAdapter(this, weeklyData);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);

        // Recyclerview dividers
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        // Request permissions during runtime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            getForecast();
        }

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            System.out.println("Location unavailable");
        }

        // Refresh button to refresh data from API
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast();
            }
        });

        // Dark sky link
        darkSky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent browser= new Intent(Intent.ACTION_VIEW, Uri.parse("https://darksky.net/poweredby/"));
                startActivity(browser);
            }
        });

    }

    // Request location updates while app is open
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    // Stop location updates when app is not in foreground/in use
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    // When location changes, update display information
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        String latString = Double.toString(latitude);
        String longString = Double.toString(longitude);

        latitudeCoords.setText("Latitude: " + latString);
        longitudeCoords.setText("Longitude: " + longString);

        getForecast();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000:{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getForecast();
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    // Interface function from WeeklyForecastAdapter.java
    @Override
    public void onItemClick(View view, int position) {
        // Do nothing for now
    }

    // Main function to make request to Dark Sky API for forecast information
    // After response, populate relevant data on the screen
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

                    // Clear RealmList<Datum__> weeklyData and repopulate with new information
                    // Notify adapter of changes so it can update recyclerview
                    weeklyData.clear();
                    weeklyData.addAll(forecast.getDaily().getData());
                    mAdapter.notifyDataSetChanged();

                    locationName.setText(forecast.getTimezone());
                    tempView.setText(tempString+"°F");
                    weatherStatus.setText(forecast.getCurrently().getSummary());

                    // Cache data in realm database
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(forecast);
                    realm.commitTransaction();
                    realm.close();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                System.out.println(t);
            }
        });
        return 0;
    }

    // Geocoding too slow, UNUSED for now
    // Get name of location to display to user
    private String hereLocation(double lat, double lon){
        String cityName = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses.size() > 0){
                for (Address adr:addresses){
                    if (adr.getLocality() != null && adr.getLocality().length() > 0){
                        cityName = adr.getLocality();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityName;
    }

}
