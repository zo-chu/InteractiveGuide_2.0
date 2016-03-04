package com.kitanasoftware.interactiveguide.map;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Chudo on 26.01.2016.
 */
public class Geopoint  implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    String type;
    int color;
    double[] coordinates;
    JSONObject jsonObject;

    public Geopoint(String name, String type, int color, double[] coordinates) {
        this.name = name;
        this.type = type;
        this.color = color;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }


    public JSONObject createJSON(){
        Gson gson = new Gson();
        try {
            jsonObject = new JSONObject(gson.toJson(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }




}
