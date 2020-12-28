package com.example.tosuccess;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SimpleTimeZone;

public class PopUpClass {

    private View popupView;
    private FrameLayout mainScreenLayout;
    private String localTime;

    public PopUpClass(FrameLayout mainLayout){
        mainScreenLayout = mainLayout;
    }

    //PopupWindow display method
    public void showPopupWindow(final View view){
        //Set opacity of background
        mainScreenLayout.getForeground().setAlpha(220);

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.input_pop, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        //Make inactive items outside of Popupwindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.setAnimationStyle(R.style.animation);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        populateSpinner((Spinner) popupView.findViewById(R.id.time_spinner_start));
        populateSpinner((Spinner) popupView.findViewById(R.id.time_spinner_end));


        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //Reset opacity of background
                mainScreenLayout.getForeground().setAlpha(0);
            }
        });

    }
    //TODO: Make a better solution for this system
    public void populateSpinner(Spinner spinner){
        //Create array with the specified times
        List<String> time_array = new ArrayList<String>();
        String[] hours = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
        String[] minutes = {"00","05","10","15","20","25","30","35","40","45","50","55"};
        for(int i = 0; i < hours.length; i++){
            for(int y = 0; y < minutes.length; y++){
                String time = hours[i] + ":" + minutes[y];
                time_array.add(time);
            }
        }

        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(popupView.getContext() ,android.R.layout.simple_spinner_item, time_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Select start time");
        spinner.setSelection(144);
    }

    public View getPopupView(){
        return popupView;
    }

}
