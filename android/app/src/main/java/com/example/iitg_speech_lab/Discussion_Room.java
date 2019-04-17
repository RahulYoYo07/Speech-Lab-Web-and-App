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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Discussion_Room extends AppCompatActivity {

    public static int j=0;
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



        final ConstraintLayout lm = (ConstraintLayout) findViewById(R.id.discussion_layout);

        // create the layout params that will be used to define how your
        // button will be displayed
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        final LinearLayout ll = (LinearLayout) findViewById(R.id.lol);
//        ll.setOrientation(LinearLayout.VERTICAL);


        final FirebaseFirestore db = FirebaseFirestore.getInstance();
//
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

                            switch (dc.getType()) {
                                case ADDED:
                                case MODIFIED:
                                    Boolean isPoll=dc.getDocument().getBoolean("IsPoll");
//                                    System.out.println(dc.getDocument().getString("MessageHead"));
                                    if(isPoll!=null && isPoll==false) {
                                        final String sourceString = "<h3>" + dc.getDocument().getString("MessageHead") + "</h3> " + "<p>" + dc.getDocument().getString("MessageBody") + "</p>";
                                        final String messageID=dc.getDocument().getId();

                                        // Create TextView
                                        String helpid=dc.getDocument().getId();
                                        View parrentView = findViewById( R.id.lol );
                                        TextView helptext = (TextView) parrentView.findViewWithTag(helpid);
                                        System.out.println(helptext);
                                        if(helptext!=null)
                                            break;

                                        final TextView temp = new TextView(help);
                                        temp.setText(Html.fromHtml(sourceString));
                                        temp.setTag(messageID);
                                        final Button btn= new Button(help);
                                        btn.setTag(messageID);
                                        btn.setText("Show Replies");
                                        final EditText et = new EditText(help);
                                        et.setHint("Add Reply");
                                        final Button replybtn= new Button(help);
                                        replybtn.setText("Reply");
                                        replybtn.setTag(messageID);

                                        replybtn.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {

                                                if(et.getText().toString()=="")
                                                    return;

                                                String Body = et.getText().toString();


                                                Map<String, Object> data = new HashMap<>();
                                                data.put("Author", "Udbhav Chugh");
                                                data.put("ReplyBody",Body);
                                                data.put("PostTime", FieldValue.serverTimestamp());
                                                data.put("MessageID",messageID);

                                                db.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages").document(messageID).collection("Replies")
                                                        .add(data);
                                                et.setText("");

                                            }
                                        });


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
                                        db.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages").document(messageID).collection("Replies")
                                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                                                        @Nullable FirebaseFirestoreException e) {
                                                        if (e != null) {
                                                            return;
                                                        }
                                                        int count=0;
                                                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                                            switch (dc.getType()) {
                                                                case ADDED:
                                                                case MODIFIED:

                                                                    String ReplyBody=dc.getDocument().getString("ReplyBody");
                                                                    if(ReplyBody!=null && btn.getText()=="Hide Replies" && count==0)
                                                                    {
                                                                        count=1;
                                                                        String replyString = "<p>" + dc.getDocument().getString("ReplyBody") + "</p>";
                                                                        temp.append(Html.fromHtml(replyString));
                                                                    }

                                                                    break;
                                                            }
                                                        }

                                                    }
                                                });

                                        ll.addView(temp);
                                        ll.addView(btn);
                                        ll.addView(et);
                                        ll.addView(replybtn);
                                    }

                                    if(isPoll!=null && isPoll==true) {
                                        final String sourceString = "<h3>" + dc.getDocument().getString("PollQues") + "</h3> ";

//                                        final String sourceString = "<h3>" + dc.getDocument().getString("PollQues") + "</h3> " + "<p>" + dc.getDocument().getString("opt") + "</p>";
                                        final String messageID=dc.getDocument().getId();

                                        // Create TextView
                                        String helpid=dc.getDocument().getId();
                                        View parrentView = findViewById( R.id.lol );
                                        TextView helptext = (TextView) parrentView.findViewWithTag(helpid);
                                        System.out.println(helptext);
                                        if(helptext!=null)
                                            break;

                                        final TextView temp = new TextView(help);
                                        temp.setText(Html.fromHtml(sourceString));
                                        temp.setTag(messageID);
                                        final Button btn= new Button(help);
                                        btn.setTag(messageID);
                                        btn.setText("Show Options");
//                                        final EditText et = new EditText(help);
//                                        et.setHint("Add Reply");
//                                        final Button replybtn= new Button(help);
//                                        replybtn.setText("Reply");
//                                        replybtn.setTag(messageID);

//                                        replybtn.setOnClickListener(new View.OnClickListener() {
//                                            public void onClick(View v) {
//
//                                                if(et.getText().toString()=="")
//                                                    return;
//
//                                                String Body = et.getText().toString();
//
//
//                                                Map<String, Object> data = new HashMap<>();
//                                                data.put("Author", "Udbhav Chugh");
//                                                data.put("ReplyBody",Body);
//                                                data.put("PostTime", FieldValue.serverTimestamp());
//                                                data.put("MessageID",messageID);
//
//                                                db.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages").document(messageID).collection("Replies")
//                                                        .add(data);
//                                                et.setText("");
//
//                                            }
//                                        });


                                        ll.addView(temp);
                                        ll.addView(btn);
                                        btn.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                if(btn.getText()=="Show Options") {
                                                    FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                                                    DocumentReference docRef2 = db2.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages").document(messageID);
                                                    docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                DocumentSnapshot document = task.getResult();
//                                                                            String replyString = "<p>" + document.getClass("PollOpt") + "</p>";
                                                                List<String> pollop=(List<String>) document.get("PollOpt");
                                                                for (int row = 0; row < 1; row++) {
                                                                    final RadioGroup llradio = new RadioGroup(help);
                                                                    llradio.setOrientation(LinearLayout.VERTICAL);

                                                                      for(int i=0;i<pollop.size();i++)
                                                                        {
//                                                                    temp.append(pollop.get(i));
//                                                                    temp.append("\n");
                                                                            RadioButton rdbtn = new RadioButton(help);
                                                                            rdbtn.setId(i);
                                                                            rdbtn.setText(pollop.get(i));
                                                                            llradio.addView(rdbtn);
                                                                            }
                                                                            ll.addView(llradio);
                                                                        }
//                                                                temp.append(Html.fromHtml(replyString));
                                                                    }
                                                                }
                                                            });
                                                    btn.setText("Hide Options");
                                                }
                                                else{
                                                    temp.setText(Html.fromHtml(sourceString));
                                                    btn.setText("Show Options");
                                                }

                                            }
                                        });
