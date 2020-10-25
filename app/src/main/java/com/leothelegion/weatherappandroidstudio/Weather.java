package com.leothelegion.weatherappandroidstudio;

import android.app.Activity;

public class Weather {

    static final String APIROOTURL = "https://api.openweathermap.org/data/2.5/weather?";
    static final String APIENDURL = "&appid=05d6b4bb7a9ea7a316223f7e9bedb3cf";


    public static void getWeather(String input, Activity activity,final SimpleRequest.Response callback){
        SimpleRequest request = new SimpleRequest(activity);

        String url = APIROOTURL + GenerateQuery(input) + APIENDURL;

        request.sendRequest(SimpleRequest.Method.GET,url,callback);
    }

    private static String GenerateQuery(String input) {
        return "q="+ input;
    }



}
