package com.example.iitg_speech_lab;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditContactUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact_us);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Contact Us Details");
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Homepage").document("contactUs");
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot user = task.getResult();
                            if(user.exists()){
                                EditText Phone = (EditText) findViewById(R.id.PhoneNumberDetail);
                                EditText Email = (EditText) findViewById(R.id.EmailDetail);
                                EditText Website = (EditText) findViewById(R.id.WebsiteDetail);
                                EditText Location = (EditText) findViewById(R.id.LocationDetail);

                                Phone.setText(user.getString("PhoneNumber"));
                                Email.setText(user.getString("Email"));
                                Website.setText(user.getString("Website"));
                                Location.setText(user.getString("Location"));
                            }
                        }
                    }
                });
    }

    public void editContactUsDetails(View view){
        EditText Phone = (EditText) findViewById(R.id.PhoneNumberDetail);
        EditText Email = (EditText) findViewById(R.id.EmailDetail);
        EditText Website = (EditText) findViewById(R.id.WebsiteDetail);
        EditText Location = (EditText) findViewById(R.id.LocationDetail);
        String str;

        str=Phone.getText().toString();
        str.trim();
        str.replace("\\n","\n");
        Phone.setText(str);

        str=Email.getText().toString();
        str.trim();
        str.replace("\\n","\n");
        Email.setText(str);

        str=Website.getText().toString();
        str.trim();
        str.replace("\\n","\n");
        Website.setText(str);

        str=Location.getText().toString();
        str.trim();
        str.replace("\\n","\n");
        Location.setText(str);


        Map<String,String> map = new HashMap<>();
        map.put("Email",Email.getText().toString());
        map.put("Phone Number",Phone.getText().toString());
        map.put("Website",Website.getText().toString());
        map.put("Location",Location.getText().toString());

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Homepage").document("contactUs");
        userRef.set(map);
        Toast.makeText(EditContactUs.this,"Details Edited Successfully",Toast.LENGTH_LONG).show();
    }
}
