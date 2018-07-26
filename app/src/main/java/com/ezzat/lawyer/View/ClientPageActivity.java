package com.ezzat.lawyer.View;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ezzat.lawyer.Controller.MyAdapterApoint;
import com.ezzat.lawyer.Controller.MyAdapterCase;
import com.ezzat.lawyer.Model.Apointment;
import com.ezzat.lawyer.Model.Case;
import com.ezzat.lawyer.Model.Client;
import com.ezzat.lawyer.Model.User;
import com.ezzat.lawyer.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClientPageActivity extends AppCompatActivity {

    private User user;
    private Client client;

    private TextView nameTV;
    private Button backBT;
    private Button deleteBT;
    private RecyclerView recCases;
    private RecyclerView recApoints;
    private FirebaseDatabase database;
    private ArrayList<Case> cases;
    private ArrayList<Apointment> apointments;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_page);
        user = (User) getIntent().getSerializableExtra("user");
        client = (Client) getIntent().getSerializableExtra("client");
        database = FirebaseDatabase.getInstance();
        setupViews();
        setValues();
        setupActions();
    }

    private void goBack() {
        Intent intent = new Intent(ClientPageActivity.this, Home.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void deleteFromFirebase() {
        myRef = database.getReference("Clients");
        myRef.child(client.getUsername()).removeValue();
        database.getReference("Users").child(client.getUsername()).removeValue();
        goBack();
    }

    private void setValues() {
        nameTV.setText(client.getUsername());
        getCases();
        getApointments();
        LinearLayoutManager MyLayoutManagerCase = new LinearLayoutManager(this);
        MyLayoutManagerCase.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager MyLayoutManagerApont = new LinearLayoutManager(this);
        MyLayoutManagerApont.setOrientation(LinearLayoutManager.HORIZONTAL);
        recCases.setAdapter(new MyAdapterCase(cases, getBaseContext(), user));
        recApoints.setAdapter(new MyAdapterApoint(apointments, getBaseContext(), user));
        recCases.setLayoutManager(MyLayoutManagerCase);
        recApoints.setLayoutManager(MyLayoutManagerApont);
    }

    private void setupViews() {
        nameTV = findViewById(R.id.name);
        backBT = findViewById(R.id.back);
        deleteBT = findViewById(R.id.del);
        recCases = findViewById(R.id.cardCases);
        recApoints = findViewById(R.id.cardApoint);
        cases = new ArrayList<>();
        apointments = new ArrayList<>();
    }

    private void setupActions() {
        backBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        deleteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromFirebase();
            }
        });
    }

    private void getCases() {
        Log.i("dodo", client.getUsername() + " sdsadaaaaaaaaaaa");
        String[] ca = client.getCasey().split("-");
        for (String x : ca) {
            getCaseFromFirebase(x);
        }
    }


    private void getCaseFromFirebase(final String x) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Cases").child(x != null? x : "");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Case c = dataSnapshot.getValue(Case.class);
                    cases.add(c);
                } else {
                    if (!x.equals(""))
                        Toast.makeText(getApplicationContext(),    x + "عذرا, القضية رقم " + " غير موجودة", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getApointments() {
        String[] ca = client.getApointments().split("-");
        for (String x : ca) {
            getApointsFromFirebase(x);
        }
    }


    private void getApointsFromFirebase(final String x) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Apointments").child(x != null? x : "");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Apointment c = dataSnapshot.getValue(Apointment.class);
                    apointments.add(c);
                } else {
                    if (!x.equals(""))
                        Toast.makeText(getApplicationContext(),   "عذرا, الموعد رقم "  + x + " غير موجودة", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
