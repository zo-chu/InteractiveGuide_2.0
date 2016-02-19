package com.kitanasoftware.interactiveguide;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class    BroadCastScreen_6 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.broad_cast_screen_6);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#f8bfd8"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
    }
}
