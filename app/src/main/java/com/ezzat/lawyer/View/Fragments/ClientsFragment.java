package com.ezzat.lawyer.View.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.ezzat.lawyer.Controller.MyListAdapter;
import com.ezzat.lawyer.Model.Client;
import com.ezzat.lawyer.Model.User;
import com.ezzat.lawyer.R;
import com.ezzat.lawyer.View.AddClientActivity;
import com.ezzat.lawyer.View.Home;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClientsFragment extends Fragment {

    private FloatingActionButton floatingActionButton;
    private ListView list;
    private ArrayList<Client> clients;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_clients, container, false);
        new loadClients().execute();
        floatingActionButton = view.findViewById(R.id.add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddClientActivity.class);
                intent.putExtra("user", ((Home)getActivity()).getUser());
                startActivity(intent);
            }
        });
        return view;
    }

    private class loadClients extends AsyncTask<String, String, String>{

        private FirebaseDatabase database;
        private DatabaseReference myRef;

        @Override
        protected String doInBackground(String... strings) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Clients");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    clients = new ArrayList<>();
                    for (DataSnapshot x : dataSnapshot.getChildren()) {
                        Client c = x.getValue(Client.class);
                        clients.add(c);
                    }
                    MyListAdapter adapter = new MyListAdapter(getActivity(), getIds(clients), getCases(clients));
                    list = view.findViewById(R.id.list);
                    list.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }
    }

    private String[] getIds(ArrayList<Client> clients) {
        String[] res = new String[clients.size()];
        int i = 0;
        for (Client c : clients) {
            res[i] = c.getUsername();
            i++;
        }
        return res;
    }

    private String[] getCases(ArrayList<Client> clients) {
        String[] res = new String[clients.size()];
        int i = 0;
        for (Client c : clients) {
            res[i] = c.getCasey();
            i++;
        }
        return res;
    }
}
