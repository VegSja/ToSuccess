package com.example.tosuccess;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    View popView;

    ArrayList<Plan> activities;
    ActivitiesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Lookup the recyclerview in activity layout
        RecyclerView rvActivities = (RecyclerView) findViewById(R.id.rvActivities);

        //Initialize activities
        activities = Plan.createPlanList(5);
        //Create adapter passing in the sample user data
        adapter = new ActivitiesAdapter(activities);
        //Attach the adapter to the recyclerview to populate items
        rvActivities.setAdapter(adapter);
        //Set layout manager to position the items
        rvActivities.setLayoutManager(new LinearLayoutManager(this));


    }

    //Called when user touches the button
    public void addActivty(View view) {
        // Do something in response to button click
        System.out.println("Button clicked");
        createPopUp(view);
    }

    public void createPopUp(View view){
        System.out.println("Init pop");
        //Inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        popView = inflater.inflate(R.layout.input_pop, null);

        //Create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popView, width, height, focusable);

        //Show popup window
        popupWindow.showAtLocation(this.findViewById(R.id.main), Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    //Is called when create activty button is pressed
    public void createActivityButton(View view){
        EditText textInput = (EditText) popView.findViewById(R.id.textInput);
        createActivity(textInput.getText().toString());
    }

    public void createActivity(String activity_name){
        //Create activity
        Plan plan = new Plan(activity_name);
        //Add activity to adapterlist
        activities.add(plan);
        //Notify the adapter that Dataset/Array has changed
        adapter.notifyDataSetChanged();

        //Display pop-up message
        Snackbar popUpMessage = Snackbar.make(this.findViewById(R.id.main), "Created activity", Snackbar.LENGTH_SHORT);
        popUpMessage.show();
    }
}