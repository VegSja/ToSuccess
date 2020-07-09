package com.example.tosuccess;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
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
import java.util.Random;

public class API_Connection {

    public interface VolleyGetCallBack{
        void onSuccess(String response);
        void onError(String errorMessage);
    }
    public interface VolleyPushCallBack{
        void onSuccess(String response);
        void onError(String errorMessage);
    }

    String url = "http://62.16.199.208:3690/activities/";

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
                callBack.onError(error.toString());
            }
        });
        queue.add(jsonRequest);
    }

    public void postRequest(String name, Integer minutes_after_midnight, Integer dayNumber, final VolleyPushCallBack callBack){
        JSONObject jsonobj;
        jsonobj = new JSONObject();
        try{
            //Adding some keys
            jsonobj.put("id", 1);
            jsonobj.put("activity_name", name);
            jsonobj.put("minutes_after_midnight", minutes_after_midnight);
            jsonobj.put("date", dayNumber);
            System.out.println("JSON Object to send: " + jsonobj.toString());
        }catch (JSONException e){
            System.out.println("Error occured while building JSON!");
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this.appContext);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonobj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.onSuccess(response.toString());
                Log.d("Response", response.toString());
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onError(error.toString());
                Log.d("Error.response", error.toString());
            }
        });
        queue.add(postRequest);
    }

    public String getRequestResponse(){
        return requestResponse;
    }

}
