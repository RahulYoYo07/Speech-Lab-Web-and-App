package com.example.iitg_speech_lab;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
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
    public String m_head;
    public String m_body;
    public String p_head;
    private ProgressBar spinner;
    public String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion__room);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        final Discussion_Room help=this;
        Intent intent = getIntent();
        final String message = intent.getStringExtra(AllDiscussionRooms.EXTRA_MESSAGE);
        TextView heading= findViewById(R.id.DiscussionHeading);
        heading.append(message);
        username=intent.getStringExtra("username");

        TextView DiscussionHeading = findViewById(R.id.DiscussionHeading);
        String Heading = "<h1>" + message +" Discussion Room"+"</h1> ";
        DiscussionHeading.setText(Html.fromHtml(Heading));


        spinner = findViewById(R.id.progress_discussion);

        spinner.setVisibility(View.VISIBLE);
//        final ConstraintLayout lm = (ConstraintLayout) findViewById(R.id.discussion_layout);
//
//        // create the layout params that will be used to define how your
//        // button will be displayed
//        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
//                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        ConstraintLayout lm = findViewById(R.id.discussion_layout);
        ViewGroup.LayoutParams para=lm.getLayoutParams();
//
        final LinearLayout ll = findViewById(R.id.lol);
////        ll.setOrientation(LinearLayout.VERTICAL);
//
        ScrollView sv= findViewById(R.id.scrollView2);
        ViewGroup.LayoutParams parall = sv.getLayoutParams();
