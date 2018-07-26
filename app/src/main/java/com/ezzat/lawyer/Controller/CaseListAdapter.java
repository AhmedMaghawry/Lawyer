package com.ezzat.lawyer.Controller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ezzat.lawyer.R;

public class CaseListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] name;
    private final String[] type;
    private final String[] desc;
    private final String[] date;

    public CaseListAdapter(Activity context, String[] name, String[] type, String[] desc, String[] date) {
        super(context, R.layout.list_row_case, name);
        this.context=context;
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.date = date;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_row_case, null,true);
        TextView nameTv = rowView.findViewById(R.id.name);
        TextView typeTv = rowView.findViewById(R.id.type);
        TextView descTv = rowView.findViewById(R.id.desc);
        TextView dateTv = rowView.findViewById(R.id.date);
        nameTv.setText(name[position]);
        typeTv.setText(type[position]);
        descTv.setText(desc[position]);
        dateTv.setText(date[position]);
        return rowView;
    }
}