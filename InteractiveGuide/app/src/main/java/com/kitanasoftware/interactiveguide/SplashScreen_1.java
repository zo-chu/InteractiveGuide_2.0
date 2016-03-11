package com.kitanasoftware.interactiveguide;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.Toast;

import com.kitanasoftware.interactiveguide.dataTransfer.StartConn;
import com.kitanasoftware.interactiveguide.db.WorkWithDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class  SplashScreen_1 extends AppCompatActivity {

    WorkWithDb workWithDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_1);
        workWithDb = WorkWithDb.getWorkWithDb(getApplicationContext());

        Intent intent = new Intent(getApplicationContext(), StartConn.class);
        startService(intent);

//        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
//        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
//        System.out.println("ip " + ip);




        Thread timerThread = new Thread() {
             public void run() {
                 try {
                     sleep(3000);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 } finally {

                     Intent intent = new Intent(getApplicationContext(), EnterYourNameScreen_2.class);
                     startActivity(intent);

                 }
             }
         };
        timerThread.start();
    }
}

