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

        if(isLatLong(input)){
            String[] a = input.split(",");
            return "lat=" + a[0] + "&lon=" + a[1];
        }

        if(isZipcode(input)){
            return "zip="+ input + ",us";
        }

        return "q="+ input;
    }

    private static boolean isZipcode(String input){
        try{
            Integer.parseInt(input);
        }catch (NumberFormatException e){
            return false;
        }

        if(input.length() != 5)
            return false;

        return true;
    }

    private static boolean isLatLong(String input){
        String[] a = input.split(",");
        if(a.length != 2)
            return false;

        try{
            Float.parseFloat(a[0]);
        }catch (NumberFormatException e){
            return false;
        }

        try{
            Float.parseFloat(a[1]);
        }catch (NumberFormatException e){
            return false;
        }

        return true;
    }

}
