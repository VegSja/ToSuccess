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
        if(JsonString != null) {
            readJson();
        }
    }

    private void readJson() {
        activity_name = new ArrayList<String>();
        seconds_after_midnight = new ArrayList<Integer>();
        try {
            JSONArray arr = new JSONArray(JsonString);
            for (int i = 0; i < arr.length(); i++) {

                if(arr.getJSONObject(i).getInt("date") == dayOfYear) { //Currently we pull everything from the server and sort it on the device. This is not good!
                    activity_name.add(arr.getJSONObject(i).getString("activity_name"));
                    seconds_after_midnight.add(arr.getJSONObject(i).getInt("minutes_after_midnight"));
                }
            }
        } catch (JSONException j){
            j.printStackTrace();
        }
    }

    public List<String> getActivityName(){return activity_name;}
    public List<Integer> getSecondsAfterMidnight(){return seconds_after_midnight;}
}
