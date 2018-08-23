package com.ezzat.lawyer.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezzat.lawyer.Model.Client;
import com.ezzat.lawyer.Model.User;
import com.ezzat.lawyer.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class Login_Register extends AppCompatActivity {

    private boolean isSigninScreen = true;
    private TextView tvSignupInvoker;
    private LinearLayout llSignup;
    private TextView tvSigninInvoker;
    private LinearLayout llSignin;
    private Button btnSignup;
    private Button btnSignin;
    private EditText emailETLogin, passwordETLogin, emailETReg, passwordETReg;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ProgressDialog pDialog;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__register);
        MobileAds.initialize(this, "ca-app-pub-7974260876012872~8453391049");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        setupDesign();
        bindViews();
        setListeners();
    }

    @Override
    public void onStart() {
        super.onStart();
        User current = loadUserItExsist();
        Client client = loadClientItExsist();
        if (current != null)
            goToHome(current, client);
    }

    public static String hashPassword(String pass) {
        int hash = 7;
        for (int i = 0; i < pass.length(); i++) {
            hash = hash*31 + pass.charAt(i);
        }
        return String.valueOf(hash);
    }

    private void goToHome(User user, Client client) {
        Intent intent = new Intent(Login_Register.this, Home.class);
        intent.putExtra("user", user);
        intent.putExtra("client", client);
        startActivity(intent);
    }

    private void setListeners() {
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginUser().execute(emailETLogin.getText().toString(), hashPassword(passwordETLogin.getText().toString()), "1");
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginUser().execute(emailETReg.getText().toString(), hashPassword(passwordETReg.getText().toString()), "0");
            }
        });
    }
        private void bindViews () {
            emailETLogin = findViewById(R.id.emaillogin);
            passwordETLogin = findViewById(R.id.passlogin);
            emailETReg = findViewById(R.id.emailreg);
            passwordETReg = findViewById(R.id.passreg);
        }

        private void setupDesign () {
            llSignin = findViewById(R.id.llSignin);
            tvSignupInvoker = findViewById(R.id.tvSignupInvoker);
            tvSigninInvoker = findViewById(R.id.tvSigninInvoker);
            btnSignup = findViewById(R.id.btnSignup);
            btnSignin = findViewById(R.id.btnSignin);

            llSignup = findViewById(R.id.llSignup);
            llSignin = findViewById(R.id.llSignin);

            tvSignupInvoker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isSigninScreen = false;
                    showSignupForm();
                }
            });

            tvSigninInvoker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isSigninScreen = true;
                    showSigninForm();
                }
            });
            showSigninForm();

            btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_right_to_left);
                    if (isSigninScreen)
                        btnSignup.startAnimation(clockwise);
                }
            });

        }

        private void showSignupForm () {
            PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
            PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
            infoLogin.widthPercent = 0.15f;
            llSignin.requestLayout();


            PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
            PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
            infoSignup.widthPercent = 0.85f;
            llSignup.requestLayout();

            tvSignupInvoker.setVisibility(View.GONE);
            tvSigninInvoker.setVisibility(View.VISIBLE);
            Animation translate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right_to_left);
            llSignup.startAnimation(translate);

            Animation clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_right_to_left);
            btnSignup.startAnimation(clockwise);

        }

        private void showSigninForm () {
            PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
            PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
            infoLogin.widthPercent = 0.85f;
            llSignin.requestLayout();


            PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
            PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
            infoSignup.widthPercent = 0.15f;
            llSignup.requestLayout();

            Animation translate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_left_to_right);
            llSignin.startAnimation(translate);

            tvSignupInvoker.setVisibility(View.VISIBLE);
            tvSigninInvoker.setVisibility(View.GONE);
            Animation clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_left_to_right);
            btnSignin.startAnimation(clockwise);
        }

        private class LoginUser extends AsyncTask<String, String, String> {
            AlertDialog alertDialog;
            String username;
            String password;
            boolean isUser;
            User value;

            /**
             * Before starting background thread Show Progress Dialog
             * */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                alertDialog = new AlertDialog.Builder(Login_Register.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                pDialog = new ProgressDialog(Login_Register.this);
                pDialog.setMessage("Signing in...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected String doInBackground(String... args) {
                username = args[0];
                password = args[1];
                isUser = args[2].equals("0")? false : true;
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("Users");
                DatabaseReference userReq = myRef.child(username);
                // Read from the database
                userReq.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        value = dataSnapshot.getValue(User.class);
                        if (value == null || password == null || !value.password.equals(password)) {
                            Toast.makeText(getApplicationContext(), "خطأ في التسجل , حاول مره اخرى", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (value.user != isUser) {
                            Toast.makeText(getApplicationContext(), "خطأ في التسجل , حاول مره اخرى", Toast.LENGTH_LONG).show();
                            return;
                        }
                        database.getReference("Clients").child(value.username).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Client cli = dataSnapshot.getValue(Client.class);
                                saveItToPreference(value, cli);
                                goToHome(value,cli);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                FirebaseCrash.log("Faild To get Client for Login");
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w("dodo", "Failed to read value.", error.toException());
                        FirebaseCrash.log("Faild To get User for Login");
                        Toast.makeText(getApplicationContext(), "خطأ في التسجل , حاول مره اخرى", Toast.LENGTH_LONG).show();
                    }
                });
                return null;
            }

            protected void onPostExecute(String file_url) {
                pDialog.dismiss();
            }
        }

    public User loadUserItExsist() {
        SharedPreferences pref = getApplication().getSharedPreferences("user", 0);
        Gson gson = new Gson();
        String json = pref.getString("now", "");
        User it = gson.fromJson(json, User.class);
        return it;
    }

    public Client loadClientItExsist() {
        SharedPreferences pref = getApplication().getSharedPreferences("user", 0);
        Gson gson = new Gson();
        String json = pref.getString("cl", "");
        Client it = gson.fromJson(json, Client.class);
        return it;
    }

        public void saveItToPreference(User user, Client client){
            SharedPreferences pref = getSharedPreferences("user", 0);
            Gson gson = new Gson();
            SharedPreferences.Editor editor = pref.edit();
            String json = gson.toJson(user);
            String json2 = gson.toJson(client);
            editor.putString("now", json);
            editor.putString("cl", json2);
            editor.commit(); // commit changes
        }
    }
