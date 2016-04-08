package com.kitanasoftware.interactiveguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kitanasoftware.interactiveguide.dataTransfer.ServerConn;
import com.kitanasoftware.interactiveguide.dataTransfer.StartConn;
import com.kitanasoftware.interactiveguide.dataTransfer.StartConn;
import com.kitanasoftware.interactiveguide.dataTransfer.StartSendingIpServer;
import com.kitanasoftware.interactiveguide.db.WorkWithDb;
import com.kitanasoftware.interactiveguide.information.InformatoonScreen_9;
import com.kitanasoftware.interactiveguide.map.EditingDialog;
import com.kitanasoftware.interactiveguide.map.MapScreen_5;
import com.kitanasoftware.interactiveguide.notification.NotificationScreen_7;

public class  MainScreen_4 extends AppCompatActivity {


    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_4);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#127e83"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        WorkWithDb.getWorkWithDb(getApplicationContext());
        startService(new Intent(getApplicationContext(), StartSendingIpServer.class));
    }

    public void MAPclick(View view) {
        intent = new Intent(getApplicationContext(),MapScreen_5.class);
        startActivity(intent);//
    }

    public void AUDIOclick(View view) {
        intent = new Intent(getApplicationContext(),BroadCastScreen_6.class);
        startActivity(intent);
    }

    public void SCHEDULEclick(View view) {
        intent = new Intent(getApplicationContext(),ScheduleScreen_8.class);
        startActivity(intent);
    }

    public void NOTIFICATIONSclick(View view) {
        intent = new Intent(getApplicationContext(),NotificationScreen_7.class);
        startActivity(intent);
    }

    public void INFORMATIIONclick(View view) {
        intent = new Intent(getApplicationContext(),InformatoonScreen_9.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Intent intent = new Intent(this,StartConn.class);
//        startService(intent);
        //item.setEnabled(false);
        //btn=findViewById(R.id.route);
        //btn.setEnabled(false);

        if(ServerConn.isSTATUS()) {
            startService(new Intent(getApplicationContext(), StartConn.class));
            ServerConn.setSTATUS(false);
        }else Toast.makeText(getApplicationContext(), "Sending has started", Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }


}
