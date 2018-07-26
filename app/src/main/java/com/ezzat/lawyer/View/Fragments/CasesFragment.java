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

import com.ezzat.lawyer.Controller.CaseListAdapter;
import com.ezzat.lawyer.Controller.ClientListAdapter;
import com.ezzat.lawyer.Model.Case;
import com.ezzat.lawyer.Model.Client;
import com.ezzat.lawyer.Model.User;
import com.ezzat.lawyer.R;
import com.ezzat.lawyer.View.AddCaseActivity;
import com.ezzat.lawyer.View.AddClientActivity;
import com.ezzat.lawyer.View.CasePageActivity;
import com.ezzat.lawyer.View.Home;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CasesFragment extends Fragment {

    private FloatingActionButton floatingActionButton;
    private ListView list;
    private ArrayList<Case> cases;
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
        view = inflater.inflate(R.layout.fragment_cases, container, false);
        user = ((Home)getActivity()).getUser();
        client = ((Home)getActivity()).getClient();
        list = view.findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), CasePageActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("client", client);
                intent.putExtra("case", cases.get(position));
                startActivity(intent);
            }
        });
        new loadCases().execute();
        floatingActionButton = view.findViewById(R.id.add);
        if (user.user)
            floatingActionButton.setVisibility(View.GONE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddCaseActivity.class);
                intent.putExtra("user", ((Home)getActivity()).getUser());
                startActivity(intent);
            }
        });
        return view;
    }

    private class loadCases extends AsyncTask<String, String, String> {

        private FirebaseDatabase database;
        private DatabaseReference myRef;

        @Override
        protected String doInBackground(String... strings) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Cases");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    cases = new ArrayList<>();
                    for (DataSnapshot x : dataSnapshot.getChildren()) {
                        Case c = x.getValue(Case.class);
                        if (!user.user)
                            cases.add(c);
                        else {
                            if (isInCase(c.getNum(), client.getCasey()))
                                cases.add(c);
                        }
                    }
                    CaseListAdapter adapter = new CaseListAdapter(getActivity(), getNames(cases), getTypes(cases), getDesc(cases), getDates(cases));
                    list.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }
    }

    private String[] getNames(ArrayList<Case> cases) {
        String[] res = new String[cases.size()];
        int i = 0;
        for (Case c : cases) {
            res[i] = c.getName();
            i++;
        }
        return res;
    }

    private String[] getTypes(ArrayList<Case> cases) {
        String[] res = new String[cases.size()];
        int i = 0;
        for (Case c : cases) {
            res[i] = c.getType();
            i++;
        }
        return res;
    }

    private String[] getDesc(ArrayList<Case> cases) {
        String[] res = new String[cases.size()];
        int i = 0;
        for (Case c : cases) {
            res[i] = c.getNum();
            i++;
        }
        return res;
    }

    private String[] getDates(ArrayList<Case> cases) {
        String[] res = new String[cases.size()];
        int i = 0;
        for (Case c : cases) {
            res[i] = c.getDate();
            i++;
        }
        return res;
    }

    private boolean isInCase(String num, String apointments) {
        String[] apos = apointments.split("-");
        for (String s : apos){
            if (s.equals(num))
                return true;
        }
        return false;
    }
}
