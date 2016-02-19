package com.kitanasoftware.interactiveguide;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class  NotificationScreen_7 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_screen_7);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#d7afd2"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
    }
}
