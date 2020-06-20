package com.example.tosuccess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.android.material.snackbar.Snackbar;

import custom_classes.Plan;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        View popView = inflater.inflate(R.layout.input_pop, null);

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
        createActivity("tiss");
    }

    public void createActivity(String activity_name){
        Plan plan = new Plan(activity_name);
        //Display pop-up message
        Snackbar popUpMessage = Snackbar.make(this.findViewById(R.id.main), "Created activity", Snackbar.LENGTH_SHORT);
        popUpMessage.show();
    }
}