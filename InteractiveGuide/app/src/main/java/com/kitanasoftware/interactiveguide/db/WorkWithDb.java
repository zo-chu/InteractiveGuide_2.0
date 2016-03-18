package com.kitanasoftware.interactiveguide.db;

import android.app.Notification;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kitanasoftware.interactiveguide.Schedule.Schedule;
import com.kitanasoftware.interactiveguide.map.Geopoint;
import com.kitanasoftware.interactiveguide.notification.MyNotification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by dasha on 23/02/16.
 */
public class WorkWithDb {

    private MyOH myOH;
    private SQLiteDatabase db;
    private Cursor cursor;
    private static WorkWithDb workWithDb;

    //1. узнать IP server
    //2. create 3 JASONArray from ourArrayList
    //3. create JASONObject and put 3 JASONArray
    //4. put JASONObject to OutPutStream from Serv
    //5. get from Client mes about "getdb"
    //6. set JASON
    private ArrayList<String> informList;
    private ArrayList<Schedule> scheduleList;
    private ArrayList<Geopoint> geopointList;
    private ArrayList<MyNotification> notificationList;

    private HashSet<String> ipList;

    private JSONObject jsonObjectInform;

    private JSONArray jsonArrayGeo;

    private JSONArray jsonArraySchedule;


