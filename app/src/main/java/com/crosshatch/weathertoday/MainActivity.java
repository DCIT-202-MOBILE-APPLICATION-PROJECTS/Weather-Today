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
    String url, urlTemplate, units;

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
                jsonParse(query);
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

    private void jsonParse(String query){
        String units = "metric";
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+query+"&appid="+API_KEY+"&units="+units;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Getting the array i want from the JSON
                    //For temperature
                    JSONObject jsonobj = response.getJSONObject("main");
                    int temperatureASInt = (int) jsonobj.getDouble("temp");
                    temperature.setText(temperatureASInt + getString(R.string.degree_symbol));
                    Log.i("mine", "temp: "+temperatureASInt);

                    //For Description
                    JSONArray weather = response.getJSONArray("weather");
                    JSONObject weatherObj = weather.getJSONObject(0);
                    String desc = weatherObj.getString("description");
                    weatherDescription.setText(desc);
                    //For City name
                    //JSONArray cname = response.getJSONArray("name");
                    String name = (String) response.getString("name");
                    cityNameView.setText(name);
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
        mQueue.add(request);
    }
}