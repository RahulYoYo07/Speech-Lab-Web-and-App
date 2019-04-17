package com.example.iitg_speech_lab;

import android.content.Intent;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FAQ extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("FAQ's");
        final LinearLayout ll = (LinearLayout) findViewById(R.id.linear);
        ll.removeAllViews();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Homepage").document("faq");
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot user = task.getResult();
                            if(user.exists()){
                                final ArrayList<Map<String,String>> ans = (ArrayList<Map<String,String>>) user.get("qa");
                                int i=0;
                                for (Map<String,String> val : ans){
                                    i++;
                                    final TextView question = new TextView(FAQ.this);
                                    final TextView answer = new TextView(FAQ.this);
                                    question.setTextAppearance(FAQ.this,R.style.QuestionFAQ);
                                    answer.setTextAppearance(FAQ.this,R.style.AnswerFAQ);
                                    question.setText("Question "+Integer.toString(i)+". " +val.get("q").toString());
                                    answer.setText("Answer: " +val.get("a").toString());
                                    ll.addView(question);
                                    ll.addView(answer);
                                }
                            }
                        }
                    }
                });
    }
}

