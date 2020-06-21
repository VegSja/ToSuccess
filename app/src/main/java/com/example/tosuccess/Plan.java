package com.example.tosuccess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Plan {
    String activityName;
    Context context;

    public Plan(String ActivityName){
        activityName = ActivityName;

    }
    public String getName(){
        return activityName;
    }

    public static ArrayList<Plan> createPlanList(int nbOfPlans){
        ArrayList<Plan> plans = new ArrayList<Plan>();

        for (int i=1; i<nbOfPlans; i++){
            plans.add(new Plan("Activtity" + i));
        }
        return plans;
    }


}
