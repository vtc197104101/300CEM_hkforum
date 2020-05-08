package com.example.a300cem_hkforum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class post_detail extends AppCompatActivity {
    String CL;
    String date;
    String content;
    String title;
    String user;
    TextView Posttitle;
    RecyclerView rv;
    Button Back;
    Button addreply;

    private List<Reply> listData;
    private MyReplyAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter != null){
            listData = new ArrayList<>();
            final DatabaseReference nm = FirebaseDatabase.getInstance().getReference("posts").child(CL).child(date).child("replys");
            nm.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                            Reply l = npsnapshot.getValue(Reply.class);
                            listData.add(l);
                        }
                        Reply f = new Reply(content, date, user, "0");
                        listData.add(0, f);
                        adapter = new MyReplyAdapter(listData);
                        rv.setAdapter(adapter);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        FirebaseDatabase database = FirebaseDatabase.getInstance();


        addreply = (Button) findViewById(R.id.replyButton);
        addreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(post_detail.this, addReply.class);
                intent.putExtra("CL", CL);
                intent.putExtra("PostID", date);
                startActivity(intent);
            }
        });
        Back = (Button) findViewById(R.id.backButton);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        date = intent.getStringExtra("time");
        user = intent.getStringExtra("user");
        CL = intent.getStringExtra("CL");
        Posttitle = (TextView)findViewById(R.id.postTitle);
        Posttitle.setText(title);
        rv = (RecyclerView) findViewById(R.id.reply_rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listData = new ArrayList<>();
        final DatabaseReference nm = FirebaseDatabase.getInstance().getReference("posts").child(CL).child(date).child("replys");
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                        Reply l = npsnapshot.getValue(Reply.class);
                        listData.add(l);
                    }
                    Reply f = new Reply(content, date, user, "0");
                    listData.add(0, f);
                    adapter = new MyReplyAdapter(listData);
                    rv.setAdapter(adapter);

                } else {
                    Reply f = new Reply(content, date, user, "0");
                    listData.add(0, f);
                    adapter = new MyReplyAdapter(listData);
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
