package com.example.socialmediaandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class setup extends AppCompatActivity {

    private EditText firstName, lastName, pplace;
    private Button  save;
    private CircleImageView profilePic;
    private ProgressDialog loadingBar;


    //Firestore init
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    DocumentReference docRef=db.collection("users").document();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        firstName=(EditText) findViewById(R.id.firstName);
        lastName=(EditText) findViewById(R.id.lastName);
        pplace=(EditText) findViewById(R.id.place);
        save=(Button) findViewById(R.id.saveButton);
        profilePic=(CircleImageView) findViewById(R.id.profilePic);
        loadingBar=new ProgressDialog(this);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });


    }

    private void saveProfile() {
        String fname=firstName.getText().toString();
        String lname=lastName.getText().toString();
        String place=pplace.getText().toString();
        String initials=String.valueOf(fname.charAt(0))+String.valueOf(lname.charAt(0));
        initials=initials.toUpperCase();

        Users u1=new Users();
        u1.setFirstName(fname);
        u1.setLastNname(lname);
        u1.setInitials(initials);
        u1.setPlace(place);

        if(TextUtils.isEmpty(fname)){
            Toast.makeText(this, "Please enter first name", Toast.LENGTH_SHORT).show();
        }else
        if(TextUtils.isEmpty(lname)){
            Toast.makeText(this, "Please enter last name", Toast.LENGTH_SHORT).show();
        }else
            if(TextUtils.isEmpty(place)){
            Toast.makeText(this, "Please enter place", Toast.LENGTH_SHORT).show();
        }else{
                String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                u1.setUid(uid);

                loadingBar.setTitle("Creating user profile");
                loadingBar.setMessage("Please wait.");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);


                docRef.set(u1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){

                           SendUserToMain();
                           Toast.makeText(setup.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                           loadingBar.dismiss();

                       }
                       else{
                           String message=task.getException().getMessage();
                           Toast.makeText(setup.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                           loadingBar.dismiss();
                       }
                    }
                });
            }
    }

    private void SendUserToMain() {
        Intent main=new Intent(setup.this,MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(main);
        finish();
    }
}
