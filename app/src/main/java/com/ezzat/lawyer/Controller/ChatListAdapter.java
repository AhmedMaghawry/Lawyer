package com.ezzat.lawyer.Controller;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ezzat.lawyer.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] maintitle;

    public ChatListAdapter(Activity context, String[] maintitle) {
        super(context, R.layout.list_row_chat, maintitle);

        this.context=context;
        this.maintitle=maintitle;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_row_chat, null,true);
        TextView titleText = rowView.findViewById(R.id.username);
        TextView floatingActionButton = rowView.findViewById(R.id.num);
        //floatingActionButton.setText("1");
        titleText.setText(maintitle[position]);
        return rowView;
    }
}