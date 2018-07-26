package com.ezzat.lawyer.Controller;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezzat.lawyer.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView nameTV;
    public TextView numTV;
    public TextView locTV;
    public TextView typeTV;
    public TextView dateTV;
    public ImageView coverImageView;
    public CardView cardView;

    public MyViewHolder(View v) {
        super(v);
        nameTV = v.findViewById(R.id.name);
        coverImageView = v.findViewById(R.id.coverImageView);
        numTV = v.findViewById(R.id.num);
        locTV = v.findViewById(R.id.loc);
        typeTV = v.findViewById(R.id.type);
        dateTV = v.findViewById(R.id.date);
        cardView = v.findViewById(R.id.card_view);
    }
}
