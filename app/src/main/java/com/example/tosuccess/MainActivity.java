package com.example.tosuccess;

//AndroidX imports
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
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.snackbar.Snackbar;

//Standard JDK imports
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    PopUpClass popUpClass;

    ArrayList<Plan> activities;

    ActivitiesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Create the list. To avoid errors in the future
        activities = new ArrayList<Plan>();

        if (activities.size() > 0) {
            createRv(activities);
        }

        //Set date to header
        displayDateTime();
    }
    //Gets local date for header
    public String getCurrentLocalDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd. MMMM yyyy");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public void displayDateTime(){
        TextView textView = (TextView) findViewById(R.id.headerDateText);
        textView.setText(getCurrentLocalDate());
    }

    //Setup RecyclerViewer
    public void createRv(ArrayList<Plan> planArray){
        //Lookup the recyclerview in activity layout
        RecyclerView rvActivities = (RecyclerView) findViewById(R.id.rvActivities);

        //Initialize the variable activites
        activities = planArray;
        //Create adapter passing in the sample user data
        adapter = new ActivitiesAdapter(activities);
        //Attach the adapter to the recyclerview to populate items
        rvActivities.setAdapter(adapter);
        //Set layout manager to position the items
        rvActivities.setLayoutManager(new LinearLayoutManager(this));
    }

    //Called when user touches the button
    public void addActivtyButton(View view) {
        // Do something in response to button click
        System.out.println("Button clicked");
        createPopUp(view);
    }

    public void createPopUp(View view){
        popUpClass = new PopUpClass();
        popUpClass.showPopupWindow(view);
    }

    //Is called when create activty button is pressed
    public void createActivityButton(View view){
        EditText textInput = (EditText) popUpClass.getPopupView().findViewById(R.id.textInput);
        createActivity(textInput.getText().toString());
    }

    //Is called when toggle on card is pressed
    public void onToggleClicked(View view){
        ToggleButton toggle = (ToggleButton) view.findViewById(R.id.toggleButton);
        ViewGroup activityCard = (ViewGroup) view.getParent();
        TextView textView = (TextView) activityCard.findViewById(R.id.activity_name);
        createPopUpMessage("Toggle on: " + textView.getText());
        String activityName = textView.getText().toString();
        changeStateOfActivity(activityName, toggle.isChecked());
    }

    public void changeStateOfActivity(String activityName, Boolean state){
        for(int i = 0; i < adapter.mActivities.size(); i++){
            if (activityName == adapter.mActivities.get(i).activityName){
                adapter.mActivities.get(i).completed = state;
                createPopUpMessage("Changed state on: " + adapter.mActivities.get(i).activityName + " = " + state.booleanValue());
            }
        }
    }

    public void createActivity(String activity_name){
        //Create activity
        Plan plan = new Plan(activity_name, false);

        if(activities.size() > 0) {
            //Add activity to adapterlist
            activities.add(plan);
            //Notify the adapter that Dataset/Array has changed
            adapter.notifyDataSetChanged();
        }
        else if(activities.size() == 0){
            //Add activity to adapterlist
            activities.add(plan);
            createRv(activities);
        }
        createPopUpMessage("Created Activity: " + activity_name);
        System.out.println("ActivitiesAdapter: " + adapter.mActivities);
    }

    public void createPopUpMessage(String message){
        //Display pop-up message
        Snackbar popUpMessage = Snackbar.make(this.findViewById(R.id.main), message, Snackbar.LENGTH_SHORT);
        popUpMessage.show();
    }
}