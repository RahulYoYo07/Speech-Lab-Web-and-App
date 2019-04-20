package com.example.iitg_speech_lab;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.microsoft.identity.client.*;
import java.util.List;
import com.microsoft.identity.client.exception.*;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Master<sampleApp> extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public boolean internetIsConnected(){
        try{
            String cmd="ping -c 1 google.com";
            return (Runtime.getRuntime().exec(cmd).waitFor()==0);
        }
        catch (Exception e){
            return false;
        }
    }
    /* Azure AD v2 Configs */
    final static String SCOPES [] = {"https://graph.microsoft.com/User.Read+profile+openid+offline_access"};
    final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me";
    static String isfirst="0";
    static String roll;
    static String department="";
    static String Username="";
    private static final long START_TIME_IN_MILLIS = 600000;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    static int check=0;
    private ProgressBar spinner;
    /* UI & Debugging Variables */
    private static final String TAG = Master.class.getSimpleName();
    /* Azure AD Variables */
    private PublicClientApplication sampleApp;
    private AuthenticationResult authResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        boolean bo=internetIsConnected();
        if(bo==false)
        {
            AlertDialog.Builder dialog=new AlertDialog.Builder(this);
            dialog.setMessage("No Internet Connection");
            dialog.setTitle("Error Message");
            dialog.setPositiveButton("Close",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            finish();
                            System.exit(0);
                        }
                    });
            AlertDialog alertDialog=dialog.create();
            alertDialog.show();

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SPEECH LAB IITG");
        spinner = (ProgressBar) findViewById(R.id.progressBar2);
        setSupportActionBar(toolbar);
        //Code for Sliding Images

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
                                ViewPager viewPager = findViewById(R.id.MasterViewPager);
                                SliderImageAdapter adapter = new SliderImageAdapter(getApplicationContext(),imageUrls);
                                viewPager.setAdapter(adapter);
                                spinner.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

                sampleApp.acquireTokenSilentAsync(SCOPES, accounts.get(0), getAuthSilentCallback());
            } else {
                /* We have no account or >1 account */
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, "Account at this position does not exist: " + e.toString());
        }
        startTimer();
    }

