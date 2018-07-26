package com.ezzat.lawyer.Controller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ezzat.lawyer.R;

public class ApointmentListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] location;
    private final String[] hour;
    private final String[] date;

    public ApointmentListAdapter(Activity context, String[] date, String[] location, String[] hour) {
        super(context, R.layout.list_row_case, date);
        this.context=context;
        this.date = date;
        this.location = location;
        this.hour = hour;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_row_apoint, null,true);
        TextView dateTv = rowView.findViewById(R.id.datey);
        TextView locationTv = rowView.findViewById(R.id.loc);
        TextView hourTv = rowView.findViewById(R.id.hour);
        dateTv.setText(date[position]);
        locationTv.setText(location[position]);
        hourTv.setText(hour[position]);
        return rowView;
    }
}