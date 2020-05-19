package com.example.socialmediaandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

public class new_postActivity extends AppCompatActivity {

    private Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        mtoolbar=(Toolbar) findViewById(R.id.update_post_page);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Update Post");


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         int id=item.getItemId();
         if(id==android.R.id.home){
             sendUserToMain();
         }

        return super.onOptionsItemSelected(item);
    }

    private void sendUserToMain() {
        Intent main=new Intent(new_postActivity.this,MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);   // to prevent going back when back btn is pressed
        startActivity(main);
//        finish();
    }
}
