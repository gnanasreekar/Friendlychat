package com.rgs.friendlychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
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
                        Toast.makeText(context, "Search Clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }

            });
            bottomSheetBehavior = BottomSheetBehavior.from(bottomDrawer);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
            bottomAppBar.replaceMenu(R.menu.menu_demo);
            bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
            bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                }
            });
//            bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    switch (item.getItemId()) {
//                        case R.id.hello:
//                            Toast.makeText(context, "Search Clicked2", Toast.LENGTH_SHORT).show();
//                            break;
//
//                    }
//                    return false;
//                }
//            });
            fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Fab Clicked!", Toast.LENGTH_SHORT).show();
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
}
