package com.example.iitg_speech_lab;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Discussion_Notice_Board extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion__notice__board);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        TextView NoticeHeading = findViewById(R.id.NoticeHeading);
        String Heading = "<h1>" + message +" Notice Board"+"</h1> ";
        NoticeHeading.setText(Html.fromHtml(Heading));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference docRef = db.collection("Courses").document(message).collection("Notices");
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData()
                                TextView textView2 = findViewById(R.id.textView2);
                                String sourceString = "<h3>" + document.getString("NoticeHead")+"</h3> " + "<p>"+document.getString("NoticeBody")+"</p>";
                                textView2.append(Html.fromHtml(sourceString));
//                                textView2.append(document.getString("NoticeHead")+"\n");
//                                textView2.append(document.getString("NoticeBody")+"\n\n");
                            }
                        }
//                        else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
                    }
                });


    }
    public void addNotice(View view){

    }
}