//
    // Core Identity methods used by MSAL
    // ==================================
    // onActivityResult() - handles redirect from System browser
    // onCallGraphClicked() - attempts to get tokens for graph, if it succeeds calls graph & updates UI
    // onSignOutClicked() - Signs account out of the app & updates UI
    // callGraphAPI() - called on successful token acquisition which makes an HTTP request to graph
    //

    /* Handles the redirect from the System Browser */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        sampleApp.handleInteractiveRequestRedirect(requestCode, resultCode, data);
    }

    /* Use MSAL to acquireToken for the end-user
     * Callback will call Graph api w/ access token & update UI
     */
    private void onCallGraphClicked() {
        spinner.setVisibility(View.VISIBLE);
        sampleApp.acquireToken(getActivity(), SCOPES, getAuthInteractiveCallback());
    }

    /* Use Volley to make an HTTP request to the /me endpoint from MS Graph using an access token */
    private void callGraphAPI() {
        Log.d(TAG, "Starting volley request to graph");

        /* Make sure we have a token to send to graph */
        if (authResult.getAccessToken() == null) {return;}

        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("key", "value");
        } catch (Exception e) {
            Log.d(TAG, "Failed to put parameters: " + e.toString());
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MSGRAPH_URL,
                parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                /* Successfully called graph, process data and send to UI */
                Log.d(TAG, "Response: " + response.toString());

                updateGraphUI(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authResult.getAccessToken());
                return headers;
            }
        };

        Log.d(TAG, "Adding HTTP GET to Queue, Request: " + request.toString());

        request.setRetryPolicy(new DefaultRetryPolicy(
                300,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
    private DatabaseReference mdatabase;
    //
    // Helper methods manage UI updates
    // ================================
    // updateGraphUI() - Sets graph response in UI
    // updateSuccessUI() - Updates UI when token acquisition succeeds
    // updateSignedOutUI() - Updates UI when app sign out succeeds
    //

    /* Sets the graph response */
    private void updateGraphUI(final JSONObject graphResponse) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String username = graphResponse.optString("mail").replace("@iitg.ac.in", "");
        final Map<String, Object> newUser = new HashMap<>();
        final Map<String, Object> Url = new HashMap<>();

        List<String> branchlist = new ArrayList<String>();
        branchlist.add("Computer Science and Engineering");
        branchlist.add("Electronics and Communication Engineering");
        branchlist.add("Mechanical Engineering");
        branchlist.add("Chemical Engineering");
        branchlist.add("Department of Design");
        branchlist.add("BSBE");
        branchlist.add("Civil Engineering");
        branchlist.add("Electronics and Electrical Engineering");
        branchlist.add("Chemical Science and Technology");
        branchlist.add("Mathematics");
        branchlist.add("Engineering Physics");
        branchlist.add("Humanities and Social Sciences");

        String des = graphResponse.optString("jobTitle");
        newUser.put("About", "");
        newUser.put("Contact", "");
        roll= graphResponse.optString("surname").substring(4, 6);
        Log.d("asdfg", roll);
        if(roll!=null)
        {
            Integer tt=null;
            for (int i=0;i<8;i++)
            {
                if (roll.equals(Integer.toString(i)+"1"))
                {
                    Log.d("asdfgddd", roll);
                    department = branchlist.get(i);
                    Log.d("asdfgddddddd", department);
                    break;
                }
            }

            if(roll.equals(21))
            {
                department = branchlist.get(10);
            }
            else if(roll.equals(22))
            {
                department = branchlist.get(8);
            }
            else if(roll.equals(23))
            {
                department = branchlist.get(9);
            }
            else if(roll.equals(41))
            {
                department = branchlist.get(11);
            }
        }

        newUser.put("Department", department);
        final String designation ;
        if(des.equalsIgnoreCase("btech") || des.equalsIgnoreCase("mtech") ||
                des.equalsIgnoreCase("bdes") || des.equalsIgnoreCase("mdes")
                || des.equalsIgnoreCase("phd"))
        {
            designation = "Student";
        }
        else
            designation="Faculty";

        newUser.put("Designation", designation);
        newUser.put("Email", graphResponse.optString("mail"));
        newUser.put("FullName", graphResponse.optString("displayName").toUpperCase());
        newUser.put("ProfilePic", "");
        if (designation.equalsIgnoreCase("Student"))
            newUser.put("Program", des);
        else
            newUser.put("CollegeDesignation", des);
        newUser.put("RollNumber", graphResponse.optString("surname"));
        Url.put("Github", "");
        Url.put("Homepage", "");
        Url.put("Linkedin", "");
        newUser.put("URL", Url);
        newUser.put("Username", username);
        DocumentReference docref= db.collection("Users").document(username);
        docref.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot user = task.getResult();
                            if(user.exists()){
                                Log.d(TAG, "Already Added");
                                updateSuccessUI(graphResponse);
                            }
                            else{
                                db.collection("Users").document(username).set(newUser);
                                isfirst="1";
                                spinner.setVisibility(View.INVISIBLE);
                                updateSuccessUI(graphResponse);
                            }
                        }
                    }
                });
    }

    /* Set the UI for successful token acquisition data */
    private void updateSuccessUI(JSONObject graphResponse) {
        finish();
        Intent intent = new Intent(Master.this, ProfileProjectDashboard.class);
        intent.putExtra("username", graphResponse.optString("mail").replace("@iitg.ac.in", ""));
        intent.putExtra("isfirst", isfirst);
        startActivity(intent);
    }

    //
    // App callbacks for MSAL
    // ======================
    // getActivity() - returns activity so we can acquireToken within a callback
    // getAuthSilentCallback() - callback defined to handle acquireTokenSilent() case
    // getAuthInteractiveCallback() - callback defined to handle acquireToken() case
    //

    public Activity getActivity() {
        return this;
    }

    /* Callback used in for silent acquireToken calls.
     * Looks if tokens are in the cache (refreshes if necessary and if we don't forceRefresh)
     * else errors that we need to do an interactive request.
     */
    private AuthenticationCallback getAuthSilentCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
                /* Successfully got a token, call graph now */
                Log.d(TAG, "Successfully authenticated");

                /* Store the authResult */
                authResult = authenticationResult;
                /* call graph */
                callGraphAPI();
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                spinner.setVisibility(View.INVISIBLE);
                Log.d(TAG, "Authentication failed: " + exception.toString());

                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                } else if (exception instanceof MsalUiRequiredException) {
                    /* Tokens expired or no session, retry with interactive */
                }
            }

            @Override
            public void onCancel() {
                spinner.setVisibility(View.INVISIBLE);
                /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }

    /* Callback used for interactive request.  If succeeds we use the access
     * token to call the Microsoft Graph. Does not check cache
     */
    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
                /* Successfully got a token, call graph now */
                Log.d(TAG, "Successfully authenticated");
                Log.d(TAG, "ID Token: " + authenticationResult.getIdToken());

                /* Store the auth result */
                authResult = authenticationResult;

                /* call graph */
                callGraphAPI();
            }

            @Override
            public void onError(MsalException exception) {
                spinner.setVisibility(View.INVISIBLE);
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());

                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                }
            }

            @Override
            public void onCancel() {
                spinner.setVisibility(View.INVISIBLE);
                /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }

    int backButtonCount = 0;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(backButtonCount >= 1)
            {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else
            {
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

    public void temp(View view){
        Intent intent = new Intent(Master.this, AdminDashboard.class);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.People) {
            Intent intent = new Intent(Master.this, ViewPeople.class);
            startActivity(intent);
        } else if (id == R.id.FAQ) {
            Intent intent = new Intent(Master.this, ViewFaqListView.class);
            startActivity(intent);
        } else if (id == R.id.ContactUs){
            Intent intent = new Intent(Master.this, ContactUs.class);
            startActivity(intent);
        } else if(id == R.id.AboutUs) {
            Intent intent = new Intent(Master.this, AboutUs.class);
            startActivity(intent);
        } else if(id == R.id.Login) {
            onCallGraphClicked();
        } else if(id == R.id.Projects) {
            Intent intent = new Intent(Master.this, ProjectsActivity.class);
            intent.putExtra("username", "");
            startActivity(intent);
        } else if(id == R.id.NoticeBoard) {
            Intent intent = new Intent(Master.this, PublicNoticeBoardMaster.class);
            intent.putExtra("username", "");
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
