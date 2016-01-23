package com.ninjawebzen.androidweather;

import android.os.AsyncTask;

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
 * Created by slavkurochkin on 1/12/16.
 */
public class RetrieveForecastTask extends AsyncTask<Double, Void, ArrayList<Weather>> {
    private ForecastAdapter adapter;
    public RetrieveForecastTask(ForecastAdapter adapter){
        this.adapter = adapter;
    }
    @Override
    protected ArrayList<Weather> doInBackground(Double... params) {
        ArrayList<Weather> list = new ArrayList<Weather>();
        try {
            // URL for OpenWeatherMap and parameters
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?lat="
                    + params[0]
                    + "&lon="
                    + params[1]
                    + "&units=metric&cnt=7"
            +"&appid=your_api_key");

            // Create a connection and connect
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            // If the response code is ok, parse the data and finish
            int response = connection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                String json = convertStreamToString(is);
                is.close();
                list.addAll(parseJSON(json));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<Weather> weathers){
        super.onPostExecute(weathers);
        adapter.setForecast(weathers);
        adapter.notifyDataSetChanged();
    }

    // Helper method to parse the JSON that OpenWeatherMap returns
    private ArrayList<Weather> parseJSON(String json) throws JSONException {
        ArrayList<Weather> forecast = new ArrayList<Weather>();
        JSONArray jsonArray = new JSONObject(json).getJSONArray("list");
        for (int i = 0; i < jsonArray.length(); i++) {
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

    // Helper method to convert the output from OpenWeatherMap to a String
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
