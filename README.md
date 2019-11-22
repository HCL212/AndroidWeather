# AndroidWeather

AndroidWeather is a weather application that shows the current weather and weekly forecast based on the user's GPS location.  The application uses the Dark Sky API for data.  

## Screenshot

![image](https://raw.githubusercontent.com/hcl212/AndroidWeather/master/screenshot.png)

## Getting Started

Download zip file and import project into Android Studio.

### Prerequisites
Android Phone  
 - [Google Play Store](https://play.google.com/store/apps/details?id=com.weather.androidweather)  
  
Android Studio  
--GPS MUST be enabled in the emulator

## Running the tests

Inside the emulator within Android Studio, set location to different latitudes and longitudes using the options.  Weather data and location will change accordingly.  
Refresh button will pull data from Dark Sky again and repopulate the weather information.

## Built With

* [Dark Sky API](https://darksky.net/dev) - The API used for weather data
* [Retrofit2](https://square.github.io/retrofit/) - HTTP client
* [Android Studio](https://developer.android.com/studio) - Android development tools
* [Realm](https://realm.io/) - Mobile database (cache weather data)

## Authors

* **Hugh L**

## Future Tasks

* Let user add specific locations
* Show weather details of a day the user selects
* Show user hourly weather
* Animated icons

## Drawback/Tradeoffs

* Wanted to use reverse geocoding for user location but there is high latency, not good for testing, but usable in real world scenario