//
//                System.out.println(parall.height);
//                System.out.println(para.height);
        parall.height = ((para.height) * 1) / 10;
        parall.width = (para.width * 1) / 10;
        sv.setLayoutParams(parall);

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

                        spinner.setVisibility(View.GONE);
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {

                            switch (dc.getType()) {
                                case ADDED:
                                case MODIFIED:
                                    Boolean isPoll=dc.getDocument().getBoolean("IsPoll");
//                                    System.out.println(dc.getDocument().getString("MessageHead"));
                                    if(isPoll!=null && isPoll==false) {
                                        final String sourceString = "<br><h3><strong>"+dc.getDocument().getString("Author")+"</strong>:  " + dc.getDocument().getString("MessageHead") + "</h3> " + "<h5>" + dc.getDocument().getString("MessageBody") + "</h5>";
                                        final String messageID=dc.getDocument().getId();

                                        // Create TextView
                                        String helpid=dc.getDocument().getId();
                                        View parrentView = findViewById( R.id.lol );
                                        TextView helptext = parrentView.findViewWithTag(helpid);
//                                        System.out.println(helptext);
                                        if(helptext!=null)
                                            break;

                                        final TextView temp = new TextView(help);
                                        temp.setText(Html.fromHtml(sourceString));
                                        temp.setTag(messageID);
                                        final Button btn= new Button(help);

                                        btn.setTag(messageID);
                                        btn.setText("Show Replies");
//                                        btn.setHeight(10);
//                                        btn.setWidth(10);
//                                        btn.setLayoutParams(params);
//                                        btn.set
                                        btn.setBackgroundColor(Color.parseColor("#00b2cc"));
                                        final EditText et = new EditText(help);
                                        et.setHint("Add Reply");
                                        final Button replybtn= new Button(help);
                                        replybtn.setText("Reply");
                                        replybtn.setTag(messageID);
                                        replybtn.setBackgroundColor(Color.parseColor("#d89de7"));
                                        replybtn.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {

                                                if(et.getText().toString().trim().length()==0) {
                                                    alertDialogempty();
                                                    return;
                                                }

                                                String Body = et.getText().toString().trim();


                                                Map<String, Object> data = new HashMap<>();
                                                data.put("Author", username);
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
                                                                int cc=0;
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    String replyString;
                                                                    if(cc%2==0)
                                                                    {
                                                                        replyString="<strong>"+document.getString("Author")+"</strong>:  <b><font color=#00cc37>"+document.getString("ReplyBody") + "</font></b><br>";
                                                                        cc=1;
                                                                    }
                                                                    else
                                                                    {
                                                                        replyString="<strong>"+document.getString("Author")+"</strong>:  <b><font color=#007fcc>"+document.getString("ReplyBody") + "</font></b><br>";
                                                                        cc=0;
                                                                    }
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
                                                                        String replyString="<strong>"+dc.getDocument().getString("Author")+"</strong>:  <b><font color=#00cc37>"+dc.getDocument().getString("ReplyBody") + "</font></b><br>";
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
                                        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) btn.getLayoutParams();
                                        params1.width=300;
                                        params1.height=150;
                                        btn.setLayoutParams(params1);
                                        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) replybtn.getLayoutParams();
                                        params3.width=300;
                                        params3.height=150;
                                        replybtn.setLayoutParams(params3);

                                    }

                                    if(isPoll!=null && isPoll==true) {
                                        final String sourceString = "<br><h3><strong>"+dc.getDocument().getString("Author")+"</strong>:  " + dc.getDocument().getString("PollQues") + "</h3> ";
                                        final String messageID2=dc.getDocument().getId();

                                        // Create TextView
                                        String helpid=dc.getDocument().getId();
                                        View parrentView = findViewById( R.id.lol );
                                        TextView helptext = parrentView.findViewWithTag(helpid);
//                                        System.out.println(helptext);
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
                                                        RadioButton rdbtn= parrentView.findViewWithTag(hel);
//                                                                System.out.println(rdbtn);
                                                        if(rdbtn==null)
                                                        {
                                                            rdbtn = new RadioButton(help);
//                                                                    rdbtn.setId(ii);
                                                            rdbtn.setTag(hel);

                                                            String rdb=pollop.get(ii) + "<b>: Vote Count</b>="+Integer.toString(0);
                                                            rdbtn.setText(Html.fromHtml(rdb));
                                                            llradio.addView(rdbtn);
                                                        }
//                                                                System.out.println(rdbtn);
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
                                                                RadioButton rdbtn3= parrentView.findViewWithTag(hel);
                                                                String rdb=pollop.get(iii) +"<b>: Vote Count</b>="+Integer.toString(0);
                                                                rdbtn3.setText(Html.fromHtml(rdb));

                                                            }
//                                                                 Set<String> voted = new HashSet<String>();
                                                            for (QueryDocumentSnapshot doc : value) {
                                                                if (doc.getId() != null) {
//                                                                         String voter=doc.getString("Author");
//                                                                         voted.add(voter);
                                                                    long val = doc.getLong("ReplyBody");
                                                                    int val2 = (int) val;
                                                                    View parrentView = findViewById( R.id.lol );
                                                                    String hel=messageID2+Integer.toString(val2);
                                                                    RadioButton rdbtn= parrentView.findViewWithTag(hel);
                                                                    if (rdbtn == null) {
                                                                        rdbtn = new RadioButton(help);
                                                                        rdbtn.setTag(hel);

                                                                        String rdb5 = pollop.get(val2) + "<b>: Vote Count</b>=" + Integer.toString(0);
                                                                        rdbtn.setText(Html.fromHtml(rdb5));
                                                                        llradio.addView(rdbtn);
                                                                    }
                                                                    arr[val2]++;
                                                                    String voter=doc.getString("Author");
                                                                    System.out.println(voter);
                                                                    if(voter.equals(username))
                                                                    {
                                                                        int oo=rdbtn.getId();
                                                                        System.out.println(Integer.toString(oo));
                                                                        llradio.check(rdbtn.getId());
                                                                    }
                                                                    String rdb5 = pollop.get(val2) + "<b>: Vote Count</b>=" + Integer.toString(arr[val2]);
                                                                    rdbtn.setText(Html.fromHtml(rdb5));

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
//                                                System.out.println(Integer.toString(selectedId));

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

                                                Map<String, Object> data = new HashMap<>();

                                                data.put("Author", username);
                                                data.put("ReplyBody",finvote);
                                                data.put("PostTime", FieldValue.serverTimestamp());
                                                data.put("MessageID",messageID2);

                                                db.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages").document(messageID2).collection("Replies").document(username)
                                                        .set(data);
//                                                et.setText("");

                                            }
                                        });

                                        ll.addView(replybtn);
                                        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) replybtn.getLayoutParams();
                                        params1.width=300;
                                        params1.height=150;
                                        replybtn.setLayoutParams(params1);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Start a Q&A");

