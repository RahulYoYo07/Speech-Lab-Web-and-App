package com.example.iitg_speech_lab;

import android.content.Intent;
import android.os.*;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.PublicClientApplication;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AfterLoginHomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /* UI & Debugging Variables */
    private static final String TAG = AfterLoginHomePage.class.getSimpleName();
    private static final long START_TIME_IN_MILLIS = 600000;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    static int check=0;
    static  String GetUsername;
    static String isfirst;
    private ProgressBar spinner;
    /* Azure AD Variables */
    private PublicClientApplication sampleApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login_home_page);
        GetUsername=getIntent().getStringExtra("username");
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Welcome, " + GetUsername);
        setSupportActionBar(toolbar);
        spinner = findViewById(R.id.progressBar4);
        isfirst=getIntent().getStringExtra("isfirst");
        spinner.setVisibility(View.VISIBLE);
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
                                ViewPager viewPager = findViewById(R.id.AfterLoginViewPager);
                                SliderImageAdapter adapter = new SliderImageAdapter(getApplicationContext(),imageUrls);
                                viewPager.setAdapter(adapter);
                                spinner.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });
        //Code for Sliding Images ends
        /* Configure your sample app and save state for this activity */
        sampleApp = null;
        if (sampleApp == null) {
            sampleApp = new PublicClientApplication(this.getApplicationContext(),
                    R.raw.auth_config);
        }

        /* Attempt to get a user and acquireTokenSilent
         * If this fails we do an interactive request
         */
        List<IAccount> accounts = null;

        try {
            accounts = sampleApp.getAccounts();

            if (accounts != null && accounts.size() == 1) {
                /* We have 1 account */
            } else {
                /* We have no account or >1 account */
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, "Account at this position does not exist: " + e.toString());
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        startTimer();
    }

    private void onSignOutClicked() {

        /* Attempt to get a account and remove their cookies from cache */
        List<IAccount> accounts = null;

        try {
            accounts = sampleApp.getAccounts();
            if (accounts == null) {
                /* We have no accounts */

            } else if (accounts.size() == 1) {
                /* We have 1 account */
                /* Remove from token cache */
                sampleApp.removeAccount(accounts.get(0));
                updateSignedOutUI();
            }
            else {
                /* We have multiple accounts */
                for (int i = 0; i < accounts.size(); i++) {
                    sampleApp.removeAccount(accounts.get(i));
                }
            }

            Toast.makeText(getBaseContext(), "Signed Out!", Toast.LENGTH_SHORT)
                    .show();

        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, "User at this position does not exist: " + e.toString());
        }
    }

    /* Set the UI for signed out account */
    private void updateSignedOutUI() {
        Intent intent = new Intent(AfterLoginHomePage.this, Master.class);
        startActivity(intent);
    }
    int backButtonCount = 0;
    @Override
    public void onBackPressed()
    {DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (backButtonCount >= 1) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
                backButtonCount++;
            }
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

        if (id == R.id.AfterLoginPeople) {
            Intent intent = new Intent(AfterLoginHomePage.this, ViewPeople.class);
            startActivity(intent);
        } else if (id == R.id.AfterLoginFAQ) {
            Intent intent = new Intent(AfterLoginHomePage.this, ViewFaqListView.class);
            startActivity(intent);
        } else if (id == R.id.AfterLoginContactUs){
            Intent intent = new Intent(AfterLoginHomePage.this, ContactUs.class);
            startActivity(intent);
        } else if(id == R.id.AfterLoginAboutUs) {
            Intent intent = new Intent(AfterLoginHomePage.this, AboutUs.class);
            startActivity(intent);
        } else if(id == R.id.AfterLoginProjects) {
            Intent intent = new Intent(AfterLoginHomePage.this, ProjectsActivity.class);
            intent.putExtra("username", "");
            startActivity(intent);
        } else if (id ==R.id.AfterLoginDashboard){
            finish();
            Intent intent = new Intent(AfterLoginHomePage.this, ProfileProjectDashboard.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            intent.putExtra("isfirst", getIntent().getStringExtra("isfirst"));
            startActivity(intent);
        } else if(id == R.id.AfterLoginLogOut){
            onSignOutClicked();
        }else if(id == R.id.AfterLoginNoticeBoard) {
            Intent intent = new Intent(AfterLoginHomePage.this, PublicNoticeBoard.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                if (check>0) {
                    backButtonCount = 0;
                    check = 0;
                }
                else {
                    check++;
                }
            }
            @Override
            public void onFinish() {resetTimer();
            }
        }.start();
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
    }
}

