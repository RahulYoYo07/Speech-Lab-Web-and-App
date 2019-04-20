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

public class AllDiscussionRooms extends AppCompatActivity {
    public List<HashMap> CourseUser;
    public static final String EXTRA_MESSAGE = "com.example.iitg_speech_lab.MESSAGE";
    public List<String> lst=new ArrayList<String>();
    public List<String> lst2=new ArrayList<String>();
    public List<String> lst3=new ArrayList<String>();
    public List<String> lst4=new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_discussion_rooms);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        final String username="gulat170123030";

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Users").document(username);
        final AllDiscussionRooms help=this;

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//
                        CourseUser= (List<HashMap>)document.get("CourseList");
//
                        for (int i=0; i<CourseUser.size(); i++){

//                            String doc=(String) CourseUser.get(i).get("CourseID");
                            String doc=((DocumentReference)CourseUser.get(i).get("CourseID")).getId();
                            lst.add(doc);
                        }

                        ListView lv = (ListView) findViewById(R.id.DiscussionRoomListView);
//                        String items[] = {"a","b","c"};
                        final String[] items = new String[lst.size()];

                        // ArrayList to Array Conversion
                        for (int i =0; i < lst.size(); i++)
                            items[i] = lst.get(i);

                        for(int i=0;i<lst.size();i++) {
                            final String courseInfo=lst.get(i);
                            System.out.println(courseInfo);
                            CollectionReference assign=db.collection("Courses").document(courseInfo).collection("Assignments");
                            assign.get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    final String assignmentID=document.getId();
                                                    System.out.println(assignmentID);
                                                    final DocumentReference userRef = db.collection("Users").document(username);
                                                    CollectionReference grpsRef = db.collection("Courses").document(courseInfo).collection("Assignments").document(assignmentID).collection("Groups");
                                                    grpsRef.get()
                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        for (QueryDocumentSnapshot grp : task.getResult()) {
                                                                            ArrayList<Map<String, Object>> mp = (ArrayList<Map<String, Object>>) grp.get("StudentList");
                                                                            for (Map<String, Object> x : mp) {
                                                                                if (((DocumentReference) x.get("StudentID")).equals(userRef)) {
                                                                                    System.out.println(x.get("StudentID"));
                                                                                    String groupID = grp.getId();
                                                                                    lst2.add(courseInfo);
                                                                                    lst3.add(assignmentID);
                                                                                    lst4.add(groupID);
                                                                                    ListView lv2 = (ListView) findViewById(R.id.DiscussionRoomAssigmentListView);

                                                                                    final String[] items2 = new String[lst2.size()];
                                                                                    final String[] items3 = new String[lst3.size()];
                                                                                    final String[] items4 = new String[lst4.size()];
//                        System.out.println("lol"+lst2.size());

                                                                                    // ArrayList to Array Conversion
                                                                                    for (int i =0; i < lst2.size(); i++){
                                                                                        items2[i] = lst2.get(i);
                                                                                        items3[i]=lst3.get(i);
                                                                                        items4[i]=lst4.get(i);

                                                                                    }
                                                                                        System.out.println(items2[0]);
                                                                                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(help,android.R.layout.simple_list_item_1,items4);
                                                                                    lv2.setAdapter(adapter2);
                                                                                    lv2.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                                                                        @Override
                                                                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
//                                                                                            System.out.println("la");
                                                                                            Intent intent = new Intent(help, Discussion_Room.class);
                                                                                            intent.putExtra(EXTRA_MESSAGE, items2[position]);
                                                                                            intent.putExtra("assignmentID",items3[position]);
                                                                                            intent.putExtra("groupID",items4[position]);
                                                                                            startActivity(intent);

                                                                                        }

                                                                                    });


                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            });

                                                }
                                            } else {
                                            }
                                        }
                                    });

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(help,android.R.layout.simple_list_item_1,items);
                        lv.setAdapter(adapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                                Intent intent = new Intent(help, Discussion_Room.class);
                                intent.putExtra(EXTRA_MESSAGE, items[position]);
                                startActivity(intent);

                            }

                        });


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
