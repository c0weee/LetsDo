package com.example.ca1;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNav;
    Deque<Integer> integerDeque = new ArrayDeque<>(3);

    public void saveTask(Taskitem task) {
        // Write task to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("tasks");

        myRef.push().setValue(task);
    }

    public void addTask() {
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("Project");
        tags.add("Homework");

        ArrayList<SubTaskitem> st = new ArrayList<SubTaskitem>();
        st.add(new SubTaskitem("Finish ANDE first"));
        st.add(new SubTaskitem("Finish mobile part"));
        st.add(new SubTaskitem("Error Handling"));
        st.add(new SubTaskitem("Do documentations"));

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();
//
//        Taskitem task = new Taskitem("ADES", "6 Feb 2021 8.10pm",
//                "10 Feb 2021 6pm", "#FFFFFC00",
//                tags, st, userID);
//        saveTask(task);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        FirebaseAuth.getInstance().signOut();

        // check to see if the user is currently signed in
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }

        // show layout if authenticated
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottomNavigationView);
        integerDeque.push(R.id.tasks);

        loadFragment(new TasksFragment());

        bottomNav.setSelectedItemId(R.id.tasks);

        bottomNav.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        integerDeque.push(id);
                        loadFragment((getFragment(item.getItemId())));
                        return true;
                    }
                }
        );
    }

    private Fragment getFragment(int itemId) {
        switch (itemId) {
            case R.id.tasks:
                bottomNav.getMenu().getItem(0).setChecked(true);

                getSupportActionBar().setTitle(R.string.label_tasks);
                getSupportActionBar().show();
                return new TasksFragment();
            case R.id.calendar:
                getSupportActionBar().hide();
                bottomNav.getMenu().getItem(1).setChecked(true);
                return new CalendarFragment();
            case R.id.gallery:
                bottomNav.getMenu().getItem(2).setChecked(true);
                getSupportActionBar().setTitle(R.string.label_gallery);
                getSupportActionBar().show();

                return new GalleryFragment();
        }
        bottomNav.getMenu().getItem(0).setChecked(true);
        return new TasksFragment();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    public void onBackPressed() {
        integerDeque.pop();
        if (!integerDeque.isEmpty()) {
            loadFragment(getFragment(integerDeque.peek()));
        } else {
            finish();
        }
    }

}