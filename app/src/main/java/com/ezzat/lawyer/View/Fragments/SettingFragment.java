package com.ezzat.lawyer.View.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ezzat.lawyer.Model.User;
import com.ezzat.lawyer.R;
import com.ezzat.lawyer.View.Home;
import com.ezzat.lawyer.View.Login_Register;

public class SettingFragment extends Fragment {

    Button logout;
    Button msg;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        user = ((Home)getActivity()).getUser();
        logout = view.findViewById(R.id.logout);
        msg = view.findViewById(R.id.msg);
        if (user.user)
            msg.setVisibility(View.VISIBLE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getContext().getSharedPreferences("user", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(getContext(), Login_Register.class));
            }
        });
        return view;
    }
}
