package com.ezzat.lawyer.Model;

import java.io.Serializable;

public class Case implements Serializable {

    String name;
    String type;
    String num;
    String location;
    boolean done;
    String desc;

    public Case() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Case(String name, String type, String num, String location, boolean done, String desc) {
        this.name = name;
        this.type = type;
        this.num = num;
        this.location = location;
        this.done = done;
        this.desc = desc;
    }
}
