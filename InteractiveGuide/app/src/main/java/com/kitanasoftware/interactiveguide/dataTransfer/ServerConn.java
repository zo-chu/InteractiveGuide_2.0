package com.kitanasoftware.interactiveguide.dataTransfer;

import android.app.Service;
import android.content.ContentResolver;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.text.format.Formatter;

import com.kitanasoftware.interactiveguide.db.WorkWithDb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by dasha on 24/02/16.
 */
public class ServerConn extends Thread {
    public void run() {

        ServerSocket serverConn;
        String clientIp;
        WorkWithDb workWithDb;

        try {
            serverConn = new ServerSocket(5002);
            workWithDb=WorkWithDb.getWorkWithDb();
            System.out.println("Server Waiting for client on port 5002");


            // IP of server put to client
            Socket connectionSocket;
            while (true) {
                try {
                    connectionSocket = serverConn.accept();
                    System.out.println("Conn OK!!");

                    BufferedReader br = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    clientIp= br.readLine();
                    System.out.println(clientIp);
                    workWithDb.addIp(clientIp);
                    (new ClientHandler(connectionSocket)).start();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch ( IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}