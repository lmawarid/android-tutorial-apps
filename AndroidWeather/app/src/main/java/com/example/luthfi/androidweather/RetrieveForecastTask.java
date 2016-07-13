package com.example.luthfi.androidweather;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Luthfi on 8/7/2016.
 */
public class RetrieveForecastTask extends AsyncTask<Double, Void, ArrayList<Weather>> {
    private ForecastAdapter adapter;
    private static final String API_KEY = "505e79d9eeab6508bdf608c04b693fc3";

    public RetrieveForecastTask(ForecastAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected ArrayList<Weather> doInBackground(Double... params) {
        ArrayList<Weather> list = new ArrayList<Weather>();
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?lat="
                    + params[0]
                    + "&lon="
                    + params[1]
                    + "&units=metric&cnt=7&appid="
                    + API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            int response = connection.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
                Log.i("HTTP Response", "Connection was successful");

                // If we get a 200, retrieve the output of the URL as JSON.
                InputStream is = connection.getInputStream();
                String json = convertStreamToString(is);

                // Close the connection stream to stop hogging bandwidth and memory,
                // parse the returned JSON.
                is.close();
                list = parseJSON(json);
            } else {
                String error = connection.getResponseMessage();
                int errorCode = connection.getResponseCode();
                Log.e("Response Code", String.valueOf(errorCode));
                Log.e("Error Message", error);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<Weather> weathers) {
        super.onPostExecute(weathers);
        adapter.setForecast(weathers);
        adapter.notifyDataSetChanged();
    }

    private ArrayList<Weather> parseJSON(String json) throws JSONException {
        ArrayList<Weather> forecast = new ArrayList<Weather>();
        JSONArray jsonArray = new JSONObject(json).getJSONArray("list");

        for (int i = 0; i < jsonArray.length(); ++i) {
            Weather weather = new Weather();
            JSONObject jsonDay = jsonArray.getJSONObject(i);
            weather.setTimestamp(jsonDay.getInt("dt"));
            weather.setHigh(jsonDay.getJSONObject("temp").getDouble("max"));
            weather.setLow(jsonDay.getJSONObject("temp").getDouble("min"));

            JSONObject jsonWeather = jsonDay.getJSONArray("weather").getJSONObject(0);
            weather.setWeather(jsonWeather.getString("main"));
            forecast.add(weather);
        }

        return forecast;
    }

    private String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return builder.toString();
    }
}
