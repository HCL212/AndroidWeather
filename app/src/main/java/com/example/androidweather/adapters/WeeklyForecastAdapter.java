package com.example.androidweather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidweather.R;
import com.example.androidweather.models.Datum__;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Long.getLong;

public class WeeklyForecastAdapter extends RecyclerView.Adapter<WeeklyForecastAdapter.ViewHolder> {

    private ArrayList<Datum__> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    //@brief: Data is passed into the constructor for the adapter
    //@params: [Context context] [List<String> data]
    public WeeklyForecastAdapter(Context context, ArrayList<Datum__> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    //@brief: Inflates the row layout from view_members_item.xml when needed
    //@params: [ViewGroup parents] [int viewType]
    //@pre condition: Rows not inflated inside the view
    //@post condition: Rows inflated inside the view
    //@return: ViewHolder with inflated views
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.weekly_forecast_view_item, parent, false);
        return new ViewHolder(view);
    }

    //@brief: Binds the data to TextView for each row
    //@pre condition: Items in the view not binded to the view
    //@post condition: Items binded to the view
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Datum__ forecast = mData.get(position);

        //String day = Double.toString(forecast.getTime());
        String high = Double.toString(forecast.getTemperatureHigh());
        String low = Double.toString(forecast.getTemperatureLow());

        long dateInMilli = 86400000/forecast.getTime();
        DateFormat convert = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        Date result = new Date(dateInMilli);

        holder.forecastDay.setText(convert.format(result));
        holder.forecastDayHigh.setText(high);
        holder.forecastDayLow.setText(low);
    }

    //@brief: Returns total number of rows
    //@return: int of total number of items for the view
    @Override
    public int getItemCount() {
        return mData.size();
    }

    //@brief: Stores and recycles views as they are scrolled off the screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView forecastDay;
        TextView forecastDayHigh;
        TextView forecastDayLow;

        ViewHolder(View itemView) {
            super(itemView);
            forecastDay = itemView.findViewById(R.id.forecast_day);
            forecastDayHigh = itemView.findViewById(R.id.forecast_high);
            forecastDayLow = itemView.findViewById(R.id.forecast_low);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Datum__ getItem (int id) {
        return mData.get(id);
    }

    //@brief: Allows click events by the user to be caught
    //@params: [ItemClickListener itemClickListener]
    //@pre condition: User has not clicked anything
    //@post condition: When user clicks something in the view, it is registered and action is taken
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}