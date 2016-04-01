package com.kitanasoftware.interactiveguide.dataTransfer;

import android.app.Service;
import android.content.ContentResolver;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.provider.Settings;
import android.text.format.Formatter;
import android.view.View;

import com.kitanasoftware.interactiveguide.db.WorkWithDb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by dasha on 24/02/16.
 */
public class ServerConn extends Thread {

    private boolean status_sending = false;
    private ServerSocket serverConn;
    private String clientIp;
    private WorkWithDb workWithDb;
    private ArrayList<String> devices = new ArrayList<String>();
    private int DP_PORT = 5010;
    static boolean STATUS=true;

    public static boolean isSTATUS() {
        return STATUS;
    }

    public static void setSTATUS(boolean STATUS) {
        ServerConn.STATUS = STATUS;
    }

    public void run() {

        try {
            STATUS=false;
            serverConn = new ServerSocket(DP_PORT);
            workWithDb=WorkWithDb.getWorkWithDb();
            System.out.println("Server Waiting for client on port 5010");

            Socket connectionSocket;
            while (true) {
                try {
                    connectionSocket = serverConn.accept();
                    System.out.println("Conn OK!!");

                    BufferedReader br = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    clientIp= br.readLine();
                    System.out.println(clientIp);
                    //   workWithDb.addIp(clientIp);

                    (new ClientHandler(connectionSocket)).start();
//                    connectionSocket.close();
//                    serverConn.close();
                    STATUS=true;
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