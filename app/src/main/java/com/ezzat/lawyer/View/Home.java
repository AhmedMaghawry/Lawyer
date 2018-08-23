package com.ezzat.lawyer.View;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.ezzat.lawyer.Model.Client;
import com.ezzat.lawyer.Model.User;
import com.ezzat.lawyer.R;
import com.ezzat.lawyer.View.Fragments.CasesFragment;
import com.ezzat.lawyer.View.Fragments.ChatFragment;
import com.ezzat.lawyer.View.Fragments.ClientsFragment;
import com.ezzat.lawyer.View.Fragments.DatesFragment;
import com.ezzat.lawyer.View.Fragments.SettingFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class Home extends AppCompatActivity {

    private User user;
    private Client client;
    private AdView mAdView;
    private ProgressDialog pDialog;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        user = (User) getIntent().getSerializableExtra("user");
        MobileAds.initialize(this, "ca-app-pub-7974260876012872~8453391049");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        showDial();
        if (user.user) {
            client = (Client) getIntent().getSerializableExtra("client");
            defaultUserViews();
        } else {
            managerViews();
        }
    }

    private void showDial() {
        alertDialog = new AlertDialog.Builder(Home.this).create();
        alertDialog.setTitle("Error");
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        pDialog = new ProgressDialog(Home.this);
        pDialog.setMessage("Signing in...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pDialog.dismiss();
    }

    public User getUser() {
        return user;
    }

    public Client getClient() {
        return client;
    }

    private void defaultUserViews() {
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("قـضــايــاك", CasesFragment.class)
                .add("مواعيدك", DatesFragment.class)
                .add("اعدادات", SettingFragment.class)
                .create());

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
    }

    private void managerViews() {
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("قـضــايــا", CasesFragment.class)
                .add("موكليـــن", ClientsFragment.class)
                .add("مواعيـــد", DatesFragment.class)
                .add("رســــائل", ChatFragment.class)
                .add("اعدادات", SettingFragment.class)
                .create());

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
    }
}
