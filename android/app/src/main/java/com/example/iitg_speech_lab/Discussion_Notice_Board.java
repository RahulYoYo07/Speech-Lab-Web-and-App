package com.example.iitg_speech_lab;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.HashMap;
import java.util.Map;

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
        db.collection("Courses").document(message).collection("Notices").orderBy("NoticeTime").get()
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Notice");

//        Context context = mapView.getContext();
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

// Add a TextView here for the "Title" label, as noted in the comments
        final EditText titleBox = new EditText(this);
        titleBox.setHint("Notice Title");
//        titleBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(titleBox); // Notice this is an add method

// Add another TextView here for the "Description" label
        final EditText descriptionBox = new EditText(this);
        descriptionBox.setHint("Notice Body");
//        descriptionBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(descriptionBox); // Another add method

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = getIntent();
                String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
                String Head = titleBox.getText().toString().trim();
                String Body = descriptionBox.getText().toString().trim();

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Map<String, Object> data = new HashMap<>();
                data.put("Author", "Udbhav Chugh");
                data.put("NoticeHead", Head);
                data.put("NoticeBody",Body);
                data.put("NoticeTime", ServerValue.TIMESTAMP);

                db.collection("Courses").document(message).collection("Notices")
                        .add(data);
                finish();
                startActivity(getIntent());
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
}
