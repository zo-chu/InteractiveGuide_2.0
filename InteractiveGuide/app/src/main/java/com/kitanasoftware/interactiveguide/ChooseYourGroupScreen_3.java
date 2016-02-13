package com.kitanasoftware.interactiveguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class  ChooseYourGroupScreen_3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_your_group_screen_3);
    }

    public void goToMainScreen(View view) {
        Intent intent = new Intent(getApplicationContext(),MainScreen_4.class);
        startActivity(intent);//'
    }
}
