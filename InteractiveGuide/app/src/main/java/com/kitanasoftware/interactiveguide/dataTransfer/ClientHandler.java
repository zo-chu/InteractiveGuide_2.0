package com.kitanasoftware.interactiveguide.dataTransfer;

import com.kitanasoftware.interactiveguide.db.WorkWithDb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by dasha on 26/02/16.
 */
public class ClientHandler extends Thread{
    protected Socket connectedClient = null;
    protected ObjectOutputStream objectOutputStream = null;
    protected ObjectInputStream objectInputStrem = null;


    public ClientHandler(Socket client) {
        this.connectedClient   = client;
    }

    @Override
    public void run() {
        super.run();
        try {
            //objectInputStrem = new ObjectInputStream(connectedClient.getInputStream());
            objectOutputStream = new ObjectOutputStream(connectedClient.getOutputStream());
            SendDbToGui();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void SendDbToGui() throws IOException {

        WorkWithDb workWithDb = WorkWithDb.getWorkWithDb();
        JSONArray jsonArrayGeo = null;
        JSONArray jsonArraySchedule = null;
        JSONObject jsonObjectInf = null;

        try {
            jsonArrayGeo = workWithDb.getJsonArrayGeo();
            System.out.println("--" + jsonArrayGeo.getJSONObject(0)+ " size " + jsonArrayGeo.length());
            jsonArraySchedule = workWithDb.getJsonArraySchedule();
            System.out.println("--" + jsonArraySchedule);
            jsonObjectInf = workWithDb.getJsonObjectInform();
            System.out.println("--" + jsonObjectInf);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("geo",jsonArrayGeo);
            jsonObject.put("schedule",jsonArraySchedule);
            jsonObject.put("inf",jsonObjectInf);

            objectOutputStream.writeObject(jsonObject.toString());
            System.out.println("geo" + workWithDb.getGeopointList().size());
            System.out.println("s" + workWithDb.getScheduleList().size());
            System.out.println("geo" + workWithDb.getInformList().get(0).toString());

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            objectOutputStream.close();
        }

    }

}
