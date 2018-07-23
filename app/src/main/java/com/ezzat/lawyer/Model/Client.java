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
    private ArrayList<Case> casesArray = new ArrayList<>();

    public Client() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Client(String username, String password, String cases) {
        this.username = username;
        this.password = password;
        this.cases = cases;
    }

    private void getCases() {
        String[] ca = cases.split("-");
        for (String x : ca) {
            Case caseIt = null;
            try {
                caseIt = getCaseFromFirebase(x);
            } catch (Exception i) {
                //Toast.makeText(,"رقم القضية غير موجودة", Toast.LENGTH_LONG).show();
                Log.i("dodo", "رقم القضية غير موجودة");
            }
            if (caseIt != null)
                casesArray.add(caseIt);
        }
    }


    private Case getCaseFromFirebase(String x) {
        FirebaseDatabase database;
        DatabaseReference myRef;
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Cases");
        DatabaseReference userReq = myRef.child(x);
        // Read from the database
        userReq.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Case ca = dataSnapshot.getValue(Case.class);
                casesArray.add(ca);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("dodo", "Failed to read value.", error.toException());
            }
        });
        return null;
    }

    public ArrayList<Case> getCasesArray() {
        return casesArray;
    }

    public String getUsername() {
        return username;
    }

    public String getCasey() {
        return cases;
    }
}
