package com.example.iitg_speech_lab;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class ViewPeople extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_people);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("People");

    }

    public void StudentClick(View view){
        final ConstraintLayout lm = (ConstraintLayout) findViewById(R.id.main_layout);

        // create the layout params that will be used to define how your
        // button will be displayed
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        final LinearLayout ll = (LinearLayout) findViewById(R.id.linear);
        ll.removeAllViews();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userRef = db.collection("Users");
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                ll.removeAllViews();
                                if (document.getString("Designation").equals("Student"))
                                {
                                    final Button btn = new Button(ViewPeople.this);
                                    btn.setTag(document.getId());
                                    btn.setText(document.getString("FullName"));

                                    btn.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(ViewPeople.this, PrivateProfileDetails.class);
                                            intent.putExtra("username",document.getId());
                                            startActivity(intent);
                                        }
                                    });
                                    ll.addView(btn);
                                }
                            }
                        }
                    }
                });
    }

    public void FacultyClick(View view){
        final ConstraintLayout lm = (ConstraintLayout) findViewById(R.id.main_layout);

        // create the layout params that will be used to define how your
        // button will be displayed
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        final LinearLayout ll = (LinearLayout) findViewById(R.id.linear);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userRef = db.collection("Users");
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ll.removeAllViews();
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("Designation").equals("Faculty"))
                                {
                                    final Button btn = new Button(ViewPeople.this);
                                    btn.setTag(document.getId());
                                    btn.setText(document.getString("FullName"));

                                    btn.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v) {
                                            Log.d("tushar",document.getString("Username"));
                                            Intent intent = new Intent(ViewPeople.this, PrivateProfileDetails.class);
                                            intent.putExtra("username",document.getString("Username"));
                                            Log.d("tushar",document.getString("Username"));
                                            startActivity(intent);
                                        }
                                    });
                                    ll.addView(btn);
                                }
                            }
                        }
                    }
                });
    }



}
