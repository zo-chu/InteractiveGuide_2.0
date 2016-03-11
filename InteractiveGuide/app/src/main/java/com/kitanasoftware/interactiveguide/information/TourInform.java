package com.kitanasoftware.interactiveguide.information;

import java.io.Serializable;

/**
 * Created by dasha on 29/01/16.
 */
public class TourInform extends Information implements Serializable{
    private String name;
    private String goal;

    public TourInform(InformType type, String name, String goal) {
        super(type);
        this.name = name;
        this.goal = goal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
