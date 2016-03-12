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
        System.out.println("service started");

        ServerConn serverConn = new ServerConn();
        serverConn.start();
        SendIp sendIp = new SendIp();
        sendIp.start();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
