package com.example.ca1;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TasksFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ArrayList<Taskitem> tasks;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.label_tasks);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tasks, container, false);

        //read user's task from firebase
        readAndBindTasks();
//        setUIRef();

        FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.addTask);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                switch (v.getId()) {
                    case R.id.addTask:
                        i = new Intent(view.getContext(), AddTasks.class);
                        startActivity(i);
                        break;
                }
            }
        });
        return view;
    }

    private void setUIRef() {
        mRecyclerView = view.findViewById(R.id.tasklist);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        TaskItemArrayAdapter myRecyclerViewAdapter = new TaskItemArrayAdapter(tasks, new TaskItemArrayAdapter.MyRecyclerViewItemClickListener(){
            //Handling clicks
            @Override
            public void onItemClicked(Taskitem item)
            {
                /*Intent i;
                Log.d("QWERTY HERH",item.getEndDateTime()+"enddd");
                i = new Intent(view.getContext(), AddTasks.class);
                i.putExtra("items", item);
                startActivity(i);*/
                //show popup
                TaskPopup popup = new TaskPopup();
                popup.taskDetail(item, view);
                Toast.makeText(view.getContext(), item.getName() ,Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(myRecyclerViewAdapter);
    }


    public void readAndBindTasks() {
        // list of tasks from db
        tasks = new ArrayList<Taskitem>();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("tasks");
        // get tasks belonging to user
        Query q = myRef.orderByChild("userID").equalTo(uid);

        // Read from the database
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tasks.clear();

//                for (String i : t.keySet()) {
//                    bindData((Taskitem) t.get(i));
//                    Log.d("Dark says", "Value is: " + i + ": " + t.get(i));
//                  }

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    String key = messageSnapshot.getKey();
                    String name = (String) messageSnapshot.child("name").getValue();
                    String startDateTime = (String) messageSnapshot.child("startDateTime").getValue();

                    String endDateTime = (String) messageSnapshot.child("endDateTime").getValue();

                    String color = (String) messageSnapshot.child("color").getValue();
                    ArrayList<SubTaskitem> subTask = new ArrayList<>();
//                     = (ArrayList<SubTaskitem>) messageSnapshot.child("subTask").getValue();
                    for (DataSnapshot x: messageSnapshot.child("subTask").getChildren()) {
                        SubTaskitem sub = x.getValue(SubTaskitem.class);
                        subTask.add(sub);
                    }
                    String userID = (String) messageSnapshot.child("userID").getValue();
                    ArrayList<String> tags = (ArrayList<String>) messageSnapshot.child("tags").getValue();
                    Boolean completed = (Boolean) messageSnapshot.child("completed").getValue();
                    //Taskitem t = new Taskitem(name, startDateTime, color);
                    Taskitem t = new Taskitem(name, startDateTime, endDateTime, color, tags, subTask, userID, completed, key);
                    tasks.add(t);

                }
                //display xml
//                bindData(tasks);
                setUIRef();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Dark says", "Failed to read value.", error.toException());
            }
        });
    }
}