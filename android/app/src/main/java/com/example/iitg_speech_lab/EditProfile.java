package com.example.iitg_speech_lab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    static String GetUsername ;
    String durl="";
    private ProgressBar spinner;
    private Uri filePath;
    private static final int PICK_FILE_REQUEST = 234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        GetUsername = getIntent().getStringExtra("username");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Profile");
        final TextView Name = findViewById(R.id.EditProfileDisplayNameDetail);
        final TextView Username = findViewById(R.id.EditProfileDisplayUsernameDetail);
        final EditText Program = findViewById(R.id.EditProfileDisplayProgramDetail);
        final EditText Dept = findViewById(R.id.EditProfileDisplayDeptDetail);
        final EditText Contact = findViewById(R.id.EditProfileDisplayContactDetail);
        final EditText Email = findViewById(R.id.EditProfileDisplayEmailDetail);
        final EditText About = findViewById(R.id.EditProfileDisplayAboutDetail);
        final EditText Website = findViewById(R.id.EditProfileDisplayWebsiteDetail);
        final EditText Github = findViewById(R.id.EditProfileDisplayGithubDetail);
        final EditText LinkedIn = findViewById(R.id.EditProfileDisplayLinkedInDetail);
        final TextView ProgramDecide = findViewById(R.id.EditProfileDisplayProgram);
        final EditText Room = findViewById(R.id.EditProfileDisplayRoomDetail);
        final TextView RoomDecide = findViewById(R.id.EditProfileDisplayRoom);
        //Log.d("tushar",Name.getText().toString());
        spinner = findViewById(R.id.progress_discussion);
        spinner.setVisibility(View.VISIBLE);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference userRef = db.collection("Users").document(GetUsername);
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot user = task.getResult();
                            spinner.setVisibility(View.GONE);
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
                                durl = map.get("ProfilePic");
                                Dept.setEnabled(false);
                                try{
                                    if (user.getString("Designation").equals("Student")) {
                                        Program.setText(user.getString("Program"));
                                        Program.setEnabled(false);
                                        Room.setText(user.getString("RollNumber"));
                                        RoomDecide.setText("Roll Number");
                                        Room.setEnabled(false);
                                    } else {
                                        Program.setText(user.getString("CollegeDesignation"));
                                        ProgramDecide.setText("Designation: ");
                                        Room.setText(user.getString("RoomNumber"));
                                    }
                                    if (user.getString("ProfilePic").length() > 0) {
                                        CircleImageView img = findViewById(R.id.EditProfileprofile);
                                        String url = user.getString("ProfilePic");
                                        Picasso.get().load(url).into(img);
                                    }}
                                catch (Exception e){
                                    System.out.println(e.getMessage());
                                }
                            }
                        }
                    }
                });
    }

    public void EditProfileDetails(View view){
        final TextView Name = findViewById(R.id.EditProfileDisplayNameDetail);
        final TextView Username = findViewById(R.id.EditProfileDisplayUsernameDetail);
        final EditText Program = findViewById(R.id.EditProfileDisplayProgramDetail);
        final EditText Dept = findViewById(R.id.EditProfileDisplayDeptDetail);
        final EditText Contact = findViewById(R.id.EditProfileDisplayContactDetail);
        final EditText Email = findViewById(R.id.EditProfileDisplayEmailDetail);
        final EditText About = findViewById(R.id.EditProfileDisplayAboutDetail);
        final EditText Website = findViewById(R.id.EditProfileDisplayWebsiteDetail);
        final EditText Github = findViewById(R.id.EditProfileDisplayGithubDetail);
        final EditText LinkedIn = findViewById(R.id.EditProfileDisplayLinkedInDetail);
        final TextView ProgramDecide = findViewById(R.id.EditProfileDisplayProgram);
        final EditText Room = findViewById(R.id.EditProfileDisplayRoomDetail);
        final TextView RoomDecide = findViewById(R.id.EditProfileDisplayRoom);
        Map <String,String> url = new HashMap<>();
        url.put("Homepage",Website.getText().toString());
        url.put("Github",Github.getText().toString());
        url.put("Linkedin",LinkedIn.getText().toString());
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference userRef = db.collection("Users").document(GetUsername);
        userRef.update(
                "FullName",Name.getText().toString(),
                "Username",Username.getText().toString(),
                "Department",Dept.getText().toString(),
                "Contact",Contact.getText().toString(),
                "Email",Email.getText().toString(),
                "About",About.getText().toString(),
                "URL",url,
                "ProfilePic",durl
        );

        final DocumentReference usertemp = db.collection("Users").document(GetUsername);
        usertemp.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot user = task.getResult();
                            if (user.exists()) {
                                Toast.makeText(getApplicationContext(),"Updated Details",Toast.LENGTH_LONG).show();
                                if (user.getString("Designation").equals("Student")) {
                                    userRef.update(
                                            "Program",Program.getText().toString(),
                                            "RollNumber",Room.getText().toString()
                                    );

                                } else {
                                    userRef.update(
                                            "RoomNumber",Room.getText().toString(),
                                            "CollegeDesignation",Program.getText().toString()
                                    );
                                }
                            }
                        }
                    }
                });
    }



    //method to show file chooser
    public void showFileChooser(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Button buttonChoose = findViewById(R.id.btnUpload);
            filePath = data.getData();
//            buttonChoose.setText(filePath.toString());
            CircleImageView img = findViewById(R.id.EditProfileprofile);
            Picasso.get().load(filePath).into(img);
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
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            // Create a storage reference from our app
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference storageRef = storage.getReference();

            // File or Blob
//            file = Uri.fromFile(new File("path/to/mountains.jpg"));

            final StorageReference ref = storageRef.child("ProfileImages/"+GetUsername+"/"+filePath.getLastPathSegment());
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