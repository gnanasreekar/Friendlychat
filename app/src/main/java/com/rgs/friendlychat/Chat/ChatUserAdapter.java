package com.rgs.friendlychat.Chat;

/*
  Developed by : R.Gnana Sreekar
  Github : https://github.com/gnanasreekar
  Linkdin : https://www.linkedin.com/in/gnana-sreekar/
  Instagram : https://www.instagram.com/gnana_sreekar/
  Website : https://gnanasreekar.com
*/

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rgs.friendlychat.R;

import java.util.ArrayList;
import java.util.List;

public class ChatUserAdapter extends RecyclerView.Adapter<com.rgs.friendlychat.Chat.ChatUserAdapter.ViewHolder> {
    private List<ChatUserPOJO> listData;
    private List<String> users;
    private List<String> chatids;
    Context context;
    SharedPreferences sharedPreferences;

    public void setlist(List<ChatUserPOJO> listData) {
        this.listData = listData;

    }

    public void setUsers(ArrayList<String> users, ArrayList<String> chatids){
        this.users = users;
        this.chatids = chatids;
    }

    public ChatUserAdapter(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("sp", 0);
        Log.d("TAG", "MyAdapter: " + sharedPreferences.getBoolean("reduceanim" , false));
    }

    @NonNull
    @Override
    public com.rgs.friendlychat.Chat.ChatUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list, parent, false);
        return new com.rgs.friendlychat.Chat.ChatUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.rgs.friendlychat.Chat.ChatUserAdapter.ViewHolder holder, int position)
    {

        if (sharedPreferences.getBoolean("animate" , false)){
            holder.chatListLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_trans));
        }


        final ChatUserPOJO ld = listData.get(position);
        final String Name = ld.getName();

        holder.name.setText(Name);
      //  holder.description.setText(ld.getUID());
//        if (ld.getPic().equals("...")){
//            Glide.with(context).load(R.drawable.ic_male).into(holder.image);
//        } else {
//            Glide.with(context).load(ld.getPic()).into(holder.image);
//        }

        holder.lytParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("3114", "onClick: " + users.toString());

                if (users.contains(ld.getUID()))
                    {
                        Log.d("3114", "onClick: 2 ........." + chatids.toString());
                        Toast.makeText(context, chatids.get(users.indexOf(ld.getUID())), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, ChatScreen.class);
                        intent.putExtra("id" , chatids.get(users.indexOf(ld.getUID())));
                        intent.putExtra("uid" , sharedPreferences.getString("uid","..."));
                        intent.putExtra("myname" , sharedPreferences.getString("name","..."));
                        intent.putExtra("name2" , ld.getName());
                      //  intent.putExtra("pic" , ld.getPic());
                        context.startActivity(intent);
                    } else {
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

                    databaseReference.child(sharedPreferences.getString("uid", "...")).child("Chat").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                                if (dataSnapshot2.getKey().equals(ld.getUID())){
                                    Log.d("3114", "Got it.....................................................................: " + dataSnapshot2.getKey());

                                    Log.d("3114", "onClick: 2 ........." + chatids.toString());
//                                    Toast.makeText(context, chatids.get(users.indexOf(ld.getUID())), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, ChatScreen.class);
                                    intent.putExtra("id" , chatids.get(users.indexOf(ld.getUID())));
                                    intent.putExtra("uid" , sharedPreferences.getString("uid","..."));
                                    intent.putExtra("myname" , sharedPreferences.getString("name","..."));
                                    intent.putExtra("name2" , ld.getName());
                                    //  intent.putExtra("pic" , ld.getPic());
                                    context.startActivity(intent);

                                } else {
                                    databaseReference.child(sharedPreferences.getString("uid" , "...")).child("Chat").child(ld.getUID()).setValue(sharedPreferences.getString("uid" , "...")+"-" + ld.getUID());
                                    databaseReference.child(ld.getUID()).child("Chat").child(sharedPreferences.getString("uid" , "...")).setValue(sharedPreferences.getString("uid" , "...")+"-" + ld.getUID());
                                    Log.d("3114", "DBDBDBDBDBDBDBDBDBDB");

                                    Intent intent = new Intent(context, ChatScreen.class);
                                    intent.putExtra("id" , sharedPreferences.getString("uid" , "...")+"-" + ld.getUID());
                                    intent.putExtra("uid" , sharedPreferences.getString("uid","..."));
                                    intent.putExtra("myname" , sharedPreferences.getString("name","..."));
                                    context.startActivity(intent);
                                }
                                Log.d("3114", "onDataChange: " + dataSnapshot2.getKey());
                            }
                            Log.d("3114", "onDataChange: ............" + users.toString());

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });


                    }

            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         LinearLayout chatListLayout;
         LinearLayout lytParent;
         ImageView image;
         TextView name;
      //   TextView description;



        public ViewHolder(View itemView) {
            super(itemView);
            chatListLayout = itemView.findViewById(R.id.chat_list_layout);
            lytParent = itemView.findViewById(R.id.lyt_parent);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
      //      description = itemView.findViewById(R.id.description);


        }
    }

}