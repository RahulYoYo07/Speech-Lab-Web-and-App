package com.example.iitg_speech_lab;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    public static final String EXTRA_MESSAGE = "com.example.iitg_speech_lab.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean bo=isInternetAvailable();
//        if(bo==false)
//        {
//            AlertDialog.Builder dialog=new AlertDialog.Builder(this);
//            dialog.setMessage("No Internet Connection");
//            dialog.setTitle("Error Message");
//            dialog.setPositiveButton("Close",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog,
//                                            int which) {
////                        Toast.makeText(getApplicationContext(),"Yes is clicked",Toast.LENGTH_LONG).show();
//                        }
//                    });
////        dialog.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////                Toast.makeText(getApplicationContext(),"cancel is clicked",Toast.LENGTH_LONG).show();
////            }
////        });
//            AlertDialog alertDialog=dialog.create();
//            alertDialog.show();
//        }
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
