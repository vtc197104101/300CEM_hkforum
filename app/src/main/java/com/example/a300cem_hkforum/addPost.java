package com.example.a300cem_hkforum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

public class addPost extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button back;
    Button push;
    EditText title;
    EditText content;
    String ts;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        Intent intent=getIntent();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(user.getUid()).child("username");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final String CL = intent.getStringExtra("CL");
        title= (EditText)findViewById(R.id.title);
        content = (EditText)findViewById(R.id.content);
        back = (Button)findViewById(R.id.back);
        push = (Button)findViewById(R.id.push);
        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().length() == 0 || content.getText().length() == 0){
                    Toast.makeText(addPost.this, getString(R.string.fail), Toast.LENGTH_SHORT).show();
                } else {
                    MaterialAlertDialogBuilder a = new MaterialAlertDialogBuilder(addPost.this, R.style.AlertDialogTheme);
                    a.setTitle(getString(R.string.sure));
                    a.setMessage(getString(R.string.alertmessage));
                    a.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Long tsLong = System.currentTimeMillis()/1000;
                            ts = tsLong.toString();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("posts").child(CL).child(ts);
                            myRef.child("title").setValue(title.getText().toString());
                            myRef.child("content").setValue(content.getText().toString());
                            myRef.child("timestamp").setValue(ts);
                            myRef.child("user").setValue(username);
                            Toast.makeText(addPost.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                            a.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    a.show();

                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
