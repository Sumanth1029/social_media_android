package com.example.socialmediaandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {


    private EditText remail,rpassword,repassword;
    private Button registerbtn;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth=FirebaseAuth.getInstance();


        remail=(EditText) findViewById(R.id.register_email);
        rpassword=(EditText) findViewById(R.id.register_password);
        repassword=(EditText) findViewById(R.id.register_confirm_password);
        registerbtn=(Button) findViewById(R.id.register_btn);
        loadingBar=new ProgressDialog(this);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUser();
            }
        });

    }

    private void CreateUser() {
        String email=remail.getText().toString();
        String password=rpassword.getText().toString();
        String re_password=repassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(re_password)){
            Toast.makeText(this, "Please re-enter password", Toast.LENGTH_SHORT).show();
        }else if(!password.equals(re_password)){
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }else{

            loadingBar.setTitle("Registering");
            loadingBar.setMessage("Please wait until we register you");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                SendUserToProfile();

//                                Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }else{
                                String message=task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }
                        }
                    });
        }


    }

    private void SendUserToProfile() {
        Intent profileIntent=new Intent(RegisterActivity.this,setup.class);
        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(profileIntent);
        finish();
    }
}
