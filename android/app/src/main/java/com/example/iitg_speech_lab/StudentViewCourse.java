package com.example.iitg_speech_lab;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.iitg_speech_lab.Class.AssignmentsMyData;
import com.example.iitg_speech_lab.Class.StudyMaterialMyData;
import com.example.iitg_speech_lab.Model.AssignmentsDataModel;
import com.example.iitg_speech_lab.Model.StudyMaterialDataModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;

public class StudentViewCourse extends AppCompatActivity {

    public static String courseInfo;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_course);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        courseInfo = getIntent().getStringExtra("courseInfo");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_view_course, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class StudentAssignmentFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static RecyclerView.Adapter adapter;
        private static RecyclerView recyclerView;
        private static ArrayList<AssignmentsDataModel> data;
        static View.OnClickListener myOnClickListener;
        public TaskCompletionSource<Integer> task1;
        public Task task2;
        private Task<Void> allTask;
        View V;
        private static final String ARG_SECTION_NUMBER = "section_number";

        public StudentAssignmentFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static StudentAssignmentFragment newInstance(int sectionNumber, String name) {
            StudentAssignmentFragment fragment = new StudentAssignmentFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);

            args.putString("someTitle", name);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_student_view_course, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            this.V = view;


            myOnClickListener = new MyOnClickListener();

            recyclerView = view.findViewById(R.id.cm_recycler_view);
            recyclerView.setHasFixedSize(true);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            task1 = new TaskCompletionSource<>();
            task2 = task1.getTask();
            AssignmentsMyData.loadAssignments(StudentViewCourse.courseInfo,task1);

            allTask = Tasks.whenAll(task2);

            allTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    data = new ArrayList<>();
                    Log.d("yo",Integer.toString(AssignmentsMyData.assignmentsInfoList.size()));
                    for (int i = 0; i < AssignmentsMyData.assignmentsInfoList.size(); i++) {
                        data.add(new AssignmentsDataModel(
                                AssignmentsMyData.assignmentsInfoList.get(i),
                                AssignmentsMyData.assignmentsNameList.get(i),
                                AssignmentsMyData.assignmentsDeadlineList.get(i)
                        ));
                    }

                    adapter = new StudentAssignmentsCustomAdapter(data);
                    recyclerView.setAdapter(adapter);
                }
            });
        }

        private class MyOnClickListener implements View.OnClickListener {

            private MyOnClickListener() {
            }

            @Override
            public void onClick(View v) {
                viewAssignment(v);
            }

            private void viewAssignment(View v) {
                int selectedItemPosition = recyclerView.getChildLayoutPosition(v);
                RecyclerView.ViewHolder viewHolder
                        = recyclerView.findViewHolderForLayoutPosition(selectedItemPosition);
                TextView textViewName
                        = viewHolder.itemView.findViewById(R.id.textViewCourseID);
                String selectedName = ( String ) textViewName.getText();

                Log.d("aman",selectedName);
                String ainfo = (String) viewHolder.itemView.getTag();
                Log.d("aman",ainfo);

                Intent intent = new Intent(getActivity(), ViewMyGroup.class);

                intent.putExtra("courseInfo",StudentViewCourse.courseInfo);
                intent.putExtra("assignID",ainfo);
                startActivity(intent);
            }
        }

    }


    public static class CMFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static RecyclerView.Adapter adapter;
        private static RecyclerView recyclerView;
        private static ArrayList<StudyMaterialDataModel> data;
        static View.OnClickListener myOnClickListener;
        public TaskCompletionSource<Integer> task1;
        public Task task2;
        private Task<Void> allTask;
        View V;

        public CMFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static CMFragment newInstance(int sectionNumber, String name) {
            CMFragment fragment = new CMFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString("someTitle", name);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_student_view_course, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            this.V = view;
            String courseInfo = StudentViewCourse.courseInfo;

            myOnClickListener = new MyOnClickListener();

            recyclerView = view.findViewById(R.id.cm_recycler_view);
            recyclerView.setHasFixedSize(true);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            task1 = new TaskCompletionSource<>();
            task2 = task1.getTask();
            StudyMaterialMyData.loadStudyMaterials(courseInfo,task1);

            allTask = Tasks.whenAll(task2);

            allTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    data = new ArrayList<>();
                    Log.d("yo",Integer.toString(StudyMaterialMyData.studyMaterialsNameList.size()));
                    for (int i = 0; i < StudyMaterialMyData.studyMaterialsNameList.size(); i++) {
                        data.add(new StudyMaterialDataModel(
                                StudyMaterialMyData.studyMaterialsNameList.get(i),
                                StudyMaterialMyData.studyMaterialsUrlList.get(i)
                        ));
                    }

                    adapter = new StudentStudyMaterialCustomAdapter(data);
                    recyclerView.setAdapter(adapter);
                }
            });
        }
        private class MyOnClickListener implements View.OnClickListener {

            private MyOnClickListener() {
            }

            @Override
            public void onClick(View v) {
                viewCM(v);
            }

            private void viewCM(View v) {
                int selectedItemPosition = recyclerView.getChildLayoutPosition(v);
                RecyclerView.ViewHolder viewHolder
                        = recyclerView.findViewHolderForLayoutPosition(selectedItemPosition);

                TextView textViewName
                        = viewHolder.itemView.findViewById(R.id.textViewCourseID);

                String selectedName = ( String ) textViewName.getText();

                String cminfo = (String) viewHolder.itemView.getTag();

                Log.d("cm",cminfo);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(cminfo)));

            }
        }
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return StudentAssignmentFragment.newInstance(0, "Assignments");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return CMFragment.newInstance(1, "Course Material");
                default:
                    return null;
            }
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below)
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}
