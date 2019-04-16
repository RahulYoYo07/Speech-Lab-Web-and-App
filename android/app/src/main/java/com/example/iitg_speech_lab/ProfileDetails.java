package com.example.iitg_speech_lab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ProfileDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        String username = getIntent().getStringExtra("username");
        //Log.d("ji",username);
        //Toast.makeText(this,"Hi",Toast.LENGTH_LONG).show();
        //Toast.makeText(this,username.toString(),Toast.LENGTH_SHORT).show();
    }
}
