package com.example.tosuccess;

//AndroidX imports
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

//Standard JDK imports
import java.io.Serializable;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {

    FrameLayout mainLayout;

    PopUpClass popUpClass;

    ArrayList<Plan> activities;

    RecyclerView rvActivities;
    ActivitiesAdapter adapter;

    API_Connection connection;

    String userTokenID;

    Logger logger = new Logger();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigation navigation = new BottomNavigation(this, bottomNavigationView);

        Intent intent = getIntent();
        userTokenID = intent.getStringExtra("IDToken");

        logger.loggerMessage("IDTOKEN: " + userTokenID);

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
        loginToBackend();
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
    }

    //Is called when create activty button is pressed
    public void createActivityButton(View view){
        //Get activity name
        EditText textInput = (EditText) popUpClass.getPopupView().findViewById(R.id.textInput);
        String activity_name =  textInput.getText().toString();

        //Get time
        Spinner timeStart = (Spinner) popUpClass.getPopupView().findViewById(R.id.time_spinner_start);
        String timeStartStr = timeStart.getSelectedItem().toString();
        int minutesAfterMidnightStart = timeStrToMinutes(timeStartStr);

        Spinner timeEnd = (Spinner) popUpClass.getPopupView().findViewById(R.id.time_spinner_end);
        String timeEndStr = timeEnd.getSelectedItem().toString();
        int minutesAfterMidnightEnd = timeStrToMinutes(timeEndStr);

        //Send to server and create activity
        sendActivityToServer(activity_name, minutesAfterMidnightStart, minutesAfterMidnightEnd, getCurrentDayOfYear(), getCurrentLocalDate()); //We need to call this one here becuase if we call it in the createActivity it will be called each time we update from server
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
    //TODO: Make a system which also sends when an activity is finished. Drop down menu?
    public void createActivity(String activity_name, int minutesAfterMidnightStart, int minutesAfterMidnightEnd, int activity_id){
        //Create activity
        Plan plan = new Plan(activity_name, minutesAfterMidnightStart, minutesAfterMidnightEnd, minutesToTimeStr(minutesAfterMidnightStart), minutesToTimeStr(minutesAfterMidnightEnd),false, activity_id);
        logger.loggerMessage("Creating activity: " + "Minutes" + String.valueOf(minutesAfterMidnightStart) + "Minutes string: " + minutesToTimeStr(minutesAfterMidnightStart));

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

    public void deleteActivity(View view){
        ViewGroup activityCard = (ViewGroup) view.getParent();
        TextView textView = (TextView) activityCard.findViewById(R.id.activity_name);
        int activityId = Integer.parseInt(textView.getTag().toString());


        connection.deleteRequest(connection.backendAccessToken,activityId, new API_Connection.VolleyDeleteCallBack() {
            @Override
            public void onSuccess(String response) {
                createPopUpMessage("Successfully deleted activity");
                populateActivitiesFromServer();
            }

            @Override
            public void onError(String errorMessage) {
                createPopUpMessage("Could not delete activity");
                populateActivitiesFromServer();
            }
        });
    }

    public void createPopUpMessage(String message){
        //Display pop-up message
        Snackbar popUpMessage = Snackbar.make(this.findViewById(R.id.main), message, Snackbar.LENGTH_SHORT);
        popUpMessage.show();
    }

    public void populateActivitiesFromServer(){

        activities = new ArrayList<Plan>(); //Clear the activities before updating the entire list

        //The callback thingy makes the program wait until onSuccess is called in OnResponse in API_Connection
        connection.getRequest(this.getCurrentDayOfYear(), connection.backendAccessToken, new API_Connection.VolleyGetCallBack() {
            @Override
            public void onSuccess(String response) {
                createPopUpMessage("Successfully connected to server");
                JsonReader jReader = new JsonReader(response, getCurrentDayOfYear());
                for(int i=0; i<jReader.getActivityName().size(); i++) {
                    createActivity(jReader.getActivityName().get(i), jReader.getSecondsAfterMidnightStart().get(i), jReader.getSecondsAfterMidnightEnd().get(i), jReader.getActivityId().get(i));
                }
            }

            @Override
            public void onError(String errorMessage) {
                createPopUpMessage(errorMessage);
            }
        });
    }

    public void sendActivityToServer(String activity_name, int minutesAfterMidnightStart, int minutesAfterMidnightEnd, int dayNumber, String date_string){
        connection.postRequest(activity_name, minutesAfterMidnightStart, minutesAfterMidnightEnd, dayNumber, date_string,new API_Connection.VolleyPushCallBack() {
            @Override
            public void onSuccess(String response) {
                createPopUpMessage("Successfully pushed to server");
                populateActivitiesFromServer();
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

    public int timeStrToMinutes(String timeStr){
        int hour = Integer.parseInt(timeStr.substring(0,2));
        int minutes = Integer.parseInt(timeStr.substring(3,5));
        return hour*60+minutes;
    }

    public void loginToBackend(){
        connection = new API_Connection(this);
        connection.loginRequest(userTokenID, new API_Connection.VolleyLoginCallBack() {
            @Override
            public void onSuccess(String response) {
                createPopUpMessage("Successfully connected to backend as user");
                populateActivitiesFromServer();
            }

            @Override
            public void onError(String errorMessage) {
                createPopUpMessage("ERROR: " + errorMessage);
            }
        });
    }


    public void startLogin(View v){
        Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(loginActivityIntent);
    }
}