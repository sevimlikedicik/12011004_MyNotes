package com.murad.mobileproject.models;

import java.io.Serializable;

public class Reminder implements Serializable {
    private String date;
    private String time;
    private int period;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
