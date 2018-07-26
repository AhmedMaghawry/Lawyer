package com.ezzat.lawyer.View;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ezzat.lawyer.Controller.CaseListAdapter;
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

public class CasePageActivity extends AppCompatActivity {

    private User user;
    private Client client;
    private Case aCase;

    private TextView nameTV;
    private TextView numTV;
    private TextView typeTV;
    private TextView locTV;
    private TextView dateTV;
    private TextView descTV;
    private Button updateBT;
    private Button apointBT;
    private Button clientBT;
    private Button backBT;
    private Button doneBT;
    private Button deleteBT;
    private FloatingActionButton prog;
    private FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_page);
        user = (User) getIntent().getSerializableExtra("user");
        client = (Client) getIntent().getSerializableExtra("client");
        aCase = (Case) getIntent().getSerializableExtra("case");
        database = FirebaseDatabase.getInstance();
        setupViews();
        setValues();
        setupActions();
    }

    private void goBack() {
        Intent intent = new Intent(CasePageActivity.this, Home.class);
        intent.putExtra("user", user);
        intent.putExtra("client", client);
        startActivity(intent);
    }

    private void deleteFromFirebase() {
        myRef = database.getReference("Cases");
        myRef.child(aCase.getNum()).removeValue();
        goBack();
    }

    private void updateDate() {
        showDialog();
    }

    private void getClientAndGo() {
        database = FirebaseDatabase.getInstance();
        Log.i("dodo", aCase.getClient());
        myRef = database.getReference("Clients").child(aCase.getClient() != null? aCase.getClient() : "");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Client c = dataSnapshot.getValue(Client.class);
                    Log.i("dodo", "Caseeee " + c.getUsername());
                    Intent intent = new Intent(CasePageActivity.this, ClientPageActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("client", c);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),"عذرا, الموكل غير موجود", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getApointmentAndGo() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Apointments").child(aCase.getApointment() != null ? aCase.getApointment() : "");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Apointment c = dataSnapshot.getValue(Apointment.class);
                    Intent intent = new Intent(CasePageActivity.this, ApointmentPageActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("apointment", c);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),"عذرا, الموعد غير موجود", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateDone() {
        myRef = database.getReference("Cases");
        myRef.child(aCase.getNum()).child("done").setValue(true);
        goBack();
    }

    @SuppressLint("ResourceAsColor")
    private void setValues() {
        nameTV.setText(aCase.getName());
        numTV.setText(aCase.getNum());
        typeTV.setText(aCase.getType());
        locTV.setText(aCase.getLocation());
        dateTV.setText(aCase.getDate());
        descTV.setText(aCase.getDesc());
        if (aCase.getDone()) {
            prog.setImageResource(R.drawable.ic_ticky);
            prog.setBackgroundTintList(ColorStateList.valueOf(R.color.white));
            doneBT.setEnabled(false);
            doneBT.setVisibility(View.INVISIBLE);
        } else {
            prog.setImageResource(R.drawable.ic_load);
            prog.setBackgroundTintList(ColorStateList.valueOf(R.color.white));
        }
        if (user.user) {
            doneBT.setVisibility(View.INVISIBLE);
            updateBT.setVisibility(View.INVISIBLE);
            clientBT.setVisibility(View.INVISIBLE);
            deleteBT.setVisibility(View.INVISIBLE);
        }
    }

    private void setupViews() {
        nameTV = findViewById(R.id.name);
        numTV = findViewById(R.id.num);
        typeTV = findViewById(R.id.type);
        locTV = findViewById(R.id.loc);
        dateTV = findViewById(R.id.time);
        descTV = findViewById(R.id.desc);
        updateBT = findViewById(R.id.updateDate);
        apointBT = findViewById(R.id.apoint);
        clientBT = findViewById(R.id.client);
        backBT = findViewById(R.id.back);
        doneBT = findViewById(R.id.done);
        deleteBT = findViewById(R.id.del);
        prog = findViewById(R.id.prog);
    }

    private void setupActions() {
        updateBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();
            }
        });

        apointBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApointmentAndGo();
            }
        });

        clientBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClientAndGo();
            }
        });

        backBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        doneBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDone();
            }
        });

        deleteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromFirebase();
            }
        });
    }

    private void showDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_et);
        dialog.setTitle("تحديث الموعد");

        // set the custom dialog components - text, button
        final EditText text = dialog.findViewById(R.id.edit);
        Button dialogCan = dialog.findViewById(R.id.can);
        // if button is clicked, close the custom dialog
        dialogCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button dialogOk = dialog.findViewById(R.id.done);
        // if button is clicked
        dialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = text.getText().toString();
                if (val != null) {
                    dialog.dismiss();
                    myRef = database.getReference("Cases");
                    myRef.child(aCase.getNum()).child("date").setValue(val);
                    dateTV.setText(val);
                }
            }
        });

        dialog.show();
    }
}
