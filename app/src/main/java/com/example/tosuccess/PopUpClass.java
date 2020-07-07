package com.example.tosuccess;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class PopUpClass {

    private View popupView;
    private FrameLayout mainScreenLayout;

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

    public View getPopupView(){
        return popupView;
    }

}
