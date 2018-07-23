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

import com.ezzat.lawyer.Model.Client;
import com.ezzat.lawyer.Model.User;
import com.ezzat.lawyer.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddClientActivity extends AppCompatActivity {

    private User user;
    private Client client;
    private EditText username;
    private EditText password;
    private EditText casee;
    private ImageButton confirm;
    private ImageButton reject;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
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
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        casee = findViewById(R.id.casey);
        confirm = findViewById(R.id.tick);
        reject = findViewById(R.id.cross);
    }

    private void confirm() {
        new addToFirebase().execute();
    }

    private void goBack() {
        Intent intent = new Intent(AddClientActivity.this, Home.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    class addToFirebase extends AsyncTask<String, String, String> {

        AlertDialog alertDialog;
        String usernameVal;
        String passwordVal;
        String cases;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(AddClientActivity.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            pDialog = new ProgressDialog(AddClientActivity.this);
            pDialog.setMessage("تحميل التسجيل...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            usernameVal = username.getText().toString();
            passwordVal = password.getText().toString();
            cases = casee.getText().toString();
            client = new Client(usernameVal, Login_Register.hashPassword(passwordVal), cases);
            final User user = new User(usernameVal, Login_Register.hashPassword(passwordVal), true);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Clients");
            final DatabaseReference ch = myRef.child(username.getText().toString());
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
                        ch.setValue(client);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(),"تم التسجيل", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            DatabaseReference myRefUser = database.getReference("Users");
            final DatabaseReference us = myRefUser.child(username.getText().toString());
            us.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {

                    } else {
                        us.setValue(user);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            goBack();
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }
    }
}
