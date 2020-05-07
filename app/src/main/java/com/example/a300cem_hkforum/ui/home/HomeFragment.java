package com.example.a300cem_hkforum.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a300cem_hkforum.CheckGPS;
import com.example.a300cem_hkforum.MyAdapter;
import com.example.a300cem_hkforum.Post;
import com.example.a300cem_hkforum.R;
import com.example.a300cem_hkforum.RecyclerItemClickListener;
import com.example.a300cem_hkforum.addPost;
import com.example.a300cem_hkforum.post_detail;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {
    static String currentLocation;

    TextView CL;
    ImageView BG;
    Button newPost;
    RecyclerView rv;
    private List<Post> listData;
    private MyAdapter adapter;


    public static void putA(Bundle arg){
        String SCL = arg.getString("key");
        currentLocation = SCL;
    }

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        CL = (TextView)root.findViewById(R.id.CL);
        BG = (ImageView)root.findViewById(R.id.forum_bg);
        rv = (RecyclerView)root.findViewById(R.id.rv);
        newPost = (Button) root.findViewById(R.id.newPost);
        newPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), addPost.class);
                intent.putExtra("CL",currentLocation);
                startActivity(intent);
            }
        });
        CL.setText(currentLocation);
        if(currentLocation.equals("Hong Kong Island")){
            BG.setImageResource(R.drawable.hki);
        } else if (currentLocation.equals("Kowloon")){
            BG.setImageResource(R.drawable.kowloon);
        } else {
            BG.setImageResource(R.drawable.nt);
        }
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("posts").child(currentLocation);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        listData=new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                        Post l = npsnapshot.getValue(Post.class);
                        listData.add(l);
                    }
                    Collections.reverse(listData);
                    adapter=new MyAdapter(listData);
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), rv,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        Intent intent = new Intent(getActivity(), post_detail.class);
                        intent.putExtra("title",listData.get(position).title);
                        intent.putExtra("content",listData.get(position).content);
                        intent.putExtra("time",listData.get(position).timestamp);
                        intent.putExtra("user",listData.get(position).user);
                        intent.putExtra("CL",currentLocation);
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                        System.out.println("fuck android232");

                    }
                })
        );
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.new_post, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }



}