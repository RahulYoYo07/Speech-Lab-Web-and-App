package com.example.iitg_speech_lab;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class EditFaq extends AppCompatActivity {
    static String position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("tushar","andar");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_faq);
        position = getIntent().getStringExtra("position");
        Log.d("tushar",position);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Homepage").document("faq");
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot user = task.getResult();
                            if(user.exists()){
                                EditText question = (EditText) findViewById(R.id.editquestion);
                                EditText answer = (EditText) findViewById(R.id.editanswer);
                                final ArrayList<Map<String,String>> arr = (ArrayList<Map<String,String>>) user.get("qa");
                                Integer z= Integer.parseInt(position);
                                String ques = arr.get(z).get("q");
                                String ans = arr.get(z).get("a");
                                question.setText(ques);
                                answer.setText(ans);
                            }
                        }
                    }
                });
    }

    public void editFaq(View view){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Homepage").document("faq");
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot user = task.getResult();
                            if(user.exists()){
                                EditText question = (EditText) findViewById(R.id.editquestion);
                                EditText answer = (EditText) findViewById(R.id.editanswer);
                                final ArrayList<Map<String,String>> arr = (ArrayList<Map<String,String>>) user.get("qa");
                                String ques = question.getText().toString().trim();
                                String ans = answer.getText().toString().trim();
                                //Toast.makeText(getApplicationContext(),ques,Toast.LENGTH_SHORT).show();
                                if(ques.length()>0 && ans.length()>0){
                                    ArrayList<Map<String,String>> arr1 = new ArrayList<Map<String,String>>();
                                    int i=0;
                                    Integer z= Integer.parseInt(position);
                                    for(i=0;i<arr.size();i++){
                                        if(i!=z) arr1.add(arr.get(i));
                                        else {
                                            Map<String,String> map = new HashMap<>();
                                            map.put("q",ques);
                                            map.put("a",ans);
                                            arr1.add(map);
                                        }

                                        if(i == arr.size()-1){
                                            Map <String,Object> temp = new HashMap<String, Object>();
                                            temp.put("qa",arr1);
                                            DocumentReference ins = db.collection("Homepage").document("faq");
                                            ins.set(temp);
                                            Toast.makeText(EditFaq.this,"FAQ edited Successfully",Toast.LENGTH_SHORT).show();
                                            question.setText("");
                                            answer.setText("");
                                            finish();
                                            Intent intent = new Intent(EditFaq.this, EditDeleteFaq.class);
                                            startActivity(intent);
                                        }
                                    }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Please do not leave fields empty",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                });
    }
}
