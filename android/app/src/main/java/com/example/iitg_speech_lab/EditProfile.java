package com.example.iitg_speech_lab;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    final String GetUsername = "pradip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        final TextView Name = (TextView) findViewById(R.id.EditProfileDisplayNameDetail);
        final TextView Username = (TextView) findViewById(R.id.EditProfileDisplayUsernameDetail);
        final EditText Program = (EditText) findViewById(R.id.EditProfileDisplayProgramDetail);
        final EditText Dept = (EditText) findViewById(R.id.EditProfileDisplayDeptDetail);
        final EditText Contact = (EditText) findViewById(R.id.EditProfileDisplayContactDetail);
        final EditText Email = (EditText) findViewById(R.id.EditProfileDisplayEmailDetail);
        final EditText About = (EditText) findViewById(R.id.EditProfileDisplayAboutDetail);
        final EditText Website = (EditText) findViewById(R.id.EditProfileDisplayWebsiteDetail);
        final EditText Github = (EditText) findViewById(R.id.EditProfileDisplayGithubDetail);
        final EditText LinkedIn = (EditText) findViewById(R.id.EditProfileDisplayLinkedInDetail);
        final TextView ProgramDecide = (TextView) findViewById(R.id.EditProfileDisplayProgram);
        final EditText Room = (EditText) findViewById(R.id.EditProfileDisplayRoomDetail);
        final TextView RoomDecide = (TextView) findViewById(R.id.EditProfileDisplayRoom);
        //Log.d("tushar",Name.getText().toString());
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference userRef = db.collection("Users").document(GetUsername);
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot user = task.getResult();
                            if (user.exists()) {
                                Name.setText(user.getString("FullName"));
                                Username.setText(user.getString("Username"));
                                Dept.setText(user.getString("Department"));
                                Contact.setText(user.getString("Contact"));
                                About.setText(user.getString("About"));
                                Email.setText(user.getString("Email"));
                                Map<String, String> map = new HashMap<>();
                                map = (Map<String, String>) user.get("URL");
                                Website.setText(map.get("Homepage"));
                                Github.setText(map.get("Github"));
                                LinkedIn.setText(map.get("Linkedin"));
                                if (user.getString("Designation").equals("Student")) {
                                    Program.setText(user.getString("Program"));
                                    Room.setText(user.getString("RollNumber"));
                                    RoomDecide.setText("Roll Number");
                                } else {
                                    Program.setText(user.getString("CollegeDesignation"));
                                    ProgramDecide.setText("Designation: ");
                                    Room.setText(user.getString("RoomNumber"));
                                }
                                if (user.getString("ProfilePic").length() > 0) {
                                    CircleImageView img = (CircleImageView) findViewById(R.id.EditProfileprofile);
                                    String url = user.getString("ProfilePic");
                                    Picasso.with(getApplicationContext()).load(url).into(img);
                                }
                            }
                        }
                    }
                });
    }
}
