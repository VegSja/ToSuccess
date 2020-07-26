package com.example.tosuccess;

import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigation extends AppCompatActivity {

    Logger logger = new Logger();

    public BottomNavigation() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                switch (item.getItemId()){
                    case R.id.page_1:
                        logger.loggerMessage("Page 1");
                        break;
                    case R.id.page_2:
                        logger.loggerMessage("Page 2");
                        break;
                }
                return true;
            }
        });
    }

}
