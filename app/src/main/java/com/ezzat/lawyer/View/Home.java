package com.ezzat.lawyer.View;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ezzat.lawyer.Model.Client;
import com.ezzat.lawyer.Model.User;
import com.ezzat.lawyer.R;
import com.ezzat.lawyer.View.Fragments.CasesFragment;
import com.ezzat.lawyer.View.Fragments.ClientsFragment;
import com.ezzat.lawyer.View.Fragments.DatesFragment;
import com.ezzat.lawyer.View.Fragments.SettingFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class Home extends AppCompatActivity {

    private User user;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        user = (User) getIntent().getSerializableExtra("user");
        if (user.user) {
            client = (Client) getIntent().getSerializableExtra("client");
            defaultUserViews();
        } else {
            managerViews();
        }
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
                .add("اعدادات", SettingFragment.class)
                .create());

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
    }
}
