package com.ninjawebzen.androidweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by slavkurochkin on 1/12/16.
 */
public class ForecastAdapter extends ArrayAdapter<Weather>{

    private ArrayList<Weather>forecast;

    public ForecastAdapter(Context context, int resource) {
        super(context, resource);
        forecast = new ArrayList<Weather>();
    }

    public void setForecast(ArrayList<Weather> items){
        this.forecast = items;
    }

    @Override
    public int getCount(){
        return forecast.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Weather weather = forecast.get(position);
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        String[] daysOfTheWeek = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(weather.getTimestamp()*1000);
        String dayText = daysOfTheWeek[cal.get(Calendar.DAY_OF_WEEK) - 1];

        viewHolder.dayTextView.setText(dayText);
        viewHolder.weatherTextView.setText(weather.getWeather());
        viewHolder.highTextView.setText(String.valueOf(weather.getHigh()));
        viewHolder.lowTextView.setText(String.valueOf(weather.getLow()));
        return convertView;
    }

    private static class ViewHolder {
        public TextView dayTextView, weatherTextView, highTextView, lowTextView;
        public ViewHolder(View listItem){
            dayTextView = (TextView) listItem.findViewById(R.id.dayTextView);
            weatherTextView = (TextView) listItem.findViewById(R.id.weatherTextView);
            highTextView = (TextView) listItem.findViewById(R.id.highTextView);
            lowTextView = (TextView) listItem.findViewById(R.id.lowTextView);

        }
    }
}
