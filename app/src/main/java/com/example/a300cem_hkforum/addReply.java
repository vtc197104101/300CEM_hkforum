package com.example.a300cem_hkforum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class addReply extends AppCompatActivity {
    Long numOfReplys;
    String PostID;
    String username;
    String CL;
    String ts;
    Button back;
    Button pushR;
    EditText contentR;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reply);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        Intent intent=getIntent();
        PostID = intent.getStringExtra("PostID");
        CL = intent.getStringExtra("CL");


        back = (Button)findViewById(R.id.backk);
        pushR = (Button) findViewById(R.id.pushReply);
        contentR = (EditText) findViewById(R.id.reply_content);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference checkref = database.getReference("posts").child(CL).child(PostID).child("replys");
        checkref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                numOfReplys = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pushR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long tsLong = System.currentTimeMillis()/1000;
                ts = tsLong.toString();
                int intReplys = Math.toIntExact(numOfReplys);
                String strReplys = Integer.toString(intReplys+1);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("posts").child(CL).child(PostID).child("replys").child(strReplys);
                myRef.child("content").setValue(contentR.getText().toString());
                myRef.child("timestamp").setValue(ts);
                myRef.child("ID").setValue(strReplys);
                myRef.child("user").setValue(username);
                finish();
            }
        });
    }
}
