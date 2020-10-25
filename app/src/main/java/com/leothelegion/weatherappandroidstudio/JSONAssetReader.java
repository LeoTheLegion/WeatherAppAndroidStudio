package com.leothelegion.weatherappandroidstudio;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JSONAssetReader {
    public static JSONObject getJsonObjectFromFile(String fileName,final Activity act)
            throws JSONException, IOException {
        String json ="";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(act.getAssets().open(fileName)));
            String line;
            while ((line = reader.readLine()) != null) {
                json += line;
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw e;
                }
            }
            try{
                return new JSONObject(json);
            }
            catch (JSONException e){
                String ARRAYTOOBJECT = "{\"value\": ["+json+"]}";
                return new JSONObject(ARRAYTOOBJECT);
            }
        }
    }
}
