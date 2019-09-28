package com.example.androidweather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidweather.R;
import com.example.androidweather.models.Datum__;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmList;

// Recyclerview adapter for the weekly forecast
public class WeeklyForecastAdapter extends RecyclerView.Adapter<WeeklyForecastAdapter.ViewHolder> {

    private RealmList<Datum__> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // Data is passed into the constructor for the adapter
    public WeeklyForecastAdapter(Context context, RealmList<Datum__> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // Inflates the row layout from weekly_forecast_view_item.xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.weekly_forecast_view_item, parent, false);
        return new ViewHolder(view);
    }

    // Binds the data to TextViews/ImageView for each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Datum__ forecast = mData.get(position);

        // Vars to hold forecast info
        String iconString = forecast.getIcon();
        String high = Double.toString(forecast.getTemperatureHigh());
        String low = Double.toString(forecast.getTemperatureLow());

        // Display day of the week (ie: Monday)
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date dateFormat = new java.util.Date(forecast.getTime() * 1000);
        String weekday = sdf.format(dateFormat);

        holder.forecastDay.setText(weekday);
        holder.forecastDayHigh.setText(high);
        holder.forecastDayLow.setText(low);

        // Display icon depending on weather
        // If no icons fit description, show alien
        if (iconString.equals("clear-day")){
            holder.forecastIcon.setImageResource(R.drawable.ic_wi_day_sunny);
        } else if (iconString.equals("clear-night")) {
            holder.forecastIcon.setImageResource(R.drawable.ic_wi_night_clear);
        } else if (iconString.equals("rain")) {
            holder.forecastIcon.setImageResource(R.drawable.ic_wi_rain);
        } else if (iconString.equals("snow")) {
            holder.forecastIcon.setImageResource(R.drawable.ic_wi_snow);
        } else if (iconString.equals("sleet")) {
            holder.forecastIcon.setImageResource(R.drawable.ic_wi_rain_mix);
        } else if (iconString.equals("wind")) {
            holder.forecastIcon.setImageResource(R.drawable.ic_wi_windy);
        } else if (iconString.equals("fog")) {
            holder.forecastIcon.setImageResource(R.drawable.ic_wi_fog);
        } else if (iconString.equals("cloudy")) {
            holder.forecastIcon.setImageResource(R.drawable.ic_wi_cloudy);
        } else if (iconString.equals("partly-cloudy-day")) {
            holder.forecastIcon.setImageResource(R.drawable.ic_wi_day_cloudy);
        } else if (iconString.equals("partly-cloudy-night")) {
            holder.forecastIcon.setImageResource(R.drawable.ic_wi_night_cloudy);
        } else {
            holder.forecastIcon.setImageResource(R.drawable.ic_wi_alien);
        }
    }

    // Returns total # of rows / size of data
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // Stores and recycles views as they are scrolled off the screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView forecastDay;
        TextView forecastDayHigh;
        TextView forecastDayLow;
        ImageView forecastIcon;

        ViewHolder(View itemView) {
            super(itemView);
            forecastDay = itemView.findViewById(R.id.forecast_day);
            forecastDayHigh = itemView.findViewById(R.id.forecast_high);
            forecastDayLow = itemView.findViewById(R.id.forecast_low);
            forecastIcon = itemView.findViewById(R.id.weatherIcon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // Convenience method for getting data at click position
    Datum__ getItem (int id) {
        return mData.get(id);
    }

    // Allows click events by the user to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // Parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}