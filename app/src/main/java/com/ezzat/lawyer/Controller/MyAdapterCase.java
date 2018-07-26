package com.ezzat.lawyer.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ezzat.lawyer.Model.Case;
import com.ezzat.lawyer.Model.User;
import com.ezzat.lawyer.R;
import com.ezzat.lawyer.View.ApointmentPageActivity;
import com.ezzat.lawyer.View.CasePageActivity;

import java.util.ArrayList;

public class MyAdapterCase extends RecyclerView.Adapter<MyViewHolder> {

    private ArrayList<Case> list;
    private Context con;
    private User user;

    public MyAdapterCase(ArrayList<Case> Data, Context context, User user) {
        list = Data;
        con = context;
        this.user = user;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycle, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.nameTV.setText(list.get(position).getName());
        holder.coverImageView.setImageResource(R.drawable.ic_law);
        holder.numTV.setText(list.get(position).getNum());
        holder.locTV.setText(list.get(position).getLocation());
        holder.typeTV.setText(list.get(position).getType());
        holder.dateTV.setText(list.get(position).getDate());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(con, CasePageActivity.class);
                intent.putExtra("user",user);
                intent.putExtra("case", list.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                con.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}