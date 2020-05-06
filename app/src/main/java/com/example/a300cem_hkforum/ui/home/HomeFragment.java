package com.example.a300cem_hkforum.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.a300cem_hkforum.CheckGPS;
import com.example.a300cem_hkforum.R;

public class HomeFragment extends Fragment {
    static String currentLocation;
    TextView CL;
    ImageView BG;
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

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.new_post, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }



}