    public JSONArray getJsonArrayGeo() {
        if(jsonArrayGeo.length()==0) {
            for (Geopoint g : getGeopointList()) {
                try {
                    jsonArrayGeo.put(g.createJSON());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonArrayGeo;
    }

    public JSONArray getJsonArraySchedule() {
        if(jsonArraySchedule.length()==0){
            for (Schedule s: getScheduleList()) {
                try {
                    jsonArraySchedule.put(s.getJSON());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonArraySchedule;
    }

    public ArrayList<Schedule> getScheduleList() {
        if(scheduleList.size() == 0 ){
            getSchedule();
        }
        return scheduleList;
    }

    public ArrayList<String> getInformList() {
        if(informList.size() == 0 ){
            getInformation();
        }
        return informList;
    }

    public ArrayList<MyNotification> getNotificationList() {
        if(notificationList.size() == 0 ){
            getNotifications();
        }
        return notificationList;
    }

    public HashSet<String> getIpList() {
        if(ipList.size() == 0 ){
            getClientIp();
        }
        return ipList;
    }

    public ArrayList<Geopoint> getGeopointList() {
        if(geopointList.size() == 0 ){
            getGeopoints();
        }
        return geopointList;
    }

    private WorkWithDb(Context context) {
        myOH = new MyOH(context);
        db = myOH.getWritableDatabase();
        informList = new ArrayList<>();
        scheduleList = new ArrayList<>();
        geopointList = new ArrayList<>();
        notificationList = new ArrayList<>();
        ipList = new HashSet<>();
        jsonObjectInform = new JSONObject();
        jsonArrayGeo= new JSONArray();
        jsonArraySchedule=new JSONArray();
    }

    public static WorkWithDb getWorkWithDb(Context context){
        if(workWithDb==null){
            workWithDb = new WorkWithDb(context);
        }
        return workWithDb;
    }
    //!!! need to be created before
    public static WorkWithDb getWorkWithDb(){
        return workWithDb;
    }

    private ArrayList<String> getInformation()  {

        cursor = db.rawQuery("SELECT * FROM information", null);
        int size = cursor.getCount();

        try {
            cursor.moveToFirst();
            informList.add(0, cursor.getString(0));
            informList.add(1,cursor.getString(1));
            informList.add(2, cursor.getString(2));
            informList.add(3, cursor.getString(3));
            informList.add(4, cursor.getString(4));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return informList;

        }


    private ArrayList<Schedule> getSchedule(){
        String time;
        String description;
        cursor = db.rawQuery("SELECT * FROM schedule", null);
        int size = cursor.getCount();
        if (size > 0){
            cursor.moveToFirst();
            for (int i = 0; i < size; i++) {
                time = cursor.getString(1);
                description = cursor.getString(2);
                scheduleList.add(new Schedule(time, description));
                cursor.moveToNext();
            }
        }

        return scheduleList;
    }
    private ArrayList<MyNotification> getNotifications(){
         String sentTo;
         String text;
        cursor = db.rawQuery("SELECT * FROM notifications", null);
        int size = cursor.getCount();
        if (size > 0){
            cursor.moveToFirst();
            for (int i = 0; i < size; i++) {
                sentTo = cursor.getString(1);
                text = cursor.getString(2);
                notificationList.add(new MyNotification(sentTo, text));
                cursor.moveToNext();
            }
        }

        return notificationList;
    }

    public JSONObject getJsonObjectInform()  {
        if(jsonObjectInform.length()==0){
            try {
                jsonObjectInform.put("guide_name",getInformList().get(0));
                jsonObjectInform.put("guide_phone",getInformList().get(1));
                jsonObjectInform.put("tour",getInformList().get(2));
                jsonObjectInform.put("goal",getInformList().get(3));
                jsonObjectInform.put("company",getInformList().get(4));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObjectInform;
    }

    private HashSet<String> getClientIp(){
        String ip;
        cursor = db.rawQuery("SELECT * FROM mygroup", null);
        int size = cursor.getCount();
        if (size > 0){
            cursor.moveToFirst();
            for (int i = 0; i < size; i++) {
                ip = cursor.getString(1);
                //description = cursor.getString(1);
                ipList.add(ip);
                cursor.moveToNext();
            }
        }

        return ipList;
    }
    private ArrayList<Geopoint> getGeopoints(){
        //int id; // ! add to Geopoint
        String name;
        String type;
        int color;
        double[] coordinates = new double[2];

        cursor = db.rawQuery("SELECT * FROM geopoints", null);

        int size = cursor.getCount();
        if (size > 0){
            cursor.moveToFirst();
            for (int i = 0; i < size; i++) {
                name = cursor.getString(1);
                type = cursor.getString(2);
                color = cursor.getInt(3);
                coordinates[0] = cursor.getDouble(4);
                coordinates[1] = cursor.getDouble(5);
                geopointList.add(new Geopoint( name, type,color,coordinates));
                cursor.moveToNext();
            }
        }

        return geopointList;
    }

    public void updateGeopointByIndex(int index, String name, String type, int color, double[] coordinates){
        Geopoint geopoint = geopointList.get(index);
        db.execSQL("UPDATE geopoints set name='" + name + "',type='" + type+"',color= " +
                color + ", lat=" + coordinates[0] +", lon=" + coordinates[1] +" WHERE id=" + index + " ");

        geopoint.setName(name);
        geopoint.setType(type);
        geopoint.setColor(color);
        geopoint.setCoordinates(coordinates);
    }

    public void updateSchedualByIndex(int index, String time, String description){
        Schedule schedule = scheduleList.get(index);
        db.execSQL("UPDATE schedule set time='" + time + "',description='" + description +
                " WHERE id=" + index + "");

        schedule.setTime(time);
        schedule.setDescription(description);
    }

    public void updateInformationByIndex(String guideName, String guidePhone, String tour, String goal,String company){

        informList.set(0,guideName);
        informList.set(1, guidePhone);
        informList.set(2, tour);
        informList.set(3, goal);
        informList.set(4, company);

        db.execSQL("UPDATE information set guide_name='" + guideName + "', " +
                "guide_phone='" + guidePhone + "', tour='" + tour + "', goal='" + goal + "'");

    }
    public void updateInformationByIndex(String guideName, String guidePhone, String tour, String goal) {
        informList.set(0, guideName);
        informList.set(1, guidePhone);
        informList.set(2, tour);
        informList.set(3, goal);

        db.execSQL("UPDATE information set guide_name='" + guideName + "', " +
                "guide_phone='" + guidePhone + "', tour='" + tour + "', goal='" + goal + "' WHERE company='"+informList.get(4)+"' ");

    }
    public void deleteSchedualByIndex(int index){
       scheduleList.remove(index);
        db.execSQL("DELETE FROM schedule WHERE  id=" + index + "");
    }
    public void deleteGeopointByIndex(int index){
        geopointList.remove(index);
        db.execSQL("DELETE FROM geopoints WHERE id=" + index + "");
    }


    public void addGeopiont(String name, String type, int color, double[] coordinates){
        int index =getGeopointList().size();
        getGeopointList().add(new Geopoint(name, type, color, coordinates));
        db.execSQL("INSERT INTO geopoints VALUES (" + index + ", '" + name + "', '" + type + "', " +
                color + ", " + coordinates[0] + ", " + coordinates[1] + ")");

    }

    public void addSchedule(String time, String description){
        int index =getScheduleList().size();
        getScheduleList().add(new Schedule(time, description));
        db.execSQL("INSERT INTO schedule VALUES (" + index + ", '" + time + "', '" + description + "')");

    }

    public void addNotification(String sentTo, String text){
        int index =getNotificationList().size();
        getNotificationList().add(new MyNotification(sentTo, text));
        db.execSQL("INSERT INTO notifications VALUES (" + index + ", '" + sentTo + "', '" + text + "')");

    }

    public void addInformation(String guideName, String guidePhone, String tour, String goal, String company) {
        informList.add(0,guideName);
        informList.add(1,guidePhone);
        informList.add(2,tour);
        informList.add(3,goal);
        informList.add(4,company);
        db.execSQL("INSERT INTO information VALUES (0,'" + guideName + "', '" + guidePhone + "', '" + tour + "','" + goal + "','" + company + "')");

    }
    public void addIp(String ip ){
        int inf_id = getIpList().size();
        db.execSQL("INSERT INTO mygroup VALUES ('" + inf_id + "','" + ip + "')");
    }
    public JSONArray CreateIdJsonArray() {
        JSONArray jsonArray;
        jsonArray = new JSONArray();

        for (String key : ipList) {
            jsonArray.put(key);
        }

        return jsonArray;
    }

}
