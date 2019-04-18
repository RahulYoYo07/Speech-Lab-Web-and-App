package com.example.iitg_speech_lab;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class ContactUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Contact Us");
        final ArrayList<String> fields = new ArrayList<String>();
        final ArrayList<String> values = new ArrayList<String>();
        final ConstraintLayout lm = (ConstraintLayout) findViewById(R.id.main_layout);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        final LinearLayout ll = (LinearLayout) findViewById(R.id.linear);
        ll.removeAllViews();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Homepage").document("contactUs");
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot user = task.getResult();
                            if(user.exists()){
                                //final String Head_detail = user.getString("Head");
                                final String Phone_detail = user.getString("PhoneNumber");
                                final String Email_detail = user.getString("Email");
                                final String Website_detail = user.getString("Website");
                                final String Location_detail = user.getString("Location");

//                                if(Head_detail.length() > 0){
//                                    final TextView txt1 = new TextView(ContactUs.this);
//                                    final TextView txt2 = new TextView(ContactUs.this);
//                                    txt1.setText("Head");
//                                    txt1.setTextAppearance(ContactUs.this,R.style.ContactUsDetailField);
//                                    txt2.setTextAppearance(ContactUs.this,R.style.ContactUsDetail);
//                                    txt2.setText(Head_detail);
//                                    ll.addView(txt1);
//                                    ll.addView(txt2);
//                                }

                                if(Phone_detail.length() > 0){
                                    final TextView txt1 = new TextView(ContactUs.this);
                                    final TextView txt2 = new TextView(ContactUs.this);
                                    txt1.setText("Phone Number");
                                    txt1.setTextAppearance(ContactUs.this,R.style.ContactUsDetailField);
                                    txt2.setTextAppearance(ContactUs.this,R.style.ContactUsDetail);
                                    txt2.setText(Phone_detail);
                                    ll.addView(txt1);
                                    ll.addView(txt2);
                                }

                                if(Email_detail.length() > 0){
                                    final TextView txt1 = new TextView(ContactUs.this);
                                    final TextView txt2 = new TextView(ContactUs.this);
                                    txt1.setText("Email");
                                    txt1.setTextAppearance(ContactUs.this,R.style.ContactUsDetailField);
                                    txt2.setTextAppearance(ContactUs.this,R.style.ContactUsDetail);
                                    txt2.setText(Email_detail);
                                    ll.addView(txt1);
                                    ll.addView(txt2);
                                }

                                if(Website_detail.length() > 0){
                                    final TextView txt1 = new TextView(ContactUs.this);
                                    final TextView txt2 = new TextView(ContactUs.this);
                                    txt1.setText("Website");
                                    txt1.setTextAppearance(ContactUs.this,R.style.ContactUsDetailField);
                                    txt2.setTextAppearance(ContactUs.this,R.style.ContactUsDetail);
                                    txt2.setText(Website_detail);
                                    ll.addView(txt1);
                                    ll.addView(txt2);
                                }

                                if(Location_detail.length() > 0){
                                    final TextView txt1 = new TextView(ContactUs.this);
                                    final TextView txt2 = new TextView(ContactUs.this);
                                    txt1.setText("Location");
                                    txt1.setTextAppearance(ContactUs.this,R.style.ContactUsDetailField);
                                    txt2.setTextAppearance(ContactUs.this,R.style.ContactUsDetail);
                                    txt2.setText(Location_detail);
                                    ll.addView(txt1);
                                    ll.addView(txt2);
                                }
                            }
                        }
                    }
                });
    }
}
