package com.ezzat.lawyer.Controller;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezzat.lawyer.R;
import com.github.library.bubbleview.BubbleTextView;

public class ChatHolder extends RecyclerView.ViewHolder {

    public TextView user;
    public TextView time;
    public TextView messageText_me;
    public TextView messageText_rep;

    public ChatHolder(View v) {
        super(v);
        user = v.findViewById(R.id.message_user);
        time = v.findViewById(R.id.message_time);
        messageText_me = /*(BubbleTextView)*/ v.findViewById(R.id.message_text_me);
        messageText_rep = /*(BubbleTextView)*/ v.findViewById(R.id.message_text_rep);
    }
}
