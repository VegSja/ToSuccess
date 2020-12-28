package com.example.tosuccess;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
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
    public interface VolleyDeleteCallBack{
        void onSuccess(String response);
        void onError(String errorMessage);
    }
    public interface VolleyLoginCallBack{
        void onSuccess(String response);
        void onError(String errorMessage);
    }



    String url = "http://vegsja.pythonanywhere.com/activities/";
    String loginUrl = "http://vegsja.pythonanywhere.com/google/";

    Context appContext;

    String requestResponse;

    String backendAccessToken;

    Logger logger = new Logger();


    public API_Connection(Context appContext){
        this.appContext = appContext;
    }


    public void getRequest(final String accessToken, final VolleyGetCallBack callBack){
        RequestQueue queue = Volley.newRequestQueue(this.appContext);

        //Request json response form the URL
        StringRequest jsonRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                logger.loggerMessage("Response: " + response.toString());
                requestResponse = response.toString();
                callBack.onSuccess(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onError(error.toString());
            }
        }
    ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String,String> params = new HashMap<String, String>();
                params.put("Authorization", " Bearer " + accessToken);
                return params;
            }
        };
        queue.add(jsonRequest);
        logger.statusMessage("Sending request to GET from server: " + jsonRequest.toString());
    }

    public void postRequest(final String accessToken, String name, Integer minutes_after_midnight_start, Integer minutes_after_midnight_end, Integer dayNumber, String date_string, final VolleyPushCallBack callBack){
        JSONObject jsonobj;
        jsonobj = new JSONObject();
        try{
            //Adding some keys
            jsonobj.put("id", 1);
            jsonobj.put("activity_name", name);
            jsonobj.put("minutes_after_midnight_start", minutes_after_midnight_start);
            jsonobj.put("minutes_after_midnight_end", minutes_after_midnight_end);
            jsonobj.put("date", dayNumber);
            jsonobj.put("date_string", date_string);
        }catch (JSONException e){
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this.appContext);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonobj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.onSuccess(response.toString());
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onError(error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String,String> params = new HashMap<String, String>();
                params.put("Authorization", " Bearer " + accessToken);
                return params;
            }
        };
        logger.statusMessage("Sending request to POST from server: " + postRequest.toString());
        queue.add(postRequest);
    }
    
    public void deleteRequest(final String accessToken, String name, final VolleyDeleteCallBack callBack){
        String deleteUrl = url.concat(name + '/');
        RequestQueue queue = Volley.newRequestQueue(this.appContext);
        StringRequest dr = new StringRequest(Request.Method.DELETE, deleteUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        logger.loggerMessage("Delete response: " + response);
                        callBack.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        logger.errorMessage("Delete error " + error.toString());
                        callBack.onSuccess(error.toString());
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError{
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("Authorization", " Bearer " + accessToken);
                        return params;
                    }
        };
        queue.add(dr);
    }

    public void loginRequest(String userTokenID, final VolleyLoginCallBack callBack){
        JSONObject jsonobj;
        jsonobj = new JSONObject();
        try{
            //Adding some keys
            jsonobj.put("token", userTokenID);
            logger.statusMessage("Adding token to json to send to server");
        }catch (JSONException e){
            e.printStackTrace();
        }
        logger.statusMessage("Trying to send json object to server: " + jsonobj);
        RequestQueue queue = Volley.newRequestQueue(this.appContext);
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, loginUrl, jsonobj, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                //Get the access token
                try {
                    backendAccessToken = response.getString("access_token");
                    logger.successMessage("Access token retrieved: " + backendAccessToken);
                } catch (JSONException e){
                    logger.errorMessage("Couldn't retrive access token from: " + response.toString());
                }
                callBack.onSuccess(response.toString());
            }
        },
        new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                logger.errorMessage("Connection to server " + error.toString());
                callBack.onSuccess(error.toString());}
        });
        queue.add(loginRequest);
    }

    public String getRequestResponse(){
        return requestResponse;
    }

}
