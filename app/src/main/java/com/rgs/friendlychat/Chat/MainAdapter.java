package com.rgs.friendlychat.Chat;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.rgs.friendlychat.R;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Message> data;
    private Context mContext;
    SharedPreferences sharedPreferences;

    public MainAdapter(Context context, ArrayList<Message> data) {
        this.data = data;
        this.mContext = context;
        sharedPreferences = context.getSharedPreferences("sp", 0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if (i == 1) { // for call layout
            view = LayoutInflater.from(mContext).inflate(R.layout.mymessage, viewGroup, false);
            return new MYmessage(view);

        } else { // for email layout
            view = LayoutInflater.from(mContext).inflate(R.layout.theirmessage, viewGroup, false);
            return new Theirmessage(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).getUsername().equals(sharedPreferences.getString("name","..."))) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder myViewHolder, int i) {



        Message p = data.get(i);
        if (myViewHolder instanceof MYmessage) {
            MYmessage view = (MYmessage) myViewHolder;

            view.my_mess_tv.setText(p.getMessage());
           // view.my_mess_tv.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_trans));

        } else {
            Theirmessage view = (Theirmessage) myViewHolder;
            view.their_mess.setText(p.getMessage());
//            if (p.getPic().equals("...")){
                Glide.with(mContext).load(R.drawable.ic_male).into(view.pic);
//            } else {
//                Glide.with(mContext).load(p.getPic()).into(view.pic);
//            }
        }
        Log.d("3114", "onBindViewHolder:2 " + sharedPreferences.getString("name","..."));
//        Message message = data.get(i);
//        String formatted_date = SCUtils.formatted_date(message.getTimestamp());

//        if (message.isNotification()) {
//            myViewHolder.textView_message.setText(Html.fromHtml("<small><i><font color=\"#FFBB33\">" + " " + message.getMessage() + "</font></i></small>"));
//        } else {
//            myViewHolder.textView_message.setText(
//                    Html.fromHtml("<font color=\"#403835\">&#x3C;" + message.getUsername() + "&#x3E;</font>" + " " + message.getMessage() + " <font color=\"#999999\">" + formatted_date + "</font>"));
//        }

    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }

    class MYmessage extends RecyclerView.ViewHolder {

        private TextView my_mess_tv;
        private LinearLayout my_lyt;

        MYmessage(@NonNull View itemView) {
            super(itemView);
            my_mess_tv = itemView.findViewById(R.id.my_message);
            my_lyt = itemView.findViewById(R.id.my_messagelayout);
        }

    }

    class Theirmessage extends RecyclerView.ViewHolder {

        private TextView their_mess;
        ImageView pic;
        LinearLayout lyt_their;

        Theirmessage(@NonNull View itemView) {
            super(itemView);
            their_mess = itemView.findViewById(R.id.their_message);
            lyt_their = itemView.findViewById(R.id.theirmessage_layout);
            pic = itemView.findViewById(R.id.their_image);
        }

    }

//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        protected TextView textView_message;
//
//        public MyViewHolder(View view) {
//            super(view);
//            this.textView_message = (TextView) view.findViewById(R.id.textView_message);
//        }
//    }


}