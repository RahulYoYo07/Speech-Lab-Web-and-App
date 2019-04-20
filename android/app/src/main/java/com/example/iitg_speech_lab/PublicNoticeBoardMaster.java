package com.example.iitg_speech_lab;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
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
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//@IgnoreExtraProperties
//public class Notice {
//
//    public String NoticeHead;
//    public String NoticeBody;
//    public Timestamp NoticeTime;
//    public String Author;
//
//    public Notice() {
//        // Default constructor required for calls to DataSnapshot.getValue(User.class)
//    }
//
//    public Notice(String Author, String NoticeHead,String NoticeBody,Timestamp NoticeTime) {
//        this.Author = Author;
//        this.NoticeHead = NoticeHead;
//        this.NoticeBody = NoticeBody;
//        this.NoticeTime=NoticeTime;
//    }
//
//}


public class PublicNoticeBoardMaster extends AppCompatActivity {

    public String username;
    public ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_notice_board_master);

        spinner = (ProgressBar) findViewById(R.id.progressBar14);

        spinner.setVisibility(View.VISIBLE);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        TextView NoticeHeading = findViewById(R.id.NoticeHeading3);
//        "<font color=#cc0029>First Color</font> <font color=#ffcc00>Second Color</font>";
        String Heading = "<h1>Public" +" NOTICE BOARD"+"</h1>";
//        NoticeHeading.setBackgroundResource( R.drawable.border_style);

        NoticeHeading.setText(Html.fromHtml(Heading));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Homepage").document("NoticeBoard")
                .addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        spinner.setVisibility(View.GONE);

                        TextView textV = findViewById(R.id.textViewtempp2);
                        textV.setText("");
                        if (snapshot != null && snapshot.exists()) {
                            if (snapshot.getId() != null) {
                                System.out.println("bc");
                                TextView textView2 = findViewById(R.id.textViewtempp2);
                                List<HashMap> hm=(List<HashMap>)snapshot.get("AllNotices");
                                for(int i=0;i<hm.size();i++){
                                    String sourceString = "<h2><font color=#990026>" + hm.get(i).get("title")+"</font></h2> " + "<p><font color=#007fcc>"+ hm.get(i).get("text")+"</font></p>";
                                    textView2.append(Html.fromHtml(sourceString));}
                            }
                        }
                    }
                });


    }

}

