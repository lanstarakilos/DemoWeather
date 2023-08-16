package com.example.demoweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.lvWeather);
        client = new AsyncHttpClient();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // creates a new empty Array List of Weather objects with the variable name alWeather
        ArrayList<Weather> alWeather = new ArrayList<Weather>();
        // will connect it to the URL where the weather data is located.
        client.get("https://api.data.gov.sg/v1/environment/2-hour-weather-forecast", new JsonHttpResponseHandler() {
            String area;
            String forecast;

            @Override
            //Once a response is successfully received by the app from the site, all the code within here is triggered
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray jsonArrItems = response.getJSONArray("items");
                    // The “forecasts” is found in the very first (index 0) JSON Object of “items”. Therefore, the first line above is used to reference this first object
                    JSONObject firstObj = jsonArrItems.getJSONObject(0);
                    // used to reference the “forecasts” JSON Array.
                    JSONArray jsonArrForecasts = firstObj.getJSONArray("forecasts");
                    for (int i = 0; i < jsonArrForecasts.length(); i++) {
                        JSONObject jsonObjForecast = jsonArrForecasts.getJSONObject(i);
                        area = jsonObjForecast.getString("area");
                        forecast = jsonObjForecast.getString("forecast");
                        Weather weather = new Weather(area, forecast);
                        alWeather.add(weather);
                    }
                } catch (JSONException e) {
                }

                //POINT X – Code to display List View
                ArrayAdapter<Weather> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, alWeather);

                // Set the ArrayAdapter to the ListView
                lv.setAdapter(adapter);
            }//end onSuccess
        });
    }//end onResume
}



