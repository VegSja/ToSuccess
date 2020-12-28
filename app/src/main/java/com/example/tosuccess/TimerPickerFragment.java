package com.example.tosuccess;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimerPickerFragment extends DialogFragment implements  TimePickerDialog.OnTimeSetListener {

    OnTimeSelectedListener callback;

    public void setOnTimeSetListener(OnTimeSelectedListener callback){
        this.callback = callback;
    }

    public interface OnTimeSelectedListener{
        public void onTimeSelected(int minutesAfterMidnight);
    }


    private Calendar mCalendar = Calendar.getInstance();


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // Use current time as default values for time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create new instece of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(),this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute){

        //Do something with the time chosen
        int minutesAfterMidnight = hourOfDay * 60 + minute;
        callback.onTimeSelected(minutesAfterMidnight);
    }
}
