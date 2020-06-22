package com.example.tosuccess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class Plan {
    String activityName;
    Boolean completed;

    public Plan(String ActivityName, Boolean activityCompleted){
        activityName = ActivityName;
        completed = activityCompleted;
    }
    public String getName(){
        return activityName;
    }
    public Boolean getCompleted(){
        return  completed;
    }


    public static ArrayList<Plan> createPlanList(int nbOfPlans){
        ArrayList<Plan> plans = new ArrayList<Plan>();

        for (int i=1; i<nbOfPlans; i++){
            plans.add(new Plan("Activtity" + i, false));
        }
        return plans;
    }


}
