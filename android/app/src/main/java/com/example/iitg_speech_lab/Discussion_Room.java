package com.example.iitg_speech_lab;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Discussion_Room extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion__room);
        final Discussion_Room help=this;
        Intent intent = getIntent();
        final String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        TextView DiscussionHeading = findViewById(R.id.DiscussionHeading);
        String Heading = "<h1>" + message +" Discussion Room"+"</h1> ";
        DiscussionHeading.setText(Html.fromHtml(Heading));

        // Create LinearLayout
//        final LinearLayout ll = new LinearLayout(this);
//        ll.setOrientation(LinearLayout.HORIZONTAL);
//        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final ConstraintLayout lm = (ConstraintLayout) findViewById(R.id.discussion_layout);

        // create the layout params that will be used to define how your
        // button will be displayed
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        final LinearLayout ll = (LinearLayout) findViewById(R.id.lol);
//        ll.setOrientation(LinearLayout.VERTICAL);


        final FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference docRef = db.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages");
//        docRef.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Boolean isPoll=document.getBoolean("IsPoll");
//                                if(isPoll==false) {
//                                    final String sourceString = "<h3>" + document.getString("MessageHead") + "</h3> " + "<p>" + document.getString("MessageBody") + "</p>";
////                                  textView2.append(Html.fromHtml(sourceString));
//                                    System.out.println(sourceString);
//                                    final String messageID=document.getId();
//                                    System.out.println(messageID);
//
//                                    // Create TextView
//
//                                    final TextView temp = new TextView(help);
//                                    temp.setTag(messageID);
//                                    temp.setText(Html.fromHtml(sourceString));
//                                    final Button btn= new Button(help);
//                                    btn.setTag(messageID);
//                                    btn.setText("Show Replies");
//
//                                    btn.setOnClickListener(new View.OnClickListener() {
//                                        public void onClick(View v) {
//                                            if(btn.getText()=="Show Replies") {
//                                                FirebaseFirestore db2 = FirebaseFirestore.getInstance();
//                                                CollectionReference docRef2 = db2.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages").document(messageID).collection("Replies");
//                                                docRef2.get()
//                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                            @Override
//                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                                if (task.isSuccessful()) {
//                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                                                        String replyString = "<p>" + document.getString("ReplyBody") + "</p>";
//                                                                        temp.append(Html.fromHtml(replyString));
//                                                                    }
//                                                                }
//                                                            }
//                                                        });
//                                            btn.setText("Hide Replies");
//                                            }
//                                            else{
//                                                temp.setText(Html.fromHtml(sourceString));
//                                                btn.setText("Show Replies");
//                                            }
//
//                                        }
//                                    });
//                                    ll.addView(temp);
//                                    ll.addView(btn);
//                                }
//                            }
//                        }
////                        else {
////                            Log.d(TAG, "Error getting documents: ", task.getException());
////                        }
//                    }
//                });

//        lm.addView(ll);
        final FirebaseFirestore db3 = FirebaseFirestore.getInstance();
        db3.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages")
                .addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
//                            Log.w(TAG, "listen:error", e);
                            return;
                        }


                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            System.out.println("first" + dc.getDocument());
                            if(dc.getDocument().getMetadata().hasPendingWrites() == true)
                                return;
                            switch (dc.getType()) {
                                case ADDED:
                                    Boolean isPoll=dc.getDocument().getBoolean("IsPoll");
                                    System.out.println(dc.getDocument().getString("MessageHead"));
//                                    if(isPoll==false) {
                                        final String sourceString = "<h3>" + dc.getDocument().getString("MessageHead") + "</h3> " + "<p>" + dc.getDocument().getString("MessageBody") + "</p>";
//                                  textView2.append(Html.fromHtml(sourceString));
                                        System.out.println(sourceString);
                                        final String messageID=dc.getDocument().getId();
                                        System.out.println(messageID);

                                        // Create TextView

                                        final TextView temp = new TextView(help);
                                        temp.setText(Html.fromHtml(sourceString));
                                        final Button btn= new Button(help);
                                        btn.setTag(messageID);
                                        btn.setText("Show Replies");

                                        btn.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                if(btn.getText()=="Show Replies") {
                                                    FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                                                    CollectionReference docRef2 = db2.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages").document(messageID).collection("Replies");
                                                    docRef2.get()
                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                                            String replyString = "<p>" + document.getString("ReplyBody") + "</p>";
                                                                            temp.append(Html.fromHtml(replyString));
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                    btn.setText("Hide Replies");
                                                }
                                                else{
                                                    temp.setText(Html.fromHtml(sourceString));
                                                    btn.setText("Show Replies");
                                                }

                                            }
                                        });
                                        ll.addView(temp);
                                        ll.addView(btn);
//                                    }
                                    break;
//                                case MODIFIED:
//                                    final String modmessageid=dc.getDocument().getId();
//                                    CollectionReference docRef = (CollectionReference) db.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages").document(modmessageid).collection("Replies").orderBy("PostTime", Query.Direction.DESCENDING).limit(1);
//                                    docRef.get()
//                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                    if (task.isSuccessful()) {
//                                                        for (QueryDocumentSnapshot document : task.getResult()) {
//                                                            View parrentView = findViewById( R.id.lol );
//                                                            TextView column_var = (TextView) parrentView.findViewWithTag(modmessageid);
//                                                            String replyString = "<p>" + document.getString("ReplyBody") + "</p>";
//                                                            column_var.append(Html.fromHtml(replyString));
//
//
//                                                        }
//                                                    }
//                                                }
//                                            });
//                                    break;
                            }
                        }

                    }
                });
    }

    public void addMessage(View view){
        Intent intent = getIntent();
        final String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        EditText MessageHead = (EditText)findViewById(R.id.editTextHead);
        String Head = MessageHead.getText().toString();
        EditText MessageBody = (EditText)findViewById(R.id.Body);
        String Body = MessageBody.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("Author", "Udbhav Chugh");
        data.put("MessageHead", Head);
        data.put("MessageBody",Body);
        data.put("PostTime", FieldValue.serverTimestamp());
        data.put("IsPoll",false);

        db.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages")
                .add(data);
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });

//        finish();
//        startActivity(getIntent());
    }


}
