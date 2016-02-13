package com.kitanasoftware.interactiveguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class  EnterYourNameScreen_2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_your_name_screen_2);
    }

    public void OKclick(View view) {
        Intent intent = new Intent(getApplicationContext(),ChooseYourGroupScreen_3.class);
        startActivity(intent);
    }
}
