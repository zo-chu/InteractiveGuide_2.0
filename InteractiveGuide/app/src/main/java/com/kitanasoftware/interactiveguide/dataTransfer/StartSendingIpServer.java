package com.kitanasoftware.interactiveguide.dataTransfer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class StartSendingIpServer extends Service {

    // it wil send every 5 sec. even if app close
    public StartSendingIpServer() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SendIp sendIp = new SendIp();
        sendIp.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
