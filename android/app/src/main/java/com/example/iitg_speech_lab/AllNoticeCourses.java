package com.example.iitg_speech_lab;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllNoticeCourses extends AppCompatActivity {
    public List<HashMap> CourseUser=new ArrayList<>();
    public List CourseUser2;
    public List CourseUser3;
    public List<HashMap> CourseUser1;
    private ProgressBar spinner;
    public static final String EXTRA_MESSAGE = "com.example.iitg_speech_lab.MESSAGE";
    public List<String> lst=new ArrayList<String>();
    public List<String> lst2=new ArrayList<String>();
    public List<String> lst3=new ArrayList<String>();
    public List<String> lst4=new ArrayList<String>();
    public List<String> lst5=new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notice_courses);

        spinner = (ProgressBar) findViewById(R.id.progressBar9);

        spinner.setVisibility(View.VISIBLE);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        final String username=getIntent().getStringExtra("username");

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Users").document(username);
        final AllNoticeCourses help=this;

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        spinner.setVisibility(View.GONE);
//
                        CourseUser1= (List<HashMap>)document.get("CourseList");
                        CourseUser2= (List)document.get("ProfCourseList");
                        CourseUser3= (List)document.get("CoursesListAsTA");


                        if(CourseUser1!=null)
                            CourseUser.addAll(CourseUser1);
//                        if(CourseUser2!=null)
//                            CourseUser.addAll(CourseUser2);
//                        if(CourseUser3!=null)
//                            CourseUser.addAll(CourseUser3);
//                        if(CourseUser1==null && CourseUser2 == null && CourseUser3 == null )
//                            return;

//
                        for (int i=0; i<CourseUser.size(); i++){

//                            String doc=(String) CourseUser.get(i).get("CourseID");
                            String doc=((DocumentReference)CourseUser.get(i).get("CourseID")).getId();
                            lst.add(doc);
                        }
                        if(CourseUser3!=null)
                        {
                            for(int i=0;i<CourseUser3.size();i++){
                                String doc=((DocumentReference)CourseUser3.get(i)).getId();
                                if(lst.contains(doc))
                                    continue;
                                lst.add(doc);
                            }
                        }
                        if(CourseUser2!=null)
                        {
                            for(int i=0;i<CourseUser2.size();i++){
                                String doc=((DocumentReference)CourseUser2.get(i)).getId();
                                if(lst.contains(doc))
                                    continue;
                                lst.add(doc);
                            }
                        }
                        ListView lv = (ListView) findViewById(R.id.NoticeRoomListView);
//                        String items[] = {"a","b","c"};
                        final String[] items = new String[lst.size()];

                        // ArrayList to Array Conversion
                        for (int i =0; i < lst.size(); i++)
                            items[i] = lst.get(i);


                        if(lst.size()>0){
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(help,android.R.layout.simple_list_item_1,items);
                            lv.setAdapter(adapter);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                                    Intent intent = new Intent(help, Discussion_Notice_Board.class);
                                    intent.putExtra(EXTRA_MESSAGE, items[position]);
                                    intent.putExtra("username",username);
                                    startActivity(intent);

                                }

                            });}


                    } else {

                        return;
                    }
                } else {
                    return;
                }
            }
        });

//                        String items[] = {"a","b","c"};








    }
}
