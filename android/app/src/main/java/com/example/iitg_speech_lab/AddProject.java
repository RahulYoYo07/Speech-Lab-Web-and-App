package com.example.iitg_speech_lab;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AddProject extends AppCompatActivity {
    static String GetUsername = "tusha170101073";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Project");
    }

    public void AddProject(View view){
        EditText Title = (EditText) findViewById(R.id.TitleDetail);
        EditText AboutProject = (EditText) findViewById(R.id.AboutProjectDetail);
        EditText Mentor = (EditText) findViewById(R.id.MentorDetail);
        EditText People = (EditText) findViewById(R.id.ProjectPeopleDetail);
        EditText Achievements = (EditText) findViewById(R.id.ProjectAchievementsDetails);
        String str;

        str=Title.getText().toString();
        str.trim();
        str.replace("\\n","\n");
        Title.setText(str);

        str=Achievements.getText().toString();
        str.trim();
        str.replace("\\n","\n");
        Achievements.setText(str);

        str=AboutProject.getText().toString();
        str.trim();
        str.replace("\\n","\n");
        AboutProject.setText(str);

        str=People.getText().toString();
        str.trim();
        str.replace("\\n","\n");
        People.setText(str);

        str=Mentor.getText().toString();
        str.trim();
        str.replace("\\n","\n");
        Mentor.setText(str);

        Map<String,Object> map = new HashMap<>();
        map.put("Title",Title.getText().toString());
        map.put("Achievements",Achievements.getText().toString());
        map.put("Mentor",Mentor.getText().toString());
        map.put("AboutProject",AboutProject.getText().toString());
        map.put("People",People.getText().toString());


        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference dref = db.collection("Users").document(GetUsername);
        map.put("Creator",dref);
        db.collection("Projects").add(map);
        Title.setText("");
        Achievements.setText("");
        People.setText("");
        Mentor.setText("");
        AboutProject.setText("");
        Toast.makeText(AddProject.this,"Project added successfully",Toast.LENGTH_LONG).show();
    }
}
