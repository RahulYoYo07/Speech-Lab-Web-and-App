package com.example.iitg_speech_lab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileProjectDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_project_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");
        setSupportActionBar(toolbar);

        //Code For Sliding Images

        final FirebaseFirestore db1 = FirebaseFirestore.getInstance();
        DocumentReference userRef = db1.collection("Homepage").document("HomeImages");
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot user = task.getResult();
                            if(user.exists()){
                                ArrayList<String> imageUrls = (ArrayList<String>) user.get("Image");
                                //for(int i=0;i<imageUrls.size();i++) Log.d("tushar",imageUrls.get(i));
                                ViewPager viewPager = findViewById(R.id.ProfileProjectDashboardViewPager);
                                SliderImageAdapter adapter = new SliderImageAdapter(getApplicationContext(),imageUrls);
                                viewPager.setAdapter(adapter);
                            }
                        }
                    }
                });
        //Code for Sliding Images ends


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.ProfileDashboardMyProfile) {
            Intent intent = new Intent(ProfileProjectDashboard.this, PrivateProfileDetails.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);
        } else if (id == R.id.ProfileDashboardEditProfile) {
            Intent intent = new Intent(ProfileProjectDashboard.this, EditProfile.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);

        } else if (id == R.id.ProfileDashboardMyProjects) {
            Intent intent = new Intent(ProfileProjectDashboard.this, ProjectsActivity.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);
        } else if (id == R.id.ProfileDashboardAddProjects) {
            Intent intent = new Intent(ProfileProjectDashboard.this, AddProject.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);

        } else if(id == R.id.RedirectCourses){
            final String username = getIntent().getStringExtra("username");
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            final DocumentReference userRef = db.collection("Users").document(username);
            userRef.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot user = task.getResult();
                                if(user.exists()){
                                    if(user.getString("Designation").equals("Student")){
                                        Intent intent = new Intent(ProfileProjectDashboard.this, StudentCoursesActivity.class);
                                        intent.putExtra("username", getIntent().getStringExtra("username"));
                                        startActivity(intent);
                                    }
                                    else{
                                        Intent intent = new Intent(ProfileProjectDashboard.this, CoursesActivity.class);
                                        intent.putExtra("username", getIntent().getStringExtra("username"));
                                        startActivity(intent);
                                    }
                                }
                            }
                        }
                    });
        } else if (id == R.id.ProfileDashboardHome){
            Intent intent = new Intent(ProfileProjectDashboard.this, AfterLoginHomePage.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);
        } else if (id == R.id.ProfileDashboardDiscussion) {
            Intent intent = new Intent(ProfileProjectDashboard.this, AllDiscussionRooms.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
