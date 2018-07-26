package com.ezzat.lawyer;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Lawyer extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
