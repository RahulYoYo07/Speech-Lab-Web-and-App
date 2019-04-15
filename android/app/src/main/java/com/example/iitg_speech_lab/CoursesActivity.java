package com.example.iitg_speech_lab;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iitg_speech_lab.Classes.CoursesMyData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;


public class CoursesActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<CoursesDataModel> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    public TaskCompletionSource<Integer> task1;
    public Task task2;
    private Task<Void> allTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String username = "pradip";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        myOnClickListener = new MyOnClickListener(this);

        recyclerView = ( RecyclerView ) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        task1 = new TaskCompletionSource<>();
        task2 = task1.getTask();
        Log.d("yo",username);
        CoursesMyData.loadData(username,task1);

        allTask = Tasks.whenAll(task2);

        allTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                data = new ArrayList<CoursesDataModel>();
                Log.d("yo",Integer.toString(CoursesMyData.coursesIDList.size()));
                for (int i = 0; i < CoursesMyData.coursesIDList.size(); i++) {
                    data.add(new CoursesDataModel(
                            CoursesMyData.coursesIDList.get(i),
                            CoursesMyData.coursesNameList.get(i),
                            CoursesMyData.IDList.get(i)
                    ));
                }

                removedItems = new ArrayList<Integer>();

                adapter = new CustomAdapter(data);
                recyclerView.setAdapter(adapter);
            }
        });


    }


    private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            removeItem(v);
        }

        private void removeItem(View v) {
            int selectedItemPosition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
            TextView textViewName
                    = viewHolder.itemView.findViewById(R.id.textViewName);
            String selectedName = ( String ) textViewName.getText();
            int selectedItemId = -1;

            for (int i = 0; i < CoursesMyData.coursesIDList.size(); i++) {
                if (selectedName.equals(CoursesMyData.coursesIDList.get(i))) {
                    selectedItemId = CoursesMyData.IDList.get(i);
                }
            }
            removedItems.add(selectedItemId);
            data.remove(selectedItemPosition);
            adapter.notifyItemRemoved(selectedItemPosition);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_courses, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.add_item) {
            //check if any items to add
            if (removedItems.size() != 0) {
                addRemovedItemToList();
            } else {
                Toast.makeText(this, "Nothing to add", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    private void addRemovedItemToList() {
        int addItemAtListPosition = 3;
        data.add(addItemAtListPosition, new CoursesDataModel(
                CoursesMyData.coursesIDList.get(removedItems.get(0)),
                CoursesMyData.coursesNameList.get(removedItems.get(0)),
                CoursesMyData.IDList.get(removedItems.get(0))
        ));
        adapter.notifyItemInserted(addItemAtListPosition);
        removedItems.remove(0);
    }
}
