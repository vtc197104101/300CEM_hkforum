package com.example.a300cem_hkforum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class edit_username extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button back;
    Button add;
    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_username);




        username = (EditText)findViewById(R.id.newusername);
        back = (Button)findViewById(R.id.backToF);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add = (Button)findViewById(R.id.pushusername);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().length() == 0){
                    Toast.makeText(edit_username.this, getString(R.string.fail),
                            Toast.LENGTH_SHORT).show();
                } else {
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = mAuth.getCurrentUser();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("users").child(user.getUid());
                    myRef.child("username").setValue(username.getText().toString());
                    Toast.makeText(edit_username.this, getString(R.string.success),
                            Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }
}
