package com.example.a300cem_hkforum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Record extends AppCompatActivity {

    Button back;
    RecyclerView rv;
    private List<Contact> listData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        back= (Button) findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rv = (RecyclerView)findViewById(R.id.recordRV);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        DatabaseHandler db = new DatabaseHandler(Record.this);
        listData=new ArrayList<>();
        listData = db.getAllPosts();
        Collections.reverse(listData);
        MyRecordAdapter adapter = new MyRecordAdapter(listData, this);
        rv.setAdapter(adapter);
    }
}
