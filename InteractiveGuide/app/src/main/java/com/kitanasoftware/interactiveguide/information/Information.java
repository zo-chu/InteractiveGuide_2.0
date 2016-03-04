package com.kitanasoftware.interactiveguide.information;

import android.graphics.Bitmap;

import com.kitanasoftware.interactiveguide.db.WorkWithDb;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by dasha on 27/01/16.
 */
public class Information implements Serializable{

    private InformType type;
    private static final long serialVersionUID = 1996156934189183983L;

    public enum InformType {
        GUIDE, TOUR, ADD
    }

    public Information(InformType type) {
        this.type =type;
    }

    public InformType getType() {
        return type;
    }

    public void setType(InformType type) {
        this.type = type;
    }

    public static void CreateFromJsonObject(JSONObject jsonObject) {
        try {
            String name = jsonObject.getString("guide_name");
            String phone= jsonObject.getString("guide_phone");
            String tour  = jsonObject.getString("tour");
            String goal  = jsonObject.getString("tour");
            String company  = jsonObject.getString("company");

            WorkWithDb workWithDb = WorkWithDb.getWorkWithDb();
            workWithDb.addInformation(name,phone,tour,goal,company);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
