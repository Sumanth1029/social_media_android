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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText userEmail,userPassword;
    private TextView link;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        link=(TextView) findViewById(R.id.linkText);
        userEmail=(EditText) findViewById(R.id.login_email);
        userPassword=(EditText) findViewById(R.id.login_password);
        loginButton=(Button) findViewById(R.id.login_button);
        mAuth=FirebaseAuth.getInstance();
        loadingBar=new ProgressDialog(this);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLogin();
            }
        });


        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToRegister();
            }
        });

    }

    private void UserLogin() {
        String email=userEmail.getText().toString();
        String password=userPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
        }else
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        }else{

            loadingBar.setTitle("Logging in");
            loadingBar.setMessage("Please wait until you are logged in");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendUserToMain();
                                Toast.makeText(loginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }else{
                                String mes=task.getException().getMessage();
                                Toast.makeText(loginActivity.this, "Error: "+mes, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });

        }
    }

    private void sendUserToMain() {
        Intent main=new Intent(loginActivity.this,MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);   // to prevent going back when back btn is pressed
        startActivity(main);
        finish();
    }

    private void sendUserToRegister() {
        Intent registerIntent=new Intent(loginActivity.this,RegisterActivity.class);
        startActivity(registerIntent);
  }
}
