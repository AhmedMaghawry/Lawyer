package com.ezzat.lawyer.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ezzat.lawyer.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class WelcomeActivity extends AppIntro {

    String titleFrag_1 = "Lawyer";
    int logo_1 = R.drawable.ic_law;
    String description_1 = "BLABLABLABLA";
    int backgroundColor = R.color.oxfordblue;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        boolean firstTime = pref.getBoolean("display", true); // getting boolean
        if (firstTime) {
            addSlide(AppIntroFragment.newInstance(titleFrag_1, description_1, logo_1, backgroundColor));
            addSlide(AppIntroFragment.newInstance(titleFrag_1, description_1, logo_1, backgroundColor));
            addSlide(AppIntroFragment.newInstance(titleFrag_1, description_1, logo_1, backgroundColor));
            showSkipButton(false);
            editor.putBoolean("display", false); // Storing boolean - true/false
            editor.commit();
        } else {
            startActivity(new Intent(WelcomeActivity.this, Login_Register.class));
            finish();
        }
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(WelcomeActivity.this, Login_Register.class));
        finish();
    }
}
