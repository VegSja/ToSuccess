package com.example.tosuccess;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigation{

    Logger logger = new Logger();

    public BottomNavigation(final Context activityContext, BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                switch (item.getItemId()){
                    case R.id.daily_schedule:
                        logger.loggerMessage("Daily Schedule");
                        Intent scheduleIntent = new Intent(activityContext, MainActivity.class);
                        activityContext.startActivity(scheduleIntent);
                        break;
                    case R.id.subjects:
                        logger.loggerMessage("Subjects");
                        Intent subjectsIntent = new Intent(activityContext, SubjectsActivity.class);
                        activityContext.startActivity(subjectsIntent);
                        break;
                    case R.id.stats:
                        logger.loggerMessage("Stats");
                        Intent statsIntent = new Intent(activityContext, StatsActivity.class);
                        activityContext.startActivity(statsIntent);
                        break;
                    case R.id.settings:
                        logger.loggerMessage("Settings");
                        Intent settingsIntent = new Intent(activityContext, SettingsActivity.class);
                        activityContext.startActivity(settingsIntent);
                        break;
                }
                return true;
            }
        });
    }

}
