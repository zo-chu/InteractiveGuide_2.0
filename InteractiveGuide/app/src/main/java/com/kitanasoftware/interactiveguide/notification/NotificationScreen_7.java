package com.kitanasoftware.interactiveguide.notification;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kitanasoftware.interactiveguide.DrawerAppCompatActivity;
import com.kitanasoftware.interactiveguide.R;
import com.kitanasoftware.interactiveguide.db.WorkWithDb;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class NotificationScreen_7 extends DrawerAppCompatActivity {
    private EditText telephoneNumber;
    private EditText theMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#d7afd2"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        telephoneNumber = (EditText) findViewById(R.id.clientPhoneNumber);
        theMessage = (EditText) findViewById(R.id.textMess);

        getList();


    }
    private void getList(){

        if (WorkWithDb.getWorkWithDb().getNotificationList() != null) {
            ArrayList<MyNotification> notifs = WorkWithDb.getWorkWithDb().getNotificationList();
            System.out.println(notifs.size());
            if (notifs.size() != 0) {

                ArrayList<String> receivedMess = new ArrayList<>();

                for (int i = 0; i < notifs.size(); i++) {
                    String tmp = "";
                    tmp += "Sent to: " + notifs.get(i).getSentTo() + " Message: " + notifs.get(i).getText();
                    receivedMess.add(tmp);
                }
                if (receivedMess.size() != 0) {
                    ListView recievedMessList = (ListView) findViewById(R.id.sendedMess);
                    ArrayAdapter<String> listAdapter= new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.one_string_list, receivedMess);
                    recievedMessList.setAdapter(listAdapter);
                }
            }
        }
    }
    @Override
    public View getContentView() {
        return getLayoutInflater().inflate(R.layout.notification_screen_7, null);
    }

    public void sendMessage(View v) {

        String phoneNo = telephoneNumber.getText().toString();
        String message = theMessage.getText().toString();
        if ((phoneNo.length() > 0) & (message.length() > 0)) {
            WorkWithDb.getWorkWithDb().addNotification(phoneNo, message);
            sendSMS(phoneNo, message);
        } else {
            Toast.makeText(getBaseContext(),
                    "Please enter both phone number and message.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSMS(String phoneNumber, String message) {
        getList();
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", message + " My location is [" + getLocation() + "]");
        sendIntent.putExtra("address", phoneNumber);
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivity(sendIntent);
    }

    public GeoPoint getLocation() {
        GeoPoint myLocation;
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            return myLocation = new GeoPoint(0, 0);

        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            myLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
        } else {
            myLocation = new GeoPoint(0, 0);
        }
        return myLocation;
    }
}


