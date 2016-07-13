package com.example.luthfi.androidweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Luthfi on 8/7/2016.
 */
public class ForecastAdapter extends ArrayAdapter<Weather> {
    private ArrayList<Weather> forecast;

    public ForecastAdapter(Context context, int resource) {
        super(context, resource);
        forecast = new ArrayList<Weather>();
    }

    public void setForecast(ArrayList<Weather> items) {
        this.forecast = items;
    }

    @Override
    public int getCount() {
        return forecast.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtains the weather.
        Weather weather = forecast.get(position);
        ViewHolder viewHolder;

        // If convertView is null, create new View by inflating it from the layout,
        // and create a new ViewHolder with convertView.
        // Else, it is a recycled view, so extract viewHolder accordingly.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Array for the days of the week.
        String[] daysOfTheWeek = new String[] {"Sunday", "Monday", "Tuesday", "Wednesday",
                "Thursday", "Friday", "Saturday"};

        // Convert timestamp to the day of the week.
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(weather.getTimestamp() * 1000);
        String dayText = daysOfTheWeek[cal.get(Calendar.DAY_OF_WEEK) - 1];

        // Set dayText for today and tomorrow.
        Calendar todayCalendar = Calendar.getInstance();
        String today = daysOfTheWeek[todayCalendar.get(Calendar.DAY_OF_WEEK) - 1];
        String tomorrow = daysOfTheWeek[todayCalendar.get(Calendar.DAY_OF_WEEK) % 7];

        if (dayText.compareTo(today) == 0) {
            dayText = "Today";
        } else if (dayText.compareTo(tomorrow) == 0) {
            dayText = "Tomorrow";
        }

        // Set all views in our ViewHolder.
        viewHolder.dayTextView.setText(dayText);
        viewHolder.weatherTextView.setText(weather.getWeather());
        viewHolder.highTextView.setText(String.valueOf(weather.getHigh()));
        viewHolder.lowTextView.setText(String.valueOf(weather.getLow()));

        // Return the list item.
        return convertView;
    }

    // This ViewHolder will hold all of our views that are inside the list item.
    private static class ViewHolder {
        public TextView dayTextView, weatherTextView, highTextView, lowTextView;

        public ViewHolder(View listItem) {
            dayTextView = (TextView) listItem.findViewById(R.id.dayTextView);
            weatherTextView = (TextView) listItem.findViewById(R.id.weatherTextView);
            highTextView = (TextView) listItem.findViewById(R.id.highTextView);
            lowTextView = (TextView) listItem.findViewById(R.id.lowTextView);
        }

    }

}
