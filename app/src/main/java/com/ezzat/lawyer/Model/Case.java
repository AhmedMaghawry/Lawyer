package com.ezzat.lawyer.Model;

import java.io.Serializable;

public class Case implements Serializable {

    String name;
    String type;
    String num;
    String location;
    boolean done;
    String desc;
    String date;
    String apointment;
    String client;

    public Case() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Case(String name, String type, String num, String location, boolean done, String desc, String date, String apointment, String client) {
        this.name = name;
        this.type = type;
        this.num = num;
        this.location = location;
        this.done = done;
        this.desc = desc;
        this.date = date;
        this.apointment = apointment;
        this.client = client;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public String getDate() {
        return date;
    }

    public String getNum() {
        return num;
    }

    public String getLocation() {
        return location;
    }

    public String getApointment() {
        return apointment;
    }

    public String getClient() {
        return client;
    }

    public boolean getDone() {
        return done;
    }
}
