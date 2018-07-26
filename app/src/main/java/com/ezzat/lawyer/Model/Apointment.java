package com.ezzat.lawyer.Model;

import java.io.Serializable;

public class Apointment implements Serializable {

    String num;
    String location;
    String desc;
    String datey;
    String hour;

    public Apointment() {}

    public Apointment(String num, String location, String desc, String datey, String hour) {
        this.num = num;
        this.location = location;
        this.desc = desc;
        this.datey = datey;
        this.hour = hour;
    }

    public String getDatey() {
        return datey;
    }

    public String getHour() {
        return hour;
    }

    public String getLocation() {
        return location;
    }

    public String getNum() {
        return num;
    }

    public String getDesc() {
        return desc;
    }
}
