package com.kitanasoftware.interactiveguide.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kitanasoftware.interactiveguide.Schedule.Schedule;
import com.kitanasoftware.interactiveguide.information.AdditionalInform;
import com.kitanasoftware.interactiveguide.information.GuideInform;
import com.kitanasoftware.interactiveguide.information.Information;
import com.kitanasoftware.interactiveguide.information.TourInform;
import com.kitanasoftware.interactiveguide.map.Geopoint;

import org.json.JSONArray;
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

    private ArrayList<Information> informList;
    private ArrayList<Schedule> scheduleList;
    private ArrayList<Geopoint> geopointList;

    private HashSet<String> ipList;

    private JSONObject jsonObjectInform;

    private JSONArray jsonArrayGeo;

    private JSONArray jsonArraySchedule;


    public JSONArray getJsonArrayGeo() {
        for (Geopoint g: getGeopointList()) {
                try {
                    jsonArrayGeo.put(g.createJSON());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        return jsonArrayGeo;
    }

    public JSONArray getJsonArraySchedule() {
        for (Schedule s: getScheduleList()) {
            try {
                jsonArraySchedule.put(s.getJSON());
            } catch (Exception e) {
                e.printStackTrace();
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

    public ArrayList<Information> getInformList() {
        if(informList.size() == 0 ){
            getInformation();
        }
        return informList;
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

    private ArrayList<Information> getInformation()  {

        GuideInform guideInform;
        TourInform tourInform;
        AdditionalInform additionalInform;

        cursor = db.rawQuery("SELECT * FROM information", null);
        int size = cursor.getCount();
        if (size > 0){
            try {
                cursor.moveToFirst();
                jsonObjectInform.put("guide_name", cursor.getString(0));
                jsonObjectInform.put("guide_phone", cursor.getString(1));
                guideInform = new GuideInform(Information.InformType.GUIDE,
                        cursor.getString(0), cursor.getString(1));
                jsonObjectInform.put("tour", cursor.getString(2));
                jsonObjectInform.put("goal", cursor.getString(3));
                tourInform = new TourInform(Information.InformType.TOUR,
                        cursor.getString(2), cursor.getString(3));
                jsonObjectInform.put("company", cursor.getString(4));
                additionalInform = new AdditionalInform(Information.InformType.ADD, cursor.getString(4));
                informList.add(0, guideInform);
                informList.add(1, tourInform);
                informList.add(2, additionalInform);
            } catch (Exception e) {
                e.printStackTrace();
            }



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
                description = cursor.getString(1);
                scheduleList.add(new Schedule(time, description));
                cursor.moveToNext();
            }
        }

        return scheduleList;
    }

    public JSONObject getJsonObjectInform() {
        if(jsonObjectInform.length()==0){
            getInformation();
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

    public void updateInformationByIndex(String guideName, String guidePhone, String tour, String goal){

        ((GuideInform)informList.get(0)).setFull_name(guideName);
        ((GuideInform)informList.get(0)).setPhone(guidePhone);
        ((TourInform)informList.get(1)).setName(tour);
        ((TourInform)informList.get(1)).setGoal(goal);

        db.execSQL("UPDATE information set guide_name='" + guideName + "', " +
                "guide_phone='" + guidePhone + "', tour='" + tour + "', goal='" + goal + "'");

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

    public void addInformation(String guideName, String guidePhone, String tour, String goal, String company ){
        int inf_id = getInformList().size();
        db.execSQL("INSERT INTO information VALUES ('" + guideName + "', '" + guidePhone + "', '" + tour + "','" + goal + "','" + company + "')");
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
