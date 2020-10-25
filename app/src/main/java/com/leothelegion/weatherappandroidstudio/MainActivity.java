package com.leothelegion.weatherappandroidstudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double lat;
    private double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, getOptions());
        AutoCompleteTextView input = (AutoCompleteTextView)
                findViewById(R.id.input);
        input.setAdapter(adapter);
    }

    public void GetWeather(View view){
        EditText inputField = (EditText) findViewById(R.id.input);
        Weather.getWeather(inputField.getText().toString(),this,
                new SimpleRequest.Response(){

                    @Override
                    public void onSuccess(String response)  {
                        //createAlert("Response is: "+ response);
                        try {
                            updateWeatherPage(new JSONObject(response));
                        }
                        catch (JSONException e){
                            createAlert("Something is wrong with the response :(");
                        }

                    }

                    @Override
                    public void onError(String error) {
                        createAlert("Failed: " + error);
                    }
                }
        );

    }

    private void updateWeatherPage(JSONObject json) throws JSONException {
        JSONObject coord = json.getJSONObject("coord");
        JSONArray weather = json.getJSONArray("weather");
        JSONObject main = json.getJSONObject("main");
        JSONObject wind = json.getJSONObject("wind");
        JSONObject sys = json.getJSONObject("sys");

        double temp = K_to_F(main.getDouble("temp"));
        double temp_feelsLike = K_to_F(main.getDouble("feels_like"));
        double temp_L = K_to_F(main.getDouble("temp_min"));
        double temp_H = K_to_F(main.getDouble("temp_max"));

        ((TextView) findViewById(R.id.location)).setText(
                json.getString("name") + "," + sys.getString("country")
        );
        ((TextView) findViewById(R.id.Temp)).setText(
                "Current Temp : "+ (int)temp + "F"
        );
        ((TextView) findViewById(R.id.Temp_Feels_Like)).setText(
                "Feels Like : "+(int)temp_feelsLike + "F"
        );
        ((TextView) findViewById(R.id.Temp_Low)).setText(
                "Low Temp : " + (int)temp_L + "F"
        );
        ((TextView) findViewById(R.id.Temp_High)).setText(
                "High Temp : " + (int)temp_H + "F"
        );
        ((TextView) findViewById(R.id.humidity)).setText(
                "Humidity : " + main.getString("humidity")
        );
        ((TextView) findViewById(R.id.WindDeg)).setText(
                "Wind Direction : " + wind.getString("deg")
        );
        ((TextView) findViewById(R.id.WindSpeed)).setText(
                "Wind Speed : " + wind.getString("speed")
        );
        ((TextView) findViewById(R.id.cloudness)).setText(
                weather.getJSONObject(0).getString("description")
        );

        lat = coord.getDouble("lat");
        lon = coord.getDouble("lon");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng point = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(point).title("Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
    }

    String[] getOptions(){
        String[] options = null;
        try {
            JSONObject reader = JSONAssetReader.getJsonObjectFromFile(
                    "world-cities_json.json",this);
            JSONArray worldcities = reader.getJSONArray("value").getJSONArray(0);

            options = new String[worldcities.length()];

            for (int i = 0; i < worldcities.length();i++){
                JSONObject city = worldcities.getJSONObject(i);
                options[i] = (city.getString("name") + "," + city.getString("country"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return options;
    }


    ///////////////////////HELPER FUNCTIONS///////////////////////
    double K_to_F(double k){
        double a = k - 273.15;
        double b = a * 9 / 5;
        double c = b + 32;
        return c;
    }

    void createAlert(String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(message);
        alert.setPositiveButton("Ok",null);
        alert.show();
    }


}
