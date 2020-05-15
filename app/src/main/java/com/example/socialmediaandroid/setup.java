package com.example.socialmediaandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class setup extends AppCompatActivity {

    private EditText firstName, lastName, place;
    private Button  save;
    private CircleImageView profilePic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        firstName=(EditText) findViewById(R.id.firstName);
        lastName=(EditText) findViewById(R.id.lastName);
        place=(EditText) findViewById(R.id.place);
        save=(Button) findViewById(R.id.saveButton);
        profilePic=(CircleImageView) findViewById(R.id.profilePic);


    }
}
