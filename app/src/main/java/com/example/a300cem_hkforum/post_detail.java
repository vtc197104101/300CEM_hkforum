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
    RecyclerView rv;
    Button Back;
    Button addreply;
    TextView Title;
    TextView Content;
    TextView Time;
    TextView User;
    private List<Reply> listData;
    private MyReplyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Title = (TextView)findViewById(R.id.postTitle);
        Content = (TextView)findViewById(R.id.strContent);
        Time = (TextView)findViewById(R.id.strTime);
        User = (TextView)findViewById(R.id.strUsername);
        addreply = (Button)findViewById(R.id.replyButton);
        addreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(post_detail.this, addReply.class);
                intent.putExtra("CL",CL);
                intent.putExtra("PostID",date);
                startActivity(intent);
            }
        });
        Back = (Button)findViewById(R.id.backButton);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent=getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        date = intent.getStringExtra("time");
        String user = intent.getStringExtra("user");
        CL = intent.getStringExtra("CL");
        long Ldate = Long.parseLong(date);
        Title.setText(title);
        User.setText(user);
        Content.setText(content);
        Time.setText(getDate(Ldate));
        updateReplys();
    }
    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }
    private void updateReplys(){
        rv=(RecyclerView)findViewById(R.id.reply_rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listData =new ArrayList<>();
        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("posts").child(CL).child(date).child("replys");
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                        Reply l=npsnapshot.getValue(Reply.class);
                        listData.add(l);
                    }
                    adapter=new MyReplyAdapter(listData);
                    rv.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
