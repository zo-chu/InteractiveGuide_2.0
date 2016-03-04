package com.kitanasoftware.interactiveguide.Schedule;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dasha on 23/02/16.
 */
public class Schedule {
    private String time;
    private String description;

    public Schedule(String time, String description) {
        this.time = time;
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JSONObject getJSON(){
        JSONObject jsonObject = null;
        Gson gson = new Gson();
        try {
            jsonObject = new JSONObject(gson.toJson(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
