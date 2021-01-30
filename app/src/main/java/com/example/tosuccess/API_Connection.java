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

    /**How class works: on calls like GET, POST we need to send a refresh token before we do the action, because the server refreshes its accesstokens every 5 seconds.
     * So we send that and onSucces we send the actual request
     * There are some flaws in the app, but these are mainly UI issues. However
     * Known issues:
     *  Inconsistent reload of page
     *
     * **/


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
    public interface VolleyRefreshCallBack{
        void onSuccess(String response);
        void onError(String errorMessage);
    }



    String url = "https://vegsja.pythonanywhere.com/activities/";
    String loginUrl = "https://vegsja.pythonanywhere.com/google/";
    String refreshUrl = "https://vegsja.pythonanywhere.com/refresh/";

    Context appContext;

    String requestResponse;

    String backendAccessToken;
    String backendRefreshToken;

    Logger logger = new Logger();


    public API_Connection(Context appContext){
        this.appContext = appContext;
    }


    public void getRequest(int date, final String accessToken, final VolleyGetCallBack callBack){
        RequestQueue queue = Volley.newRequestQueue(this.appContext);

        //Request json response form the URL
        StringRequest jsonRequest = new StringRequest(Request.Method.GET, url + "?date=" + String.valueOf(date) + "&nb_days=1", new Response.Listener<String>() {
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
    }

    public void postRequest(final String name, final Integer minutes_after_midnight_start, final Integer minutes_after_midnight_end, final Integer dayNumber, final String date_string, final VolleyPushCallBack callBack){
        this.sendRefreshRequest(new API_Connection.VolleyRefreshCallBack() {
            @Override
            public void onSuccess(String response) {
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
                RequestQueue queue = Volley.newRequestQueue(appContext);
                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonobj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            callBack.onSuccess(response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            logger.loggerMessage("Failed to post to server " + error.toString());
                            callBack.onError(error.toString());
                        }
                    }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError{
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("Authorization", " Bearer " + backendAccessToken);
                        return params;
                    }
                };
                queue.add(postRequest);
            }

            @Override
            public void onError(String errorMessage) {
                return;
            }
        });
    }
    
    public void deleteRequest(final String accessToken, final int id, final VolleyDeleteCallBack callBack){
        this.sendRefreshRequest(new API_Connection.VolleyRefreshCallBack() {
            @Override
            public void onSuccess(String response) {
                String deleteUrl = url.concat(String.valueOf(id) + '/');
                RequestQueue queue = Volley.newRequestQueue(appContext);
                StringRequest dr = new StringRequest(Request.Method.DELETE, deleteUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                callBack.onSuccess(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                logger.errorMessage("Delete error for id " + String.valueOf(id) + error.toString());
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
            @Override
            public void onError(String errorMessage) {
                return;
            }
        });

    }

    public void loginRequest(String userTokenID, final VolleyLoginCallBack callBack){
        JSONObject jsonobj;
        jsonobj = new JSONObject();
        try{
            //Adding some keys
            jsonobj.put("token", userTokenID);
        }catch (JSONException e){
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(this.appContext);
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, loginUrl, jsonobj, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                //Get the access token
                try {
                    backendAccessToken = response.getString("access_token");
                    backendRefreshToken = response.getString("refreash_token");
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

    public void sendRefreshRequest(final VolleyRefreshCallBack callBack){
        JSONObject jsonobj = new JSONObject();
        try{
            //Adding some keys
            jsonobj.put("refresh", backendRefreshToken);
        }catch (JSONException e){
            e.printStackTrace();
        }
        logger.loggerMessage("Sending refresh token to server: " + jsonobj.toString());
        RequestQueue queue = Volley.newRequestQueue(this.appContext);
        JsonObjectRequest refreshRequest = new JsonObjectRequest(Request.Method.POST, refreshUrl, jsonobj, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                //Get the refresh token
                try {
                    backendAccessToken = response.getString("access");
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
        queue.add(refreshRequest);
    }

    public String getRequestResponse(){
        return requestResponse;
    }

}
