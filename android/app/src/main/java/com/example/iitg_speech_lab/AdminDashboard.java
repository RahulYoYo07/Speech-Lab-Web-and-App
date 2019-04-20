package com.example.iitg_speech_lab;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.PublicClientApplication;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = AfterLoginHomePage.class.getSimpleName();
    private PublicClientApplication sampleApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Administrator");
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
                                ViewPager viewPager = findViewById(R.id.AdminViewPager);
                                SliderImageAdapter adapter = new SliderImageAdapter(getApplicationContext(),imageUrls);
                                viewPager.setAdapter(adapter);
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

        if (id == R.id.AdminAddFaq) {
            Intent intent = new Intent(AdminDashboard.this, AddFaq.class);
            startActivity(intent);
        } else if (id == R.id.AdminEditAboutUs) {
            Intent intent = new Intent(AdminDashboard.this, EditAboutUs.class);
            startActivity(intent);

        } else if (id == R.id.AdminEditContactUs) {
            Intent intent = new Intent(AdminDashboard.this, EditContactUs.class);
            startActivity(intent);
        } else if (id == R.id.AdminEditDeleteFaq){
            Intent intent = new Intent(AdminDashboard.this, EditDeleteFaq.class);
            startActivity(intent);
        } else if (id == R.id.AdminHome){
            Intent intent = new Intent(AdminDashboard.this, AfterLoginHomePage.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);
        } else if (id == R.id.AdminLogOut){
            onSignOutClicked();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        Intent intent = new Intent(AdminDashboard.this, Master.class);
        startActivity(intent);
    }
}
