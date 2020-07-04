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

public class Plan {
    String activityName;
    int minutesAfterMidnight;
    Boolean completed;

    public Plan(String ActivityName, int minutesAfterMidnight, Boolean activityCompleted){
        activityName = ActivityName;
        completed = activityCompleted;
        this.minutesAfterMidnight = minutesAfterMidnight;
    }
    public String getName(){
        return activityName;
    }
    public Boolean getCompleted(){
        return  completed;
    }

    public String getTime(){
        String hoursString = "";
        String minuteString = "";


        int hoursAfterMidnight = minutesAfterMidnight/60;
        if (hoursAfterMidnight < 10){
            hoursString = "0" + valueOf(hoursAfterMidnight);
        }
        else if(hoursAfterMidnight >= 10){
            hoursString = valueOf(hoursAfterMidnight);
        }
        int restMinutesAfterMidnight = (int) minutesAfterMidnight%60;
        if (restMinutesAfterMidnight < 10){
            minuteString = "0" + valueOf(restMinutesAfterMidnight);
        }
        else if(restMinutesAfterMidnight >= 10){
            minuteString = valueOf(restMinutesAfterMidnight);
        }

        String timeStr = hoursString + ":" + minuteString;
        return timeStr;
    }

    public static ArrayList<Plan> createPlanList(int nbOfPlans){
        ArrayList<Plan> plans = new ArrayList<Plan>();

        for (int i=1; i<nbOfPlans; i++){
            plans.add(new Plan("Activtity" + i, 100, false));
        }
        return plans;
    }


}