//        Context context = mapView.getContext();
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

// Add a TextView here for the "Title" label, as noted in the comments
        final EditText titleBox = new EditText(this);
        titleBox.setHint("Message Head");
//        titleBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(titleBox); // Notice this is an add method

// Add another TextView here for the "Description" label
        final EditText descriptionBox = new EditText(this);
        descriptionBox.setHint("Description");
//        descriptionBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(descriptionBox); // Another add method

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_head = titleBox.getText().toString().trim();
                m_body = descriptionBox.getText().toString().trim();
                if(m_head.length()==0 || m_body.length()==0){
                    alertDialogempty();
                    return;
                }
                Intent intent = getIntent();
                final String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
                String Head=m_head;
                String Body=m_body;
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Map<String, Object> data = new HashMap<>();
                data.put("Author", username);
                data.put("MessageHead", Head);
                data.put("MessageBody",Body);
                data.put("PostTime", FieldValue.serverTimestamp());
                data.put("IsPoll",false);

                db.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages")
                        .add(data);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    public void addPoll(View view){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Poll");

//        Context context = mapView.getContext();
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

// Add a TextView here for the "Title" label, as noted in the comments
        final EditText titleBox = new EditText(this);
        titleBox.setHint("Poll Question");
        layout.addView(titleBox); // Notice this is an add method
        final Discussion_Room help2=this;

        final Button addopt= new Button(this);
        addopt.setText("Add Poll Option");
        layout.addView(addopt);

        addopt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText et = new EditText(help2);
                et.setId(j);
//                System.out.println(Integer.toString(j));
                j=j+1;
                et.setHint("Add Poll Option");
                layout.addView(et);
            }
        });

        final Button delopt= new Button(this);
        delopt.setText("Delete last Poll Option");
        layout.addView(delopt);

        delopt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(j==0)
                    return;
                EditText et = layout.findViewById(j-1);
                LinearLayout help= layout;
                help.removeView(et);
                j=j-1;
            }
        });

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(j<2){
                    alertDialog();
                    return;
                }
                p_head = titleBox.getText().toString().trim();
                if(p_head.length()==0)
                {
                    alertDialogempty();
                    return;
                }
                Intent intent = getIntent();
                final String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
                List<String> pollop2 = new ArrayList<>();

                for (int i = 0; i < j; i++) {

                    EditText et = layout.findViewById(i);
//                    System.out.println("lolll");
                    if (et == null)
                        continue;
//                    System.out.println("lol");
                    String kk=et.getText().toString().trim();
                    if(kk.length()==0)
                    {
                        alertDialogempty();
                        return;
                    }
                    pollop2.add(kk);

                }
                j = 0;

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String pollques = p_head;
//        List<String> vowelsList = Arrays.asList(pollop);
                Map<String, Object> data = new HashMap<>();
                data.put("Author", username);
                data.put("PollQues", pollques);
                data.put("PollOpt", pollop2);
                data.put("PostTime", FieldValue.serverTimestamp());
                data.put("IsPoll", true);
                db.collection("Courses").document(message).collection("CourseGroup").document("1").collection("Messages").add(data);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

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

    private void alertDialogempty() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Text field has no text added");
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

