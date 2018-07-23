package com.ezzat.lawyer.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

@IgnoreExtraProperties
public class User implements Serializable {

    public String username;
    public String password;
    public boolean user;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String password, boolean user) {
        this.username = username;
        this.password = password;
        this.user = user;
    }
}