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
import android.widget.AdapterView;
import android.widget.ListView;

import com.ezzat.lawyer.Controller.CaseListAdapter;
import com.ezzat.lawyer.Controller.ChatListAdapter;
import com.ezzat.lawyer.Controller.ClientListAdapter;
import com.ezzat.lawyer.Model.Case;
import com.ezzat.lawyer.Model.Client;
import com.ezzat.lawyer.Model.User;
import com.ezzat.lawyer.R;
import com.ezzat.lawyer.View.AddCaseActivity;
import com.ezzat.lawyer.View.CasePageActivity;
import com.ezzat.lawyer.View.ChatActivity;
import com.ezzat.lawyer.View.Home;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    private ListView list;
    private View view;
    private User user;
    private Client client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentt
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        user = ((Home)getActivity()).getUser();
        client = ((Home)getActivity()).getClient();
        list = view.findViewById(R.id.list);
        new loadChat().execute();
        return view;
    }

    private class loadChat extends AsyncTask<String, String, String> {

        private FirebaseDatabase database;
        private DatabaseReference myRef;

        @Override
        protected String doInBackground(String... strings) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Clients");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String[] username = new String[(int) dataSnapshot.getChildrenCount()];
                    int count = 0;
                    for (DataSnapshot x : dataSnapshot.getChildren()) {
                        Client c = x.getValue(Client.class);
                        username[count] = c.getUsername();
                        count++;
                        Log.i("dodoE", count+"");
                    }
                    ChatListAdapter adapter = new ChatListAdapter(getActivity(), username);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getContext(), ChatActivity.class);
                            intent.putExtra("admin", user);
                            intent.putExtra("username", username[position]);
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }
    }
}
