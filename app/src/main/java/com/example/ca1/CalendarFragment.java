package com.example.ca1;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CalendarFragment extends Fragment {
//    private final ArrayList<EventDay> events = new ArrayList<>();

    private ArrayList<EventDay> events = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ArrayList<Taskitem> mTask = new ArrayList<>();

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm aa", Locale.ENGLISH);
    private SimpleDateFormat dateOnly = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.label_calendar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_calendar, container, false);

        CalendarView calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        readTasks(calendarView);
        calendarView.setOnDayClickListener(eventDay ->
            readSelected(dateOnly.format(eventDay.getCalendar().getTime()))
        );
        return view;
    }


    // display event icon from firebase data
    private void addCalendarEvent(CalendarView calendarView, ArrayList<Taskitem> tasks) {
        events.clear();

        for(Taskitem t: tasks) {
            Calendar cal = Calendar.getInstance();
            String date = t.getStartDateTime();

            try {
                cal.setTime(sdf.parse(date));// all done
                Log.d("Dark says", date);
                events.add(new EventDay(cal, R.drawable.a));
            } catch(ParseException pe) {
                Toast.makeText(getContext(), "Date is is the wrong format", Toast.LENGTH_SHORT).show();
            }
        }


        calendarView.setEvents(events);
    }

    private void setUIRef(){
        mRecyclerView = view.findViewById(R.id.tasklist);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        Log.d("Dark says array length is", String.valueOf(mTask.size()));
        TaskItemArrayAdapter myRecyclerViewAdapter = new TaskItemArrayAdapter(mTask, new TaskItemArrayAdapter.MyRecyclerViewItemClickListener(){
            //Handling clicks
            @Override
            public void onItemClicked(Taskitem item)
            {
                //show popup
                TaskPopup popup = new TaskPopup();
                popup.taskDetail(item, view);
                Toast.makeText(view.getContext(), item.getName() ,Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(myRecyclerViewAdapter);
    }
    private void bindData(ArrayList<Taskitem> task) {
        // clear task before displaying new ones
        mTask.clear();
        for(Taskitem t: task) {
            Log.d("Dark says", t.getName()+ " "+t.getStartDateTime()+" "+t.getColor());
            mTask.add(new Taskitem(t.getName(),  t.getStartDateTime(), t.getColor()));

        }
    }

    // read from firebase
    public void readTasks(CalendarView calendarView) {
        // list of tasks from db
        ArrayList<Taskitem> tasks = new ArrayList<Taskitem>();

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

                    String name = (String) messageSnapshot.child("name").getValue();
                    String startDateTime = (String) messageSnapshot.child("startDateTime").getValue();
                    String color = (String) messageSnapshot.child("color").getValue();

                    Taskitem t = new Taskitem(name, startDateTime, color);
                    tasks.add(t);
                }
                //display event indicator
                addCalendarEvent(calendarView, tasks);

                //read tasks for that day
                readSelected(dateOnly.format(new Date()));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Dark says", "Failed to read value.", error.toException());
            }
        });
    }

    // read tasks for a specific day
    public void readSelected(String selectedDay) {
        Log.d("Dark says", selectedDay);
        ArrayList<Taskitem> tasks = new ArrayList<Taskitem>();

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

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    String name = (String) messageSnapshot.child("name").getValue();
                    String startDateTime = (String) messageSnapshot.child("startDateTime").getValue();
                    String color = (String) messageSnapshot.child("color").getValue();

                    // get only the date
                    Date d = new Date();
                    try {
                       d = sdf.parse(startDateTime);
                    } catch(ParseException pe) { Log.d("Darks says", pe.getMessage()); }

                    String startDate = dateOnly.format(d);
                    Log.d("Darks says date is", startDate);
                    if(startDate.equals(selectedDay)) {
                        Taskitem t = new Taskitem(name, startDateTime, color);
                        tasks.add(t);
                    }
                }
                //display tasks for that day
                bindData(tasks);
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