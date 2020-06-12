package com.rgs.friendlychat;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Feedback extends AppCompatActivity {

    private AppCompatEditText namea;
    private AppCompatEditText phonenumner;
    private AppCompatEditText message;
    private AppCompatButton btSubmit;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

                sharedPreferences = Feedback.this.getSharedPreferences("sp", 0);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle( "Feedback");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        namea = findViewById(R.id.name);
        phonenumner = findViewById(R.id.phonenumner);
        message = findViewById(R.id.message);
        btSubmit = findViewById(R.id.bt_submit);


        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = namea.getText().toString();
                String phone = phonenumner.getText().toString();
                String feedback = message.getText().toString();
                //Checking edit text if its empty
                if (name.isEmpty()) {
                    namea.setError("Provide your Name");
                    namea.requestFocus();
                } else if (phone.isEmpty()) {
                    phonenumner.setError("Provide your Phone!");
                    phonenumner.requestFocus();
                } else if (feedback.isEmpty()) {
                    message.setError("Fill the Field");
                    message.requestFocus();
                } else {
                    //Uploading Data to FBDB
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Feedback/" + phone);
                    databaseReference.child("Name").setValue(name);
                    databaseReference.child("Email").setValue(phone);
                    databaseReference.child("Feedback").setValue(feedback);
                    databaseReference.child("uid").setValue(sharedPreferences.getString("uid", "..."));
                    Toast.makeText(Feedback.this, "Thanks for your feedback! ;-)", Toast.LENGTH_SHORT).show();
                    finish();

                }
            }
        });


    }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
