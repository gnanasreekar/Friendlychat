package com.rgs.friendlychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rgs.friendlychat.Chat.ChatUserAdapter;
import com.rgs.friendlychat.Chat.ChatUserPOJO;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    ArrayList<String> users;
    ArrayList<String> chatids;
    String TAG = "3114";
    private List<ChatUserPOJO> listData;
    private RecyclerView rv;
    private ChatUserAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        users = new ArrayList<>();
        chatids = new ArrayList<>();
        adapter = new ChatUserAdapter(MainActivity.this);
        rv = (RecyclerView) findViewById(R.id.chat_home_rec);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listData = new ArrayList<>();

        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String UID = "...",Name = "...",Email = "...";

                    UID = dataSnapshot1.getKey();
                    if (dataSnapshot1.getKey().equals(sharedPreferences.getString("uid", "..."))){

                    } else {


                            Name = dataSnapshot1.child("Name").getValue().toString();
                            Email = dataSnapshot1.child("Email").getValue().toString();
//                            if (dataSnapshot1.hasChild("PIC")){
//                                Pic = dataSnapshot1.child("PIC").getValue().toString();
//                            }

                            listData.add(new ChatUserPOJO(UID,Name,Email));

                    }

                }
                adapter.setlist(listData);
                rv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child(sharedPreferences.getString("uid", "...")).child("Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                users.clear();
                chatids.clear();
                for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                    users.add(dataSnapshot2.getKey());
                    chatids.add(dataSnapshot2.getValue().toString());
                    Log.d("3114", "onDataChange: " + dataSnapshot2.getKey());
                }
                Log.d("3114", "onDataChange: ............" + users.toString());

                adapter.setUsers(users,chatids);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}