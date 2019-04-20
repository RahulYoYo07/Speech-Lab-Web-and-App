package com.example.iitg_speech_lab;

import android.annotation.SuppressLint;
import android.app.AppComponentFactory;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.iitg_speech_lab.Classes.ProjectsMyData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class ViewProject extends AppCompatActivity {
    public static String ProjectID;
    VideoView videoView;
    MediaController mediaController;
    ProgressBar bufferProgress;
    FloatingActionButton fab;
    FloatingActionButton fab2;
    static String username;
    private ProgressBar spinner;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_project);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Project Details");
        username=getIntent().getStringExtra("username");
        fab = findViewById(R.id.floatingActionButton2);
        fab2 = findViewById(R.id.floatingActionButton);
        fab2.setVisibility(View.INVISIBLE);
        spinner = findViewById(R.id.progressBar6);
        ProjectID=getIntent().getStringExtra("projectID");
        spinner.setVisibility(View.VISIBLE);
        if (username.equals("")){

        } else {
            fab.setVisibility(View.VISIBLE);
            fab2.setVisibility(View.VISIBLE);
        }
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Projects").document(ProjectID);
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot user = task.getResult();
                            if (user.exists()) {
                                final String About = user.getString("AboutProject");
                                final String Title = user.getString("Title");
                                final String Mentor = user.getString("Mentor");
                                final String Contributers = user.getString("People");
                                final String Achievements =user.getString("Achievements");
                                final TextView txtTitle = findViewById(R.id.aboutProjTitle);
                                final TextView txtabout = findViewById(R.id.aboutproj);
                                final TextView txtmentor = findViewById(R.id.mentors);
                                final TextView txtcontri = findViewById(R.id.mentors2);
                                final TextView txtachieve = findViewById(R.id.achievements);
                                final String video = user.getString("Media");
                                videoView = findViewById(R.id.videoView);
                                mediaController = new MediaController(ViewProject.this);
                                Uri uri = Uri.parse(video);
                                videoView.setVideoURI(uri);
                                mediaController.setAnchorView(videoView);
                                videoView.setMediaController(mediaController);
                                spinner.setVisibility(View.INVISIBLE);
                                videoView.start();
                                txtTitle.setMovementMethod(new ScrollingMovementMethod());
                                txtabout.setMovementMethod(new ScrollingMovementMethod());
                                txtmentor.setMovementMethod(new ScrollingMovementMethod());
                                txtcontri.setMovementMethod(new ScrollingMovementMethod());
                                txtachieve.setMovementMethod(new ScrollingMovementMethod());
                                txtTitle.setTextAppearance(ViewProject.this, R.style.Title);
                                txtTitle.append(Title);
                                txtabout.setText(About);
                                txtmentor.setText(Mentor);
                                txtcontri.setText(Contributers);
                                txtachieve.setText(Achievements);
                            }
                        }
                    }
                });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(ViewProject.this, EditProject.class);
                intent.putExtra("username", username);
                intent.putExtra("projectID", ProjectID);
                startActivity(intent);
            }
        });
    }

    private void alertDialog() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Are you sure?");
        dialog.setTitle("Delete Project");
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        final FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Projects").document(ProjectID).delete();
                        finish();
                        Intent intent = new Intent(ViewProject.this, ProjectsActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                });
        dialog.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(ViewProject.this, ProjectsActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}