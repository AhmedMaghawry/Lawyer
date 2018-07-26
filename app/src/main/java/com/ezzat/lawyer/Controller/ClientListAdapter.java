package com.ezzat.lawyer.Controller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ezzat.lawyer.R;

public class ClientListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] maintitle;
    private final String[] subtitle;

    public ClientListAdapter(Activity context, String[] maintitle, String[] subtitle) {
        super(context, R.layout.list_row_client, maintitle);

        this.context=context;
        this.maintitle=maintitle;
        this.subtitle=subtitle;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_row_client, null,true);
        TextView titleText = rowView.findViewById(R.id.username);
        TextView subtitleText = rowView.findViewById(R.id.cases);
        titleText.setText(maintitle[position]);
        subtitleText.setText(subtitle[position]);
        return rowView;
    }
}