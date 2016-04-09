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
            //getting ip from Client
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
            System.out.println("JSONs witch send: ");

            //creating json array with all geopoints
            jsonArrayGeo = workWithDb.getJsonArrayGeo();
            System.out.println("--" + jsonArrayGeo.getJSONObject(0)+ " size " + jsonArrayGeo.length());

            //creating json array with schedule
            jsonArraySchedule = workWithDb.getJsonArraySchedule();
            System.out.println("--" + jsonArraySchedule);

            //creating json with information
            jsonObjectInf = workWithDb.getJsonObjectInform();
            System.out.println("--" + jsonObjectInf);


            //put JASONs to one JSON object for sending
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("geo", jsonArrayGeo);
            jsonObject.put("schedule", jsonArraySchedule);
            jsonObject.put("inf", jsonObjectInf);

            //sending JSON object to client
            objectOutputStream.writeObject(jsonObject.toString());
            System.out.println("DB was sent");

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            objectOutputStream.close();
        }

    }

}
