package com.example.iitg_speech_lab;

import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

//-------------------------------------------------------------------------------------------------
//import com.google.android.gms.common.ConnectionResult;
//import com.google.api.client.extensions.android.http.AndroidHttp;
//import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.util.ExponentialBackOff;
//
//import com.google.api.services.calendar.CalendarScopes;
//
//import android.accounts.AccountManager;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Typeface;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.text.method.ScrollingMovementMethod;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import java.util.Arrays;
//import java.util.List;

public class ViewCalendar extends AppCompatActivity {

    public TaskCompletionSource<Integer> task1;
    public Task task2;
    public Credential credentials;


    private static final String APPLICATION_NAME = "IITG-Speech-Lab";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */

    public class myAsyncTask extends AsyncTask<Void, Void, Void> {
//        String urlLink;
        GoogleAuthorizationCodeFlow flow;
        LocalServerReceiver receiver;

        public myAsyncTask(GoogleAuthorizationCodeFlow f, LocalServerReceiver r){
            flow = f;
            receiver = r;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                credentials = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
            }
            catch (IOException e){
                System.out.println("Credentials receiving failure");
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                System.out.println(credentials);
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.println("Credentials printing failure");
                e.printStackTrace();
            }
        }
    }

    public void getCredentials(final NetHttpTransport HTTP_TRANSPORT) {
        try{
            // Load client secrets.
            InputStream in = ViewCalendar.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
            if (in == null) {
                throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
            }
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

            // Build flow and trigger user authorization request.
            java.io.File dataDirectory = new java.io.File(getApplicationContext().getFilesDir(), TOKENS_DIRECTORY_PATH);
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(new FileDataStoreFactory(dataDirectory))
                    .setAccessType("offline")
                    .build();
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
            credentials = null;
            myAsyncTask task1= myAsyncTask(flow, receiver);
            task1.execute();
//            credentials = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

//            task1 = new TaskCompletionSource<>();
//            task2 = task1.getTask();
//
//            while (credentials != null){
//                task1.setResult(1);
//            }
//
//            Task<Void> allTask = Tasks.whenAll(task2);
//            System.out.println(credentials);
//
//            allTask.addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    return;
//                }
//            });

//            return credentials;
        }
        catch(IOException e){
            System.out.println("getCredentials failed");
            System.out.println(e.getMessage());
            return;
        }

    }

    public Calendar GetService(String... args) {
        // Build a new authorized API client service.
//        try{
            final NetHttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
            getCredentials(HTTP_TRANSPORT);
            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
//        }
//        catch(IOException e ){
//            System.out.println("GetService IOException failed");
//            System.out.println(e.getMessage());
//        }
//        catch(GeneralSecurityException g){
//            System.out.println("getService GSE failed");
//            System.out.println(g.getMessage());
//        }
        return service;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_calendar);
//        credential = GoogleAccountCredential.usingOAuth2(this, Collections.singleton(TasksScopes.TASKS));
//        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
//        credential.setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));
//        // Tasks client
//        service = new com.google.api.services.tasks.Tasks.Builder(httpTransport, JsonFactory, credential).setApplicationName("Google-TasksAndroidSample/1.0").build();
        Calendar service = GetService();
    }
}
