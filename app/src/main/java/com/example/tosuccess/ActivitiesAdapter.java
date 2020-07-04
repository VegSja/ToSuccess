package com.example.tosuccess;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;


public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        public TextView activityNameTextView;
        public TextView activityTimeTextView;
        public ToggleButton toggleButtonFinished;

        public ViewHolder(View itemView){
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            activityNameTextView = (TextView) itemView.findViewById(R.id.activity_name);
            activityTimeTextView = (TextView) itemView.findViewById(R.id.activity_time);
            toggleButtonFinished = (ToggleButton) itemView.findViewById(R.id.toggleButton);

        }

    }

    public List<Plan> mActivities;

    public ActivitiesAdapter(List<Plan> activities){
        this.mActivities = activities;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ActivitiesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View activityView = inflater.inflate(R.layout.activity, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(activityView);
        return viewHolder;
    }

    //Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ActivitiesAdapter.ViewHolder viewHolder, int position){
        //Get the data model based on position
        Plan activity = mActivities.get(position);

        //Set item views based on your views and data model
        TextView textView = viewHolder.activityNameTextView;
        textView.setText(activity.getName());

        TextView timeView = viewHolder.activityTimeTextView;
        timeView.setText(activity.getTime());

        //Set boolean value based on what was fed into plan
        ToggleButton toggleButton = viewHolder.toggleButtonFinished;
        toggleButton.setChecked(activity.getCompleted());

        activity.completed = toggleButton.isChecked();
    }

    //Returns the total count of items in the lsit
    @Override
    public int getItemCount(){
        return mActivities.size();
    }
}
