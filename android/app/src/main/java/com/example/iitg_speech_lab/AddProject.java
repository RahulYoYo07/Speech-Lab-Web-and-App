package com.example.iitg_speech_lab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddProject extends AppCompatActivity {
    static String GetUsername;

    String durl="";
    private Uri filePath;
    private static final int PICK_FILE_REQUEST = 234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        GetUsername = getIntent().getStringExtra("username");
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
        map.put("Media", durl);

        if(Title.getText().toString().length()==0){
            Toast.makeText(getApplicationContext(),"Please enter valid non-empty Title",Toast.LENGTH_LONG).show();
        } else if(People.getText().toString().length()==0){
            Toast.makeText(getApplicationContext(),"Please enter valid non-empty people's names in People involved",Toast.LENGTH_LONG).show();
        } else if(Mentor.getText().toString().length()==0){
            Toast.makeText(getApplicationContext(),"Please enter valid non-empty Mentor Name",Toast.LENGTH_LONG).show();
        } else{

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

    //method to show file chooser
    public void showFileChooser1(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Button buttonChoose = findViewById(R.id.UploadVideo);
            filePath = data.getData();
//            buttonChoose.setText(filePath.toString());
            uploadFile();
        }
    }


    //this method will upload the file
    protected void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            // Create a storage reference from our app
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference storageRef = storage.getReference();

            // File or Blob
//            file = Uri.fromFile(new File("path/to/mountains.jpg"));

            final StorageReference ref = storageRef.child("Projects/"+GetUsername+"/"+filePath.getLastPathSegment());
            UploadTask uploadTask = ref.putFile(filePath);

            // Listen for state changes, errors, and completion of the upload.
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    System.out.println("Upload is " + progress + "% done");
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Upload is paused");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Upload is complete");
                    progressDialog.hide();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                    {
                        @Override
                        public void onSuccess(Uri downloadUrl)
                        {
                            durl=downloadUrl.toString();
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Video Uploaded Successfully",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
                }
            });
        }
        //if there is not any file
        else {
            //you can display an error toast
            Toast.makeText(
                    getApplicationContext(),
                    "Upload Uncessful",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

}