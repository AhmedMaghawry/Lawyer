package com.ezzat.lawyer.Model;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class Client implements Serializable {

    String cases;
    String username;
    String password;
    String apointments;

    public Client() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Client(String username, String password, String cases, String apointments) {
        this.username = username;
        this.password = password;
        this.cases = cases;
        this.apointments = apointments;
    }

    public String getUsername() {
        return username;
    }

    public String getCasey() {
        return cases;
    }

    public String getApointments() {
        return apointments;
    }
}
