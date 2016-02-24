package com.kitanasoftware.interactiveguide.Schedule;

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

}
