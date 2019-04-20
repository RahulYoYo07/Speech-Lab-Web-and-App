package com.example.iitg_speech_lab;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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


public class ViewAssignment extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String assignmentID;

    public static String courseInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        courseInfo = getIntent().getStringExtra("courseInfo");
        assignmentID = getIntent().getStringExtra("assignID");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assignment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(assignmentID);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_assignment,
                    new FragmentGroups()).commit();
            navigationView.setCheckedItem(R.id.nav_groups);
        }
        Toast.makeText(ViewAssignment.this, courseInfo + " " + assignmentID , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_assignment, menu);
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

        if (id == R.id.nav_groups) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_assignment,
                    new FragmentGroups()).commit();
        } else if (id == R.id.nav_submissions) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_assignment,
                    new FragmentSubmissions()).commit();

        } else if (id == R.id.nav_grading) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_assignment,
                    new FragmentGrading()).commit();
        } else if (id == R.id.nav_update_assignment) {
            Intent intent = new Intent(ViewAssignment.this, UpdAssignment.class);
            intent.putExtra("cInfo", ViewAssignment.courseInfo);
            intent.putExtra("aid", ViewAssignment.assignmentID);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
