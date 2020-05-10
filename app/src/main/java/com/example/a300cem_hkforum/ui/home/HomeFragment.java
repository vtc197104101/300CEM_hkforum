package com.example.a300cem_hkforum.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a300cem_hkforum.Contact;
import com.example.a300cem_hkforum.DatabaseHandler;
import com.example.a300cem_hkforum.MyAdapter;
import com.example.a300cem_hkforum.Post;
import com.example.a300cem_hkforum.R;
import com.example.a300cem_hkforum.RecyclerItemClickListener;
import com.example.a300cem_hkforum.addPost;
import com.example.a300cem_hkforum.post_detail;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {
    static String currentLocation;
     static String CLname;
    TextView CL;
    ImageView BG;
    Button newPost;
    RecyclerView rv;
    private List<Post> listData;
    private MyAdapter adapter;

    public static void putA(Bundle arg){
        String SCL = arg.getString("key");
        String name = arg.getString("name");
        currentLocation = SCL;
        CLname = name;
    }

    private HomeViewModel homeViewModel;

    @Override
    public void onResume() {
        super.onResume();

        CL.setText(CLname);
        if(currentLocation.equals("Hong Kong Island")){
            BG.setImageResource(R.drawable.hki);
        } else if (currentLocation.equals("Kowloon")){
            BG.setImageResource(R.drawable.kowloon);
        } else {
            BG.setImageResource(R.drawable.nt);
        }
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
                    adapter=new MyAdapter(listData, getActivity());
                    rv.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

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
                        Long tsLong = System.currentTimeMillis()/1000;
                        int ts = tsLong.intValue();
                        DatabaseHandler db = new DatabaseHandler(getActivity());
                        db.addContact(new Contact(ts, listData.get(position).title, listData.get(position).timestamp, listData.get(position).user));
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever

                        MaterialAlertDialogBuilder a = new MaterialAlertDialogBuilder(getActivity(),R.style.AlertDialogTheme);
                        a.setTitle(listData.get(position).title);
                        a.setMessage(listData.get(position).content);
                        a.setCancelable(true);
                        a.show();

                    }
                })
        );
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.new_post, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }



}