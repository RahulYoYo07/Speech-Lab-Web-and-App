package com.example.iitg_speech_lab;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.PublicClientApplication;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PrivateProfileDetails extends AppCompatActivity {
    private static final String TAG = PrivateProfileDetails.class.getSimpleName();
    private static final long START_TIME_IN_MILLIS = 600000;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    static int check=0;
    static int  kyaadminh=1;
    static Boolean adminhkya=false;
    static String isfirst;
    private ProgressBar spinner;
    static String GetUsername;
    private PublicClientApplication sampleApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_profile_details);
        isfirst=getIntent().getStringExtra("isfirst");
        GetUsername = getIntent().getStringExtra("username");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");
        //Code For Sliding Images

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
        spinner = (ProgressBar) findViewById(R.id.progress_discussion);
        spinner.setVisibility(View.VISIBLE);
        //Log.d("tushar",Name.getText().toString());
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference userRef = db.collection("Users").document(GetUsername);
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot user = task.getResult();
                            spinner.setVisibility(View.GONE);
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
                                if(user.getString("ProfilePic").length() > 0){
                                    CircleImageView img = (CircleImageView) findViewById(R.id.profile);
                                    String url = user.getString("ProfilePic");
                                    Picasso.get().load(url).into(img);
                                }
                            }
                        }
                    }
                });
    }

}