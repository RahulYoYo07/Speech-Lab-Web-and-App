package com.example.iitg_speech_lab;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddFaq extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faq);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add FAQ");
    }

    public void addFaq(View view){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Homepage").document("faq");
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot user = task.getResult();
                            if(user.exists()){
                                EditText question = findViewById(R.id.question);
                                EditText answer = findViewById(R.id.answer);
                                final ArrayList<Map<String,String>> arr = (ArrayList<Map<String,String>>) user.get("qa");
                                String ques = question.getText().toString().trim();
                                String ans = answer.getText().toString().trim();
                                if(ques.length()==0||ans.length()==0){
                                    Toast.makeText(getApplicationContext(),"Please do not leave fields empty",Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Map<String,String> map = new HashMap<>();
                                    map.put("q",ques);
                                    map.put("a",ans);
                                    arr.add(map);
                                    Map <String,Object> temp = new HashMap<String, Object>();
                                    temp.put("qa",arr);
                                    DocumentReference ins = db.collection("Homepage").document("faq");
                                    ins.set(temp);
                                    Toast.makeText(AddFaq.this,"FAQ added Successfully",Toast.LENGTH_SHORT).show();
                                    question.setText("");
                                    answer.setText("");
                                }
                            }
                        }
                    }
                });
    }
}
