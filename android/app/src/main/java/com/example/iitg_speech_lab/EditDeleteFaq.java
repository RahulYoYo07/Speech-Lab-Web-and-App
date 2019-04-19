package com.example.iitg_speech_lab;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditDeleteFaq extends AppCompatActivity {
    final ArrayList<String> questionList = new ArrayList<>();
    final ArrayList<String> answerList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_faq);
        final ListView listView = (ListView) findViewById(R.id.EditDeleteFaqListView);
        registerForContextMenu(listView);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Homepage").document("faq");
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot user = task.getResult();
                            if (user.exists()) {
                                final ArrayList<Map<String, String>> ans = (ArrayList<Map<String, String>>) user.get("qa");
                                int i = 0;
                                //Log.d("tushar",Integer.toString(ans.size()));
                                for (Map<String, String> val : ans) {
                                    i = i + 1;
                                    questionList.add("Question: " + val.get("q").toString());
                                    answerList.add("Answer: " + val.get("a").toString());
                                    //Log.d("tushar",Integer.toString(i));
                                }
                                if (ans.size() == i) {
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.EditDeleteFaqListView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            //menu.setHeaderTitle(Countries[info.position]);
            String[] menuItems = getResources().getStringArray(R.array.EditDeleteContextList);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        //String[] menuItems = getResources().getStringArray(R.array.EditDeleteContextList);
        //String menuItemName = menuItems[menuItemIndex];
        //Toast.makeText(getApplicationContext(),questionList.get(menuItemIndex),Toast.LENGTH_LONG).show();
        if(menuItemIndex == 0){
            finish();
            Intent intent = new Intent(EditDeleteFaq.this, EditFaq.class);
            intent.putExtra("position",Integer.toString(info.position));
            startActivity(intent);
        }
        else if(menuItemIndex == 1){
            alertDialog(info.position);
        }
        return true;
    }

    class CustomAdapter extends BaseAdapter {

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
            convertView = getLayoutInflater().inflate(R.layout.faq_layout, null);

            TextView answer = (TextView) convertView.findViewById(R.id.viewFaqAnswer);
            TextView question = (TextView) convertView.findViewById(R.id.viewFaqQuestion);

            answer.setText(answerList.get(position));
            question.setText(questionList.get(position));
            return convertView;
        }
    }

    private void alertDialog(final Integer position) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Are you sure?");
        dialog.setTitle("Delete FAQ");
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        final FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference userRef = db.collection("Homepage").document("faq");
                        userRef.get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot user = task.getResult();
                                            if(user.exists()){
                                                final ArrayList<Map<String,String>> arr = (ArrayList<Map<String,String>>) user.get("qa");
                                                ArrayList<Map<String,String>> arr1 = new ArrayList<Map<String,String>>();
                                                int i=0;
                                                Integer z= position;
                                                for(i=0;i<arr.size();i++){
                                                    if(i!=z) arr1.add(arr.get(i));
                                                    else {
                                                    }

                                                    if(i == arr.size()-1){
                                                        Map <String,Object> temp = new HashMap<String, Object>();
                                                        temp.put("qa",arr1);
                                                        DocumentReference ins = db.collection("Homepage").document("faq");
                                                        ins.set(temp);
                                                        Toast.makeText(EditDeleteFaq.this,"FAQ delete Successfully",Toast.LENGTH_SHORT).show();
                                                        finish();
                                                        startActivity(getIntent());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                });

                    }
                });
        dialog.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }
}
