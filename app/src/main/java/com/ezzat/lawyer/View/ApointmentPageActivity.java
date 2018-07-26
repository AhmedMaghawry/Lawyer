package com.ezzat.lawyer.View;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class ApointmentPageActivity extends AppCompatActivity {

    private User user;
    private Client client;
    private Apointment apointment;

    private TextView nameTV;
    private TextView numTV;
    private TextView locTV;
    private TextView descTV;
    private TextView hourTV;
    private Button backBT;
    private Button deleteBT;
    private FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apointment_page);
        user = (User) getIntent().getSerializableExtra("user");
        client = (Client) getIntent().getSerializableExtra("client");
        apointment = (Apointment) getIntent().getSerializableExtra("apointment");
        database = FirebaseDatabase.getInstance();
        setupViews();
        setValues();
        setupActions();
    }

    private void goBack() {
        Intent intent = new Intent(ApointmentPageActivity.this, Home.class);
        intent.putExtra("user", user);
        intent.putExtra("client", client);
        startActivity(intent);
    }

    private void deleteFromFirebase() {
        myRef = database.getReference("Apointments");
        myRef.child(apointment.getNum()).removeValue();
        goBack();
    }

    private void setValues() {
        nameTV.setText(apointment.getDatey());
        numTV.setText(apointment.getNum());
        locTV.setText(apointment.getLocation());
        hourTV.setText(apointment.getHour());
        descTV.setText(apointment.getDesc());
        if (user.user)
            deleteBT.setVisibility(View.INVISIBLE);
    }

    private void setupViews() {
        nameTV = findViewById(R.id.name);
        numTV = findViewById(R.id.num);
        locTV = findViewById(R.id.loc);
        hourTV = findViewById(R.id.hour);
        descTV = findViewById(R.id.desc);
        backBT = findViewById(R.id.back);
        deleteBT = findViewById(R.id.del);
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
}
