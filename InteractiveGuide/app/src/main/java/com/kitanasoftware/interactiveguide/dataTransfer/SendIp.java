package com.kitanasoftware.interactiveguide.dataTransfer;

import android.os.StrictMode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by dasha on 12/03/16.
 */
public class SendIp extends Thread {
   String message = "WELCOME";

    @Override
    public void run() {
        super.run();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            while (true) {
                Thread.sleep(5000);
                DatagramSocket socket = new DatagramSocket();
                socket.setBroadcast(true);
                byte[] sendData = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData,
                        sendData.length,
                        WifiUtility.getBroadcastAddress(),
                        5001);
                socket.send(sendPacket);
                System.out.println("send " + message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
