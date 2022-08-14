package com.crosshatch.weathertoday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    final String API_KEY = "a62f19204db4271d56c8d5f2dc4c0dc3";
    TextView cityNameView, temperature, weatherDescription;
    SearchView searchBox;
    RequestQueue mQueue;
    String cityName, url, urlTemplate, units;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityNameView = findViewById(R.id.city);
        temperature = findViewById(R.id.temperature);
        weatherDescription = findViewById(R.id.description_weather);
        searchBox = findViewById(R.id.search_box);

        mQueue = Volley.newRequestQueue(this);

        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getWeatherData(query);
                Log.i("mine", "onQueryTextSubmit: " + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Toast.makeText(getApplicationContext(), newText, Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    private void getWeatherData(String query) {
        cityName = query;
        units = "metric";
        url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=" + units + "&appid=" + API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "getWeatherData " + query, Toast.LENGTH_LONG).show();
                        try {
                            JSONArray weatherResponse = response.getJSONArray("weather");
                            JSONObject weatherArray = weatherResponse.getJSONObject(0);
                            String description = weatherArray.getString("description");
                            Toast.makeText(getApplicationContext(), description, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }
}