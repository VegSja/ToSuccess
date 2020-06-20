package com.example.tosuccess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Plan {
    String activityName;
    Context context;

    public Plan(String ActivityName, Context parentContext){
        activityName = ActivityName;
        context = parentContext;
    }

    public LinearLayout viewGroup(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout activityLayout = (LinearLayout) inflater.inflate(R.layout.activity, null);
        TextView activityText = (TextView) activityLayout.findViewById(R.id.activity_name);
        activityText.setText(activityName);
        return activityLayout;
    }
}
