package com.example.iitg_speech_lab;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditAboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_about_us);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit About Us Details");
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Homepage").document("AboutUs");
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot user = task.getResult();
                            if(user.exists()){
                                EditText AboutUs = (EditText) findViewById(R.id.AboutUsDetail);
                                EditText Achievements = (EditText) findViewById(R.id.AchievementsDetail);
                                EditText Head = (EditText) findViewById(R.id.HeadDetail);
                                EditText People = (EditText) findViewById(R.id.PeopleDetail);
                                EditText Established = (EditText) findViewById(R.id.EstablishedDetail);

                                AboutUs.setText(user.getString("Paragraph"));
                                Achievements.setText(user.getString("Achievements"));
                                Head.setText(user.getString("Head"));
                                People.setText(user.getString("ImportantPeople"));
                                Established.setText(user.getString("Established"));
                            }
                        }
                    }
                });
    }

    public void editAboutUsDetails(View view){
        EditText AboutUs = (EditText) findViewById(R.id.AboutUsDetail);
        EditText Achievements = (EditText) findViewById(R.id.AchievementsDetail);
        EditText Head = (EditText) findViewById(R.id.HeadDetail);
        EditText People = (EditText) findViewById(R.id.PeopleDetail);
        EditText Established = (EditText) findViewById(R.id.EstablishedDetail);
        String str;

        str=AboutUs.getText().toString();
        str.trim();
        str.replace("\\n","\n");
        AboutUs.setText(str);

        str=Achievements.getText().toString();
        str.trim();
        str.replace("\\n","\n");
        Achievements.setText(str);

        str=Head.getText().toString();
        str.trim();
        str.replace("\\n","\n");
        Head.setText(str);

        str=People.getText().toString();
        str.trim();
        str.replace("\\n","\n");
        People.setText(str);

        str=Established.getText().toString();
        str.trim();
        str.replace("\\n","\n");
        Established.setText(str);

        Map<String,String> map = new HashMap<>();
        map.put("Paragraph",AboutUs.getText().toString());
        map.put("Achievements",Achievements.getText().toString());
        map.put("Established",Established.getText().toString());
        map.put("Head",Head.getText().toString());
        map.put("ImportantPeople",People.getText().toString());

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Homepage").document("AboutUs");
        userRef.set(map);
        Toast.makeText(EditAboutUs.this,"Details Edited Successfully",Toast.LENGTH_LONG).show();
    }
}
