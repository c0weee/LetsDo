package com.example.ca1;


import android.content.Intent;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class TaskPopup extends PopupWindow{
    private PopupWindow popupWindow;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private boolean taskComplete = false;

    private ChipGroup chipGroupTag;
    private ArrayList<String> tagArr = new ArrayList<>();

    public void taskDetail(Taskitem item, final View view) {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_taskinfo, null);

        //Specify the length and width through constants
        int width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        int height = RelativeLayout.LayoutParams.WRAP_CONTENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler
        TextView name = popupView.findViewById(R.id.popuptitle);
        TextView startDate = popupView.findViewById(R.id.startdate);
        TextView endDate = popupView.findViewById(R.id.enddate);
        ProgressBar progress = popupView.findViewById(R.id.taskprogress);

        Log.d("Dark says", String.valueOf(R.id.chipGroupTag2));


        chipGroupTag = popupView.findViewById(R.id.chipGroupTag2);
        tagArr = item.getTags();
        if(tagArr != null){

            Log.d("Dark says tag is", String.valueOf(chipGroupTag));
            for(int i = 0 ; i < tagArr.size(); i++) {
                Chip chip = new Chip(popupView.getContext());
                Log.d("Dark says", "I'm here!!");
                ChipDrawable drawable = ChipDrawable.createFromAttributes(popupView.getContext(), null, 0, R.style.Widget_MaterialComponents_Chip_Choice);
                Log.d("Dark says", "I'm here 2");
                chip.setChipDrawable(drawable);

                chip.setCheckable(false);
                chip.setClickable(false);
                chip.setText(tagArr.get(i));

                chipGroupTag.addView(chip);
            }
        }
        name.setText(item.getName());
        startDate.setText(item.getStartDateTime().substring(0, 11));
        endDate.setText(item.getEndDateTime().substring(0, 11));

        ArrayList<SubTaskitem> subtask = item.getSubTask();
        RecyclerView subtaskView = popupView.findViewById(R.id.subtasks);
        subtaskView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        PopupItemArrayAdapter myRecyclerViewAdapter = new PopupItemArrayAdapter(subtask, new PopupItemArrayAdapter.MyRecyclerViewItemClickListener() {
            //Handling clicks
            @Override
            public void onItemClicked(SubTaskitem subitem) {
                boolean checked = subitem.getCompleted();
                subitem.setCompleted(!checked);
            }
        });
        subtaskView.setAdapter(myRecyclerViewAdapter);

        progress.setMax(subtask.size());
        int complete = 0;
        for (SubTaskitem subitem:subtask) {
            if(subitem.getCompleted()){
                complete++;
            }
        }
        progress.setProgress(complete);

        if (complete == subtask.size()){
            taskComplete = true;
            rewardUser();
            Toast.makeText(view.getContext(), "You got a reward! Go to the gallery to see your reward.", Toast.LENGTH_LONG).show();
        }

        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Close the window when clicked
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("tasks").child(item.getKey());
                myRef.setValue(item);
                popupWindow.dismiss();
                return true;
            }
        });

        Button edit = popupView.findViewById(R.id.buttonedit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("QWERTY HERH",item.getEndDateTime()+"enddd");
                Intent i = new Intent(v.getContext(), AddTasks.class);
                i.putExtra("items", item);
                v.getContext().startActivity(i);
            }
        });

        Button done = popupView.findViewById(R.id.buttondone);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("tasks").child(item.getKey());
                myRef.setValue(item);
                popupWindow.dismiss();
            }
        });
    }


    public void dismiss(){
        popupWindow.dismiss();
    }


    public void rewardUser() {
        FirebaseDatabase database;
        DatabaseReference myRef;

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("rewards/"+userID);


        // create a list of Integer type
        List<String> list = new ArrayList<>();

        // add elements
        list.add("whale");
        list.add("whale");
        list.add("whale");
        list.add("dark");
        list.add("beee");
        list.add("beee");
        list.add("beee");
        list.add("beee");
        list.add("dumbo");
        list.add("dumbo");
        list.add("dumbo");
        list.add("dumbo");
        list.add("dumbo");
        list.add("dumbo");
        list.add("dumbo");
        list.add("dumbo");

        // get random pet
        Random randomizer = new Random();
        String random = list.get(randomizer.nextInt(list.size()));

        // save image name
        myRef.push().setValue(random);
    }
}
