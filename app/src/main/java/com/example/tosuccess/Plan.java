package com.example.tosuccess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.String.valueOf;

public class Plan implements Comparable<Plan>{
    String activityName;
    int minutesAfterMidnight;
    String startingTime;
    Boolean completed;

    @Override
    public int compareTo(Plan p){
        return this.minutesAfterMidnight - p.getMinutes();
    }

    public Plan(String ActivityName, int minutesAfterMidnight, String startingTime,  Boolean activityCompleted){
        activityName = ActivityName;
        this.minutesAfterMidnight = minutesAfterMidnight;
        this.startingTime = startingTime;
        completed = activityCompleted;

    }
    public String getName(){
        return activityName;
    }
    public Boolean getCompleted(){
        return  completed;
    }
    public String getTime(){return startingTime;}
    public int getMinutes(){return minutesAfterMidnight;}
}
