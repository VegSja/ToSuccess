package com.example.tosuccess;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import java.util.List;

public class API_Connection {

    public interface VolleyCallBack{
        void onSuccess(String response);
        void onError(String errorMessage);
    }

    String url = "http://62.16.199.208:3690/activities";

    Context appContext;

    String requestResponse;


    public API_Connection(Context appContext){
        this.appContext = appContext;
    }


    public void getRequest(final VolleyCallBack callBack){
        RequestQueue queue = Volley.newRequestQueue(this.appContext);

        //Request json response form the URL
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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

    public String getRequestResponse(){
        return requestResponse;
    }

}
