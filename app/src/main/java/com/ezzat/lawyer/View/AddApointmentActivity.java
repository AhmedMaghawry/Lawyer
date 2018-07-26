package com.ezzat.lawyer.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ezzat.lawyer.Model.Apointment;
import com.ezzat.lawyer.Model.Case;
import com.ezzat.lawyer.Model.User;
import com.ezzat.lawyer.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddApointmentActivity extends AppCompatActivity {

    private User user;
    private Apointment apointment;
    private EditText hourET;
    private EditText numET;
    private EditText descET;
    private EditText locET;
    private EditText dateET;
    private ImageButton confirm;
    private ImageButton reject;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apointment);
        user = (User) getIntent().getSerializableExtra("user");
        setupViews();
        setupActions();
    }

    private void setupActions() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    private void setupViews() {
        hourET = findViewById(R.id.hour);
        numET = findViewById(R.id.num);
        descET = findViewById(R.id.desc);
        locET = findViewById(R.id.loc);
        dateET = findViewById(R.id.date);
        confirm = findViewById(R.id.tick);
        reject = findViewById(R.id.cross);
    }

    private void confirm() {
        new addApointment().execute();
    }

    private void goBack() {
        Intent intent = new Intent(AddApointmentActivity.this, Home.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    class addApointment extends AsyncTask<String, String, String> {

        AlertDialog alertDialog;
        String num;
        String hour;
        String loc;
        String desc;
        String date;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(AddApointmentActivity.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            pDialog = new ProgressDialog(AddApointmentActivity.this);
            pDialog.setMessage("تحميل الميعاد...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            //pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            num = numET.getText().toString();
            hour = hourET.getText().toString();
            loc = locET.getText().toString();
            desc = descET.getText().toString();
            date = dateET.getText().toString();
            apointment = new Apointment(num, loc, desc, date, hour);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Apointments");
            myRef.keepSynced(true);
            final DatabaseReference ch = myRef.child(num);
            ch.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "فشل التسجيل ,موجود من قبل", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        ch.setValue(apointment);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(),"تم التسجيل", Toast.LENGTH_LONG).show();
                            }
                        });
                        goBack();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }

        protected void onPostExecute(String file_url) {
            //pDialog.dismiss();
        }
    }
}
