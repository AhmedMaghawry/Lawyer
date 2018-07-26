package com.ezzat.lawyer.View.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ezzat.lawyer.Controller.ApointmentListAdapter;
import com.ezzat.lawyer.Controller.CaseListAdapter;
import com.ezzat.lawyer.Model.Apointment;
import com.ezzat.lawyer.Model.Case;
import com.ezzat.lawyer.Model.Client;
import com.ezzat.lawyer.Model.User;
import com.ezzat.lawyer.R;
import com.ezzat.lawyer.View.AddApointmentActivity;
import com.ezzat.lawyer.View.AddCaseActivity;
import com.ezzat.lawyer.View.ApointmentPageActivity;
import com.ezzat.lawyer.View.CasePageActivity;
import com.ezzat.lawyer.View.Home;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatesFragment extends Fragment {

    private FloatingActionButton floatingActionButton;
    private ListView list;
    private ArrayList<Apointment> apointments;
    private View view;
    private User user;
    private Client client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dates, container, false);
        user = ((Home)getActivity()).getUser();
        client = ((Home)getActivity()).getClient();
        list = view.findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ApointmentPageActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("client", client);
                intent.putExtra("apointment", apointments.get(position));
                startActivity(intent);
            }
        });
        new loadApointments().execute();
        floatingActionButton = view.findViewById(R.id.add);
        if (user.user)
            floatingActionButton.setVisibility(View.GONE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddApointmentActivity.class);
                intent.putExtra("user", ((Home)getActivity()).getUser());
                startActivity(intent);
            }
        });
        return view;
    }

    private class loadApointments extends AsyncTask<String, String, String> {

        private FirebaseDatabase database;
        private DatabaseReference myRef;

        @Override
        protected String doInBackground(String... strings) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Apointments");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    apointments = new ArrayList<>();
                    for (DataSnapshot x : dataSnapshot.getChildren()) {
                        Apointment c = x.getValue(Apointment.class);
                        if (!user.user)
                            apointments.add(c);
                        else {
                            if (isInApoint(c.getNum(), client.getApointments()))
                                apointments.add(c);
                        }
                    }
                    ApointmentListAdapter adapter = new ApointmentListAdapter(getActivity(), getDates(apointments), getLocations(apointments), getHours(apointments));
                    list.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }
    }

    private boolean isInApoint(String num, String apointments) {
        String[] apos = apointments.split("-");
        for (String s : apos){
            if (s.equals(num))
                return true;
        }
        return false;
    }

    private String[] getDates(ArrayList<Apointment> apointments) {
        String[] res = new String[apointments.size()];
        int i = 0;
        for (Apointment c : apointments) {
            res[i] = c.getDatey();
            i++;
        }
        return res;
    }

    private String[] getLocations(ArrayList<Apointment> apointments) {
        String[] res = new String[apointments.size()];
        int i = 0;
        for (Apointment c : apointments) {
            res[i] = c.getLocation();
            i++;
        }
        return res;
    }

    private String[] getHours(ArrayList<Apointment> apointments) {
        String[] res = new String[apointments.size()];
        int i = 0;
        for (Apointment c : apointments) {
            res[i] = c.getHour();
            i++;
        }
        return res;
    }
}