//                                        ll.addView(et);
//                                        ll.addView(replybtn);
                                    }

                                    break;

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
//        try {
//            //  Block of code to try
//            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
//                    new Fragment_AddMessage()).commit();
//        }
//        catch(Exception e) {
//            //  Block of code to handle errors
//            System.out.println(e.getMessage());
//        }

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
    }

    public void addPollOption(View view){

        final LinearLayout ll = (LinearLayout) findViewById(R.id.layout_polloption);
        final EditText et = new EditText(this);
        et.setId(j);
        j=j+1;
        et.setHint("Add Reply");
        ll.addView(et);
    }

    public void addPoll(View view){
        Intent intent = getIntent();
        final String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
//        String pollop[]=new String[10];
        List<String> pollop = new ArrayList<>();

        for(int i=0;i<j;i++)
        {
            EditText et = (EditText) findViewById(i);
            if(et==null)
                continue;
//            pollop[i]=et.getText().toString().trim();
            pollop.add(et.getText().toString().trim());
//            System.out.println(pollop[i]);

        }
        j=0;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        TextView poll=(EditText) findViewById(R.id.pollquestion);
        String pollques=poll.getText().toString().trim();
//        List<String> vowelsList = Arrays.asList(pollop);
        Map<String, Object> data = new HashMap<>();
        data.put("Author", "Udbhav Chugh");
        data.put("PollQues", pollques);
        data.put("PollOpt",pollop);
        data.put("PostTime", FieldValue.serverTimestamp());
        data.put("IsPoll",true);
            db.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages").add(data);

    }

    public void delPollOption(View view){

        if(j==0)
            return;
        return;
//        final LinearLayout ll = (LinearLayout) findViewById(R.id.layout_polloption);
//        EditText et = (EditText) findViewById(j-1);
//
//        j=j-1;
    }



}
