package com.example.iitg_speech_lab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.widget.CalendarView;
import android.widget.EditText;
import java.util.HashMap;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Map;

public class addAssignment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);
        String userName = "pradip";

    }
    private static final String TAG = "AddAssignmentActivity";
    public String  gid = "AS$$";


    public void addAssgn(View view){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String cinfo = "HS241_pradip_2019";

        db.collection("Courses").document(cinfo).collection("Assignments").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            gid = "AS" + Integer.toString(task.getResult().size()+1);
                            up();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void up(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String cinfo = "HS241_pradip_2019";

        Map<String, Object> newassg = new HashMap <>();
        EditText About = findViewById(R.id.About);
        EditText Title = findViewById(R.id.Title);
        CalendarView dt = findViewById(R.id.calendarView);
//        SimpleDateFormat ss = new SimpleDateFormat("dd-MM-yyyy");



        newassg.put("About", About.getText().toString());
        newassg.put("Name", Title.getText().toString());
        newassg.put("AssignmentID", gid);
        newassg.put("Deadline",dt.getDateTextAppearance() );
        Log.d(TAG, gid);


        db.collection("Courses").document(cinfo).collection("Assignments").document(gid).set(newassg)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
}
