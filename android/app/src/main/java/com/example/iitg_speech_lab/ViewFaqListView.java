package com.example.iitg_speech_lab;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class ViewFaqListView extends AppCompatActivity {
    final ArrayList<String> questionList = new ArrayList<>();
    final ArrayList<String> answerList = new ArrayList<>();
    private ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_faq_list_view);
        final ListView listView = findViewById(R.id.ViewFaqListView);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("View FAQ");
        spinner = findViewById(R.id.progress_discussion);
        spinner.setVisibility(View.VISIBLE);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Homepage").document("faq");
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot user = task.getResult();
                            spinner.setVisibility(View.GONE);
                            if(user.exists()){
                                final ArrayList<Map<String,String>> ans = (ArrayList<Map<String,String>>) user.get("qa");
                                int i=0;
                                //Log.d("tushar",Integer.toString(ans.size()));
                                for (Map<String,String> val : ans){
                                    i = i+1;
                                    questionList.add("Question: "+ val.get("q"));
                                    answerList.add("Answer: "+ val.get("a"));
                                    //Log.d("tushar",Integer.toString(i));
                                }
                                if(ans.size() == i){
                                    //Log.d("tushar",Integer.toString(i));
                                    CustomAdapter customAdapter = new CustomAdapter();
                                    //Log.d("tushar",Integer.toString(i));
                                    listView.setAdapter(customAdapter);
                                    //Log.d("tushar",Integer.toString(i));
                                }
                            }
                        }
                    }
                });

    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return questionList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.faq_layout,null);

            TextView answer = convertView.findViewById(R.id.viewFaqAnswer);
            TextView question = convertView.findViewById(R.id.viewFaqQuestion);

            answer.setText(answerList.get(position));
            question.setText(questionList.get(position));
            return convertView;
        }
    }
}
