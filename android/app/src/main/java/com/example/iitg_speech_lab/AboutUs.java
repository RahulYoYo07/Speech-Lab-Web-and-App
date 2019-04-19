package com.example.iitg_speech_lab;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Distribution;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AboutUs extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("About Us");
        final LinearLayout ll = (LinearLayout) findViewById(R.id.linear);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Homepage").document("AboutUs");
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot user = task.getResult();
                            if(user.exists()){
                                final String Head_detail = user.getString("Head");
                                final String Establish_detail = user.getString("Established");
                                final String People_detail = user.getString("ImportantPeople");
                                final String AboutUs_detail = user.getString("Paragraph");
                                final String Achievement_detail = user.getString("Achievements");

                                if(AboutUs_detail.length() > 0){
                                    final TextView txt1 = new TextView(AboutUs.this);
                                    final TextView txt2 = new TextView(AboutUs.this);
                                    txt1.setText("About Us");
                                    txt1.setTextAppearance(AboutUs.this,R.style.AboutUs);
                                    txt2.setTextAppearance(AboutUs.this,R.style.AboutUsDetail);
                                    txt2.setText(AboutUs_detail);
                                    ll.addView(txt1);
                                    ll.addView(txt2);
                                }

                                if(Achievement_detail.length() > 0){
                                    final TextView txt1 = new TextView(AboutUs.this);
                                    final TextView txt2 = new TextView(AboutUs.this);
                                    txt1.setText("Achievements");
                                    txt1.setTextAppearance(AboutUs.this,R.style.AboutUs);
                                    txt2.setTextAppearance(AboutUs.this,R.style.AboutUsDetail);
                                    txt2.setText(Achievement_detail);
                                    ll.addView(txt1);
                                    ll.addView(txt2);
                                }

                                if(Head_detail.length() > 0){
                                    final TextView txt1 = new TextView(AboutUs.this);
                                    final TextView txt2 = new TextView(AboutUs.this);
                                    txt1.setText("Head");
                                    txt1.setTextAppearance(AboutUs.this,R.style.AboutUs);
                                    txt2.setTextAppearance(AboutUs.this,R.style.AboutUsDetail);
                                    txt2.setText(Head_detail);
                                    ll.addView(txt1);
                                    ll.addView(txt2);
                                }

                                if(People_detail.length() > 0){
                                    final TextView txt1 = new TextView(AboutUs.this);
                                    final TextView txt2 = new TextView(AboutUs.this);
                                    txt1.setText("Important People");
                                    txt1.setTextAppearance(AboutUs.this,R.style.AboutUs);
                                    txt2.setTextAppearance(AboutUs.this,R.style.AboutUsDetail);
                                    txt2.setText(People_detail);
                                    ll.addView(txt1);
                                    ll.addView(txt2);
                                }

                                if(Establish_detail.length() > 0){
                                    final TextView txt1 = new TextView(AboutUs.this);
                                    final TextView txt2 = new TextView(AboutUs.this);
                                    txt1.setText("Established");
                                    txt1.setTextAppearance(AboutUs.this,R.style.AboutUs);
                                    txt2.setTextAppearance(AboutUs.this,R.style.AboutUsDetail);
                                    txt2.setText(Establish_detail);
                                    ll.addView(txt1);
                                    ll.addView(txt2);
                                }
                            }
                        }
                    }
                });
    }

}
