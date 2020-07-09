package com.example.tosuccess;

//AndroidX imports
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.snackbar.Snackbar;

//Standard JDK imports
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collections;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements TimerPickerFragment.OnTimeSelectedListener {

    FrameLayout mainLayout;

    PopUpClass popUpClass;

    ArrayList<Plan> activities;

    RecyclerView rvActivities;
    ActivitiesAdapter adapter;

    API_Connection connection;

    int timePickerMinutesAfterMidnight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create the list. To avoid errors in the future
        activities = new ArrayList<Plan>();

        if (activities.size() > 0) {
            createRv(activities);
        }

        //Change dimness of foreground
        mainLayout = (FrameLayout) findViewById(R.id.main);
        mainLayout.getForeground().setAlpha(0);

        //Set date to header
        displayDateTime();

        populateActivitiesFromServer();
    }


    //Gets local date for header
    public String getCurrentLocalDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd. MMMM yyyy");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public Integer getCurrentDayOfYear(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public void displayDateTime(){
        TextView textView = (TextView) findViewById(R.id.headerDateText);
        textView.setText(getCurrentLocalDate());
    }

    //Setup RecyclerViewer
    public void createRv(ArrayList<Plan> planArray){
        //Lookup the recyclerview in activity layout
        rvActivities = (RecyclerView) findViewById(R.id.rvActivities);

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
    public void addActivtyButton(View view) throws InterruptedException {
        createPopUp(view);
    }

    public void createPopUp(View view){
        popUpClass = new PopUpClass(mainLayout);
        popUpClass.showPopupWindow(view);
        //Set the opacity of the main layout
    }

    //Is called when create activty button is pressed
    public void createActivityButton(View view){
        EditText textInput = (EditText) popUpClass.getPopupView().findViewById(R.id.textInput);
        createActivity(textInput.getText().toString(), timePickerMinutesAfterMidnight);
    }

    //Timepicker actions ----------------------------
    public void showTimePickerDialog(View v){
        DialogFragment newFragment = new TimerPickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onAttachFragment(Fragment fragment){
        if(fragment instanceof TimerPickerFragment){
            TimerPickerFragment timerPickerFragment = (TimerPickerFragment) fragment;
            timerPickerFragment.setOnTimeSetListener(this);
        }
    }

    public void onTimeSelected(int minutesAfterMidnight){

        //Change the button text
        Button timeButton = (Button) popUpClass.getPopupView().findViewById(R.id.TimeButton);
        timeButton.setText(minutesToTimeStr(minutesAfterMidnight));

        //Sets up the variable that the created activity depends on
        timePickerMinutesAfterMidnight = minutesAfterMidnight;
    }

    //Is called when toggle on card is pressed
    public void onToggleClicked(View view){
        ToggleButton toggle = (ToggleButton) view.findViewById(R.id.toggleButton);
        ViewGroup activityCard = (ViewGroup) view.getParent();
        TextView textView = (TextView) activityCard.findViewById(R.id.activity_name);
        String activityName = textView.getText().toString();
        changeStateOfActivity(activityName, toggle.isChecked());
    }

    public void changeStateOfActivity(String activityName, Boolean state){
        for(int i = 0; i < adapter.mActivities.size(); i++){
            if (activityName == adapter.mActivities.get(i).activityName){
                adapter.mActivities.get(i).completed = state;
            }
        }
    }

    public void createActivity(String activity_name, int minutesAfterMidnight){
        //Create activity
        Plan plan = new Plan(activity_name, minutesAfterMidnight, minutesToTimeStr(minutesAfterMidnight),false);
        sendActivityToServer(activity_name, minutesAfterMidnight, getCurrentDayOfYear());

        if(activities.size() > 0) {
            //Add activity to adapterlist
            activities.add(plan);

            //Sort the activites before sending to array adapter
            Collections.sort(activities);

            //Notify the adapter that Dataset/Array has changed
            adapter.notifyDataSetChanged();
        }
        else if(activities.size() == 0){
            //Add activity to adapterlist
            activities.add(plan);
            createRv(activities);
        }
        rvActivities.scheduleLayoutAnimation();
    }

    public void createPopUpMessage(String message){
        //Display pop-up message
        Snackbar popUpMessage = Snackbar.make(this.findViewById(R.id.main), message, Snackbar.LENGTH_SHORT);
        popUpMessage.show();
    }

    public void populateActivitiesFromServer(){
        connection = new API_Connection(this);

        //The callback thingy makes the program wait until onSuccess is called in OnResponse in API_Connection
        connection.getRequest(new API_Connection.VolleyGetCallBack() {
            @Override
            public void onSuccess(String response) {
                createPopUpMessage("Successfully connected to server");
                JsonReader jReader = new JsonReader(response, getCurrentDayOfYear());
                for(int i=0; i<jReader.getActivityName().size(); i++) {
                    createActivity(jReader.getActivityName().get(i), jReader.getSecondsAfterMidnight().get(i));
                }
            }

            @Override
            public void onError(String errorMessage) {
                createPopUpMessage(errorMessage);
            }
        });
    }

    public void sendActivityToServer(String activity_name, int minutesAfterMidnight, int dayNumber){
        connection.postRequest(new API_Connection.VolleyPushCallBack() {
            @Override
            public void onSuccess(String response) {
                createPopUpMessage("Successfully pushed to server");
            }

            @Override
            public void onError(String errorMessage) {
                createPopUpMessage("Failed to push to server");
            }
        });
    }

    public String minutesToTimeStr(int minutesAfterMidnight){
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
}