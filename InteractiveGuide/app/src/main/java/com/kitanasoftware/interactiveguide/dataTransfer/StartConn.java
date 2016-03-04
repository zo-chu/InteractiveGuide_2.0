package com.kitanasoftware.interactiveguide.dataTransfer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class StartConn extends Service {
    public StartConn() {
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(), "Service start", Toast.LENGTH_LONG).show();
        ServerConn serverConn = new ServerConn();
        serverConn.start();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
