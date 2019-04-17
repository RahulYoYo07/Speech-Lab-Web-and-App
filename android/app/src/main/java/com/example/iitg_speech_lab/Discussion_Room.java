package com.example.iitg_speech_lab;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        db3.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages").orderBy("PostTime")
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
                                                    db2.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages").document(messageID).collection("Replies").orderBy("PostTime").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

                                        final Set<String> hash_Set = new HashSet<String>();
                                        db.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages").document(messageID).collection("Replies").orderBy("PostTime")
                                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                                                        @Nullable FirebaseFirestoreException e) {
                                                        if (e != null) {
                                                            return;
                                                        }
                                                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                                            switch (dc.getType()) {
                                                                case ADDED:
                                                                case MODIFIED:

                                                                    String ReplyBody=dc.getDocument().getString("ReplyBody");
                                                                    if(ReplyBody!=null && btn.getText()=="Hide Replies" && hash_Set.contains(dc.getDocument().getId())==false)
                                                                    {
                                                                        hash_Set.add(dc.getDocument().getId());
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
                                        final String messageID2=dc.getDocument().getId();

                                        // Create TextView
                                        String helpid=dc.getDocument().getId();
                                        View parrentView = findViewById( R.id.lol );
                                        TextView helptext = (TextView) parrentView.findViewWithTag(helpid);
                                        System.out.println(helptext);
                                        if(helptext!=null)
                                            break;

                                        final TextView temp = new TextView(help);
                                        temp.setText(Html.fromHtml(sourceString));
                                        temp.setTag(messageID2);



                                        ll.addView(temp);
                                        final RadioGroup llradio = new RadioGroup(help);
                                        llradio.setOrientation(LinearLayout.VERTICAL);
                                        llradio.setTag(messageID2);


                                        FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                                        DocumentReference docRef2 = db2.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages").document(messageID2);
                                        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    final List<String> pollop=(List<String>) document.get("PollOpt");

                                                             int ii;
                                                            for(ii=0;ii<pollop.size();ii++)
                                                            {
                                                                View parrentView = findViewById( R.id.lol );
                                                                String hel=messageID2+Integer.toString(ii);
                                                                RadioButton rdbtn=(RadioButton) parrentView.findViewWithTag(hel);
                                                                System.out.println(rdbtn);
                                                                if(rdbtn==null)
                                                                {
                                                                    rdbtn = new RadioButton(help);
//                                                                    rdbtn.setId(ii);
                                                                    rdbtn.setTag(hel);
                                                                    String rdb=pollop.get(ii) + ": Vote Count="+Integer.toString(0);
                                                                    rdbtn.setText(rdb);
                                                                    llradio.addView(rdbtn);
                                                                }
                                                                System.out.println(rdbtn);
                                                            }
                                                            final int sizee=pollop.size();
                                                         final int[] arr = new int[sizee];
                                                         for (int i = 0; i < sizee; i++) {
                                                             arr[i] = 0;
                                                         }
                                                         db.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages").document(messageID2).collection("Replies").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                             @Override
                                                             public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                                                                 if (e != null) {
                                                                     return;
                                                                 }
                                                                 for (int i = 0; i < sizee; i++) {
                                                                     arr[i] = 0;
                                                                 }
                                                    int iii=0;
                                                for(iii=0;iii<pollop.size();iii++)
                                                {
                                                    View parrentView = findViewById( R.id.lol );
                                                    String hel=messageID2+Integer.toString(iii);
                                                    RadioButton rdbtn3=(RadioButton) parrentView.findViewWithTag(hel);
                                                    String rdb=pollop.get(iii) +": Vote Count="+Integer.toString(0);
                                                    rdbtn3.setText(rdb);

                                                }
                                                                 for (QueryDocumentSnapshot doc : value) {
                                                                     if (doc.getId() != null) {
                                                                         long val = doc.getLong("ReplyBody");
                                                                         int val2 = (int) val;
                                                                         View parrentView = findViewById( R.id.lol );
                                                                         String hel=messageID2+Integer.toString(val2);
                                                                         RadioButton rdbtn=(RadioButton) parrentView.findViewWithTag(hel);
                                                                         if (rdbtn == null) {
                                                                             rdbtn = new RadioButton(help);
                                                                             rdbtn.setTag(hel);
                                                                             String rdb5 = pollop.get(val2) + ": Vote Count=" + Integer.toString(0);
                                                                             rdbtn.setText(rdb5);
                                                                             llradio.addView(rdbtn);
                                                                         }
                                                                         arr[val2]++;
                                                                         String rdb5 = pollop.get(val2) + ": Vote Count=" + Integer.toString(arr[val2]);
                                                                         rdbtn.setText(rdb5);

                                                                     }
                                                                 }



                                                             }
                                                         });

                                                }
                                            }
                                        });



                                        ll.addView(llradio);

                                        final Button replybtn= new Button(help);
                                        replybtn.setText("Vote");
                                        replybtn.setTag(messageID2);
                                        replybtn.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {


                                                int selectedId = llradio.getCheckedRadioButtonId();
                                                System.out.println(Integer.toString(selectedId));

                                                int count = llradio.getChildCount();
                                                int minn=100;
                                                ArrayList<RadioButton> listOfRadioButtons = new ArrayList<RadioButton>();
                                                for (int i=0;i<count;i++) {
                                                    View o = llradio.getChildAt(i);
                                                    if (o instanceof RadioButton) {
                                                        RadioButton help=(RadioButton)o;
                                                        int p=help.getId();
                                                        if(p<minn)
                                                            minn=help.getId();


                                                    }
                                                }
                                                int finvote=selectedId-minn;

//                                                String Body = et.getText().toString();
//                                                int radioButtonID = radioButtonGroup.getCheckedRadioButtonId();
//                                                View radioButton = radioButtonGroup.findViewById(radioButtonID);
//                                                int idx = radioButtonGroup.indexOfChild(radioButton);
//                                                System.out.println("lol"+Integer.toString(idx));
//
//
                                                Map<String, Object> data = new HashMap<>();

                                                data.put("Author", "Udbhav Chugh");
                                                data.put("ReplyBody",finvote);
                                                data.put("PostTime", FieldValue.serverTimestamp());
                                                data.put("MessageID",messageID2);

                                                db.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages").document(messageID2).collection("Replies").document("Udbhav Chugh")
                                                        .set(data);
//                                                et.setText("");

                                            }
                                        });

                                        ll.addView(replybtn);
                                    }

                                    break;

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
//        et.setTag("EditText"+Integer.toString(j));
        et.setId(j);
        System.out.println(Integer.toString(j));
        j=j+1;
        et.setHint("Add Reply");
        ll.addView(et);
    }

    public void addPoll(View view){
        if(j<2){
            alertDialog();
            return;
        }

            Intent intent = getIntent();
            final String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
//        String pollop[]=new String[10];
            List<String> pollop2 = new ArrayList<>();

            for (int i = 0; i < j; i++) {
                View parrentView2 = findViewById( R.id.layout_polloption );
//                String hel="EditText"+Integer.toString(i);
//                System.out.println(hel);
//                RadioButton rdbtn=(RadioButton) ;
                EditText et = (EditText) parrentView2.findViewById(i);
                System.out.println("lolll");
                if (et == null)
                    continue;
                System.out.println("lol");
//            pollop2[i]=et.getText().toString().trim();
                pollop2.add(et.getText().toString().trim());
//            System.out.println(pollop[i]);

            }
            j = 0;

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            TextView poll = (EditText) findViewById(R.id.pollquestion);
            String pollques = poll.getText().toString().trim();
//        List<String> vowelsList = Arrays.asList(pollop);
            Map<String, Object> data = new HashMap<>();
            data.put("Author", "Udbhav Chugh");
            data.put("PollQues", pollques);
            data.put("PollOpt", pollop2);
            data.put("PostTime", FieldValue.serverTimestamp());
            data.put("IsPoll", true);
            db.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages").add(data);
        LinearLayout lll = (LinearLayout) findViewById(R.id.layout_polloption);
        lll.removeAllViews();

    }

    public void delPollOption(View view){

        if(j==0)
            return;
        View parrentView2 = findViewById( R.id.layout_polloption );
        EditText et = (EditText) parrentView2.findViewById(j-1);
        LinearLayout help=(LinearLayout) parrentView2;
        help.removeView(et);
        j=j-1;

//        final LinearLayout ll = (LinearLayout) findViewById(R.id.layout_polloption);
//        EditText et = (EditText) findViewById(j-1);
//
//        j=j-1;
    }

    private void alertDialog() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Poll must have minimum of two options");
        dialog.setTitle("Error Message");
        dialog.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
//                        Toast.makeText(getApplicationContext(),"Yes is clicked",Toast.LENGTH_LONG).show();
                    }
                });
//        dialog.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getApplicationContext(),"cancel is clicked",Toast.LENGTH_LONG).show();
//            }
//        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }



}
