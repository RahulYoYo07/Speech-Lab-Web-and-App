package com.example.iitg_speech_lab;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class PrivateProfileDetails extends AppCompatActivity {
    final String GetUsername = "tusha170101073";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_profile_details);
        final TextView Name = (TextView) findViewById(R.id.DisplayNameDetail);
        final TextView Username = (TextView) findViewById(R.id.DisplayUsernameDetail);
        final TextView Program = (TextView) findViewById(R.id.DisplayProgramDetail);
        final TextView Dept = (TextView) findViewById(R.id.DisplayDeptDetail);
        final TextView Contact = (TextView) findViewById(R.id.DisplayContactDetail);
        final TextView Email = (TextView) findViewById(R.id.DisplayEmailDetail);
        final TextView About = (TextView) findViewById(R.id.DisplayAboutDetail);
        final TextView Website = (TextView) findViewById(R.id.DisplayWebsiteDetail);
        final TextView Github = (TextView) findViewById(R.id.DisplayGithubDetail);
        final TextView LinkedIn = (TextView) findViewById(R.id.DisplayLinkedInDetail);
        final TextView ProgramDecide = (TextView) findViewById(R.id.DisplayProgram);
        final TextView Room = (TextView) findViewById(R.id.DisplayRoomDetail);
        final TextView RoomDecide = (TextView) findViewById(R.id.DisplayRoom);
        //Log.d("tushar",Name.getText().toString());
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference userRef = db.collection("Users").document(GetUsername);
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot user = task.getResult();
                            if(user.exists()){
                                Name.setText(user.getString("FullName"));
                                Username.setText(user.getString("Username"));
                                Dept.setText(user.getString("Department"));
                                Contact.setText(user.getString("Contact"));
                                About.setText(user.getString("About"));
                                Email.setText(user.getString("Email"));
                                Map<String,String> map =new HashMap<>();
                                map = (Map<String,String>) user.get("URL");
                                Website.setText(map.get("Homepage"));
                                Github.setText(map.get("Github"));
                                LinkedIn.setText(map.get("Linkedin"));
                                if(user.getString("Designation").equals("Student")){
                                    Program.setText(user.getString("Program"));
                                    Room.setText(user.getString("RollNumber"));
                                    RoomDecide.setText("Roll Number");
                                }
                                else{
                                    Program.setText(user.getString("CollegeDesignation"));
                                    ProgramDecide.setText("Designation: ");
                                    Room.setText(user.getString("RoomNumber"));
                                }
                                //Log.d("tushar",Name.getText().toString());
                                //Toast.makeText(PrivateProfileDetails.this,Name.getText().toString(),Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
}