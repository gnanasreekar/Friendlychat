package com.rgs.friendlychat;

/*
  Developed by : R.Gnana Sreekar
  Github : https://github.com/gnanasreekar
  Linkdin : https://www.linkedin.com/in/gnana-sreekar/
  Instagram : https://www.instagram.com/gnana_sreekar/
  Website : https://gnanasreekar.com
*/


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rgs.friendlychat.Chat.ChatUserAdapter;
import com.rgs.friendlychat.Chat.ChatUserPOJO;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    ArrayList<String> users;
    ArrayList<String> chatids;
    String TAG = "3114";
    private List<ChatUserPOJO> listData;
    private RecyclerView rv;
    private ChatUserAdapter adapter;

    BottomAppBar bottomAppBar;
    BottomSheetBehavior<View> bottomSheetBehavior;
    CoordinatorLayout coordinatorLayout;
    FloatingActionButton fab;
    NavigationView navigationView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("FriendlyChat");
        }

        Toasty.success(this, "Please Report Bugs if any... \nHope it helps 😊", Toast.LENGTH_LONG,true).show();



        {
            context = MainActivity.this;
            navigationView = findViewById(R.id.navigation_view);
            coordinatorLayout = findViewById(R.id.coordinator_layout);
            View bottomDrawer = coordinatorLayout.findViewById(R.id.bottom_drawer);
            bottomAppBar = findViewById(R.id.bottom_app_bar);

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.hello)
                        startActivity(new Intent(MainActivity.this,Feedback.class));
                    else if (item.getItemId() == R.id.code)
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/gnanasreekar/Friendlychat")));
                    else if (item.getItemId() == R.id.help)
                        helpdialog();
                    return true;
                }
            });
            bottomSheetBehavior = BottomSheetBehavior.from(bottomDrawer);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
          //  bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
         //   bottomAppBar.replaceMenu(R.menu.menu_demo);
            bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                }
            });
            bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.hello:
                            startActivity(new Intent(MainActivity.this,Feedback.class));
                            break;
                    }
                    return false;
                }
            });
            fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,About.class));
                }
            });
        }


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


    public void helpdialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setTitle("Help");
        builder.setMessage("Every User yo see in the main screen is a account in firebase with a UID, Usinf this uid i was able to make a one to one chat room app \n*There is an Offencive word filter \n*an Admin mode \n " +
                "*Anti Flood Protection and even more features coming soon");
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", null);

        builder.show();
    }
}
