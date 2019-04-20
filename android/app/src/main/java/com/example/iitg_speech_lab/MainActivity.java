package com.example.iitg_speech_lab;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {



    public static final String EXTRA_MESSAGE = "com.example.iitg_speech_lab.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, Discussion_Room.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString().trim();
        intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
    }

    public void viewNotice(View view) {
        Intent intent = new Intent(this, Discussion_Notice_Board.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString().trim();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void viewCalendar(View view) {
        Intent intent = new Intent(this, Calendar.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString().trim();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void openCourses(View view){
        Intent intent = new Intent(this, CoursesActivity.class);
        startActivity(intent);
    }

    public void studentOpenCourses(View view){
        Intent intent = new Intent(this, StudentCoursesActivity.class);
        startActivity(intent);
    }
}
