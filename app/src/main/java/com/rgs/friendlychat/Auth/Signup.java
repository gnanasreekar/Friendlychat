package com.rgs.friendlychat.Auth;

/*
  Developed by : R.Gnana Sreekar
  Github : https://github.com/gnanasreekar
  Linkdin : https://www.linkedin.com/in/gnana-sreekar/
  Instagram : https://www.instagram.com/gnana_sreekar/
  Website : https://gnanasreekar.com
*/


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.UserProfileChangeRequest;
import com.rgs.friendlychat.MainActivity;
import com.rgs.friendlychat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;


public class Signup extends AppCompatActivity {
    public EditText emailId;
    public EditText password;
    public EditText username;
    Button buttom_signup;
    TextView signIn;
    FirebaseAuth firebaseAuth;
    String name;
    CharSequence s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        setTitle("SignUp");

        firebaseAuth = FirebaseAuth.getInstance();

        {
        emailId = findViewById(R.id.email_signup);
        username = findViewById(R.id.username_signup);
        password = findViewById(R.id.password_signup);
        buttom_signup = findViewById(R.id.button_signup);
        signIn = findViewById(R.id.signin_signup);
        
    }

        buttom_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String emailID = emailId.getText().toString();
                String paswd = password.getText().toString();
                name = username.getText().toString();

                if (emailID.isEmpty()) {
                    username.setError("Set your Username");
                    username.requestFocus();
                } else if (name.isEmpty()) {
                    emailId.setError("Provide your Email first!");
                    emailId.requestFocus();
                } else if (paswd.isEmpty()) {
                    password.setError("Set your password");
                    password.requestFocus();
                } else if (!(emailID.isEmpty() && paswd.isEmpty() && name.isEmpty())) {
                    firebaseAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Signup.this.getApplicationContext(),
                                        "SignUp unsuccessful: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();

                                task.getResult().getUser().updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("3114", "User profile updated.");
                                                }
                                            }
                                        });


                                //Date
                                Date d = new Date();
                                s  = DateFormat.format("MMMM d, yyyy HH:mm:ss", d.getTime());

                                //Storing data to display in the Nav bar and in the app
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sp",0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("uid" , firebaseAuth.getUid());
                                editor.putString("name" , name);
                                editor.putString("email" , emailID);
                                editor.apply();

                                //To save data in Firebase Database
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + firebaseAuth.getUid());
                                databaseReference.child("Name").setValue(name);
                                databaseReference.child("Email").setValue(emailID);
                                databaseReference.child("UID").setValue(firebaseAuth.getUid());
                                databaseReference.child("Date").setValue(s);
                                databaseReference.child("V1").setValue(0);
                                databaseReference.child("V2").setValue(0);

                                startActivity(new Intent(Signup.this, MainActivity.class));
                                finish();

                            }
                        }
                    });
                } else {
                    Toast.makeText(Signup.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup.this, Login.class));
            }
        });

    }


}

