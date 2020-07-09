package com.example.tosuccess;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class JsonReader {

    String JsonString;
    Integer dayOfYear;

    ArrayList<String> activity_name;
    ArrayList<Integer> seconds_after_midnight;

    public JsonReader(String json, Integer filterDate){
        JsonString = json;
        dayOfYear = filterDate;
        System.out.println("JSON STRING: " + JsonString);
        if(JsonString != null) {
            readJson();
        }else{
            System.out.println("Json string error!");
        }
    }

    private void readJson() {
        activity_name = new ArrayList<String>();
        seconds_after_midnight = new ArrayList<Integer>();
        try {
            JSONObject obj = new JSONObject(JsonString);
            JSONArray arr = obj.getJSONArray("results");
            for (int i = 0; i < arr.length(); i++) {
                System.out.println("Json object: " + arr.getJSONObject(i).getInt("date") + " | Checking with date: " + String.valueOf(dayOfYear));

                if(arr.getJSONObject(i).getInt("date") == dayOfYear) { //Currently we pull everything from the server and sort it on the device. This is not good!
                    activity_name.add(arr.getJSONObject(i).getString("activity_name"));
                    seconds_after_midnight.add(arr.getJSONObject(i).getInt("seconds_after_midnight"));
                }else{
                    System.out.println("NOT A MATCH!");
                }
            }
        } catch (JSONException j){
            System.out.println("KEEPS FAILING");
            j.printStackTrace();
        }
    }

    public List<String> getActivityName(){return activity_name;}
    public List<Integer> getSecondsAfterMidnight(){return seconds_after_midnight;}
}
