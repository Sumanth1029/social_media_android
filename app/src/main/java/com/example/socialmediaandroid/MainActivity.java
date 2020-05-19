package com.example.socialmediaandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView postList;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FirebaseAuth mAuth;
    private DatabaseReference dbref;
    private FirebaseFirestore fstore;
    private TextView nav_name;
    private ImageButton add_post;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView=(NavigationView) findViewById(R.id.navigation_view);
        View navView=navigationView.inflateHeaderView(R.layout.navigation_header);
        nav_name=(TextView) navView.findViewById(R.id.nav_name);
        add_post=(ImageButton) findViewById(R.id.add_post);

        //add appbar with title
        mToolbar=(Toolbar) findViewById(R.id.main_page_toolbar) ;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        //for hamburger menu toggle
        actionBarDrawerToggle=new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //firebase auth
        mAuth=FirebaseAuth.getInstance();

        fstore=FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser()!=null){
            DocumentReference docRef=fstore.collection("users").document(mAuth.getCurrentUser().getUid());
            if(docRef!=null){
                docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    String navName=documentSnapshot.getString("initials");
                    nav_name.setText(navName);

                }
            });}

        }


        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToPost();
            }
        });


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        UserMenuSelector(item);
                        return false;
                    }
                });
    }

    private void SendUserToPost() {
        Intent postIntent=new Intent(MainActivity.this, new_postActivity.class);
        startActivity(postIntent);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {  // function to sync hamburger with sidebar

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }





    @Override                           //function that runs on app start
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser==null){
            SendUserToLogin();
        }
    }


    private void SendUserToLogin() {
        Intent loginIntent=new Intent(MainActivity.this,loginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }


    private  void UserMenuSelector(MenuItem item){
        switch ( item.getItemId()){
            case R.id.nav_home:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
                
            case R.id.nav_logout:
                mAuth.signOut();
                SendUserToLogin();
                break;



        }
    }
}
