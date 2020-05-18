package com.example.socialmediaandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class setup extends AppCompatActivity {

    private EditText firstName, lastName, pplace;
    private Button  save;
    private CircleImageView profilePic;
    private ProgressDialog loadingBar,loadingBar2;
    String email,password;
    private FirebaseAuth mAuth;




    //Firestore init
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    DocumentReference docRef=db.collection("users").document();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        Intent intent=getIntent();
         email=intent.getStringExtra("email");
         password=intent.getStringExtra("password");


        firstName=(EditText) findViewById(R.id.firstName);
        lastName=(EditText) findViewById(R.id.lastName);
        pplace=(EditText) findViewById(R.id.place);
        save=(Button) findViewById(R.id.saveButton);
//        profilePic=(CircleImageView) findViewById(R.id.profilePic);
        loadingBar=new ProgressDialog(this);
        loadingBar2=new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });


    }

    private void saveProfile() {
        final String fname=firstName.getText().toString();
        final String lname=lastName.getText().toString();
        String place=pplace.getText().toString();
        String initials=String.valueOf(fname.charAt(0))+String.valueOf(lname.charAt(0));
        initials=initials.toUpperCase();



        if(TextUtils.isEmpty(fname)){
            Toast.makeText(this, "Please enter first name", Toast.LENGTH_SHORT).show();
        }else
        if(TextUtils.isEmpty(lname)){
            Toast.makeText(this, "Please enter last name", Toast.LENGTH_SHORT).show();
        }else
            if(TextUtils.isEmpty(place)){
            Toast.makeText(this, "Please enter place", Toast.LENGTH_SHORT).show();
        }else{



            loadingBar.setTitle("Registering");
            loadingBar.setMessage("Please wait until we register you");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

                final String finalInitials = initials;
                mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();

                                loadingBar2.setTitle("Creating user profile");
                                loadingBar2.setMessage("Please wait.");
                                loadingBar2.show();
                                loadingBar2.setCanceledOnTouchOutside(true);

//

                                Map<String, String> usersMap = new HashMap<>();
                                usersMap.put("firstName", fname.toUpperCase());
                                usersMap.put("lastName", lname.toUpperCase());
                                usersMap.put("initials", finalInitials.toUpperCase());

                                db.collection("users").document(uid).set(usersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            SendUserToMain();
                                            Toast.makeText(setup.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                                            loadingBar2.dismiss();
                                        }
                                        else{
                                            String message=task.getException().getMessage();
                                            Toast.makeText(setup.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                            loadingBar2.dismiss();
                                        }

                                    }
                                });







                                loadingBar.dismiss();
                            }else{
                                String message=task.getException().getMessage();
                                Toast.makeText(setup.this, "Error: "+message, Toast.LENGTH_LONG).show();
//                                AlertDialogLayout
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
