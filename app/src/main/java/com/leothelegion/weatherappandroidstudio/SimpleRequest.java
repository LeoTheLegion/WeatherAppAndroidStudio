package com.leothelegion.weatherappandroidstudio;
import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


public class SimpleRequest {

    private RequestQueue queue;

    public SimpleRequest(final Activity act){
        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(act);
    }

    public void sendRequest(Method method, String url, String tag, final Response callback){

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(method.ordinal(), url,
            new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                    callback.onSuccess(response);
                }
            },
            new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onError(error.getMessage());
                }
            }
        );
        stringRequest.setTag(tag);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void sendRequest(Method method,String url, Response callback){
        sendRequest(method,url,null,callback);
    }

    public void cancelRequest(String tag){
        queue.cancelAll(tag);
    }



    public enum Method{
        GET,
        POST
    }

    public interface Response{
        public void onSuccess(String response);
        public  void onError(String error);
    }
}
