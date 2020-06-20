package com.example.tosuccess;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    Context context;
    ConstraintLayout pageLayout;
    View popView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getBaseContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pageLayout = (ConstraintLayout) inflater.inflate(R.layout.activity_main, null);
        LinearLayout activityLayout = (LinearLayout) inflater.inflate(R.layout.activity, null);

        setContentView(pageLayout);

        //pageLayout.addView(activityLayout);


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
        Plan plan = new Plan(activity_name, context);
        //Display acitivty
        System.out.println("Tried to create a viewGroup");
        pageLayout.addView(plan.viewGroup());

        //Display pop-up message
        Snackbar popUpMessage = Snackbar.make(this.findViewById(R.id.main), "Created activity", Snackbar.LENGTH_SHORT);
        popUpMessage.show();
    }
}