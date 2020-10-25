package com.leothelegion.weatherappandroidstudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        JSONObject clouds = json.getJSONObject("clouds");
        JSONObject sys = json.getJSONObject("sys");


        ((TextView) findViewById(R.id.location)).setText(
                json.getString("name") + "," + sys.getString("country")
        );
        ((TextView) findViewById(R.id.Temp)).setText(
                "Current Temp : "+main.getString("temp")
        );
        ((TextView) findViewById(R.id.Temp_Feels_Like)).setText(
                "Feels Like : "+main.getString("feels_like")
        );
        ((TextView) findViewById(R.id.Temp_Low)).setText(
                "Low Temp : " + main.getString("temp_min")
        );
        ((TextView) findViewById(R.id.Temp_High)).setText(
                "High Temp : " + main.getString("temp_max")
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
    }

    void createAlert(String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(message);
        alert.setPositiveButton("Ok",null);
        alert.show();
    }

}
