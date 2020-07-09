package com.example.tosuccess;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class API_Connection {

    public interface VolleyGetCallBack{
        void onSuccess(String response);
        void onError(String errorMessage);
    }
    public interface VolleyPushCallBack{
        void onSuccess(String response);
        void onError(String errorMessage);
    }

    String url = "http://62.16.199.208:3690/activities";

    Context appContext;

    String requestResponse;


    public API_Connection(Context appContext){
        this.appContext = appContext;
    }


    public void getRequest(final VolleyGetCallBack callBack){
        RequestQueue queue = Volley.newRequestQueue(this.appContext);

        //Request json response form the URL
        StringRequest jsonRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Response: " + response.toString());
                requestResponse = response.toString();
                callBack.onSuccess(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR!!!!: " + error.toString());
                callBack.onError(error.toString());
            }
        });
        queue.add(jsonRequest);
    }

    public void postRequest(final VolleyPushCallBack callBack){
        RequestQueue queue = Volley.newRequestQueue(this.appContext);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.onSuccess(response);
                Log.d("Response", response);
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onError(error.toString());
                Log.d("Error.response", error.toString());
            }
        }
    ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("activity_name", "activity_name1");
                params.put("minutes_after_midnight", "10");
                return params;
            }
        };
        queue.add(postRequest);
    }

    public String getRequestResponse(){
        return requestResponse;
    }

}
