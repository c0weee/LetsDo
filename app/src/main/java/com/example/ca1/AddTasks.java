package com.example.ca1;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;


public class AddTasks extends AppCompatActivity implements View.OnClickListener {
    private EditText editTag;
    private ChipGroup chipGroupTag;
    private TextView startDate;
    private TextView endDate;
    private String chosen;
    private String which;
    private ImageView btnColor;

    private SimpleDateFormat sdf;
    private String timeNow;
    private String timeLater;
    private String finalColor = null;

    private Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    final int hour = calendar.get(Calendar.HOUR);
    final int min = calendar.get(Calendar.MINUTE);

    private DatePickerDialog.OnDateSetListener setDateListener;
    private TimePickerDialog.OnTimeSetListener setTimeListener;

    private ListView lv;
    private EditText itemText;
    private ArrayList<String> itemNameList;
    private ArrayList<SubTaskitem> itemList;
    private ArrayAdapter adapter;

    private ArrayList<String> tagArr;
    private EditText title;
    private String userID;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private Taskitem task;
    private String key;
    private int red = 0;
    private int green = 0;
    private int blue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addtasks);
        btnColor = findViewById(R.id.btnColor);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);

        editTag = findViewById(R.id.editTag);
        chipGroupTag = findViewById(R.id.chipGroupTag);

        lv = findViewById(R.id.listView);
        itemText = findViewById(R.id.editTask);

        title = findViewById(R.id.editTitle);

        itemList = new ArrayList<>();
        itemNameList = new ArrayList<>();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemNameList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                /*YOUR CHOICE OF COLOR*/
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(14);
                return view;
            }
        };

        try {
            if (getIntent().getExtras() == null) {
                getSupportActionBar().setTitle("Add Task");

                Date date;
                sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
                timeNow = sdf.format(new Date());

                date = sdf.parse(timeNow);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.HOUR, 1);
                timeLater = sdf.format(calendar.getTime());

                startDate.setText(timeNow);
                endDate.setText(timeLater);

                Button btn = (Button) findViewById(R.id.btnDelete);
                btn.setEnabled(false);

            } else {
                getSupportActionBar().setTitle("Edit Task");
                task = (Taskitem) getIntent().getSerializableExtra("items");
                key = task.getKey();

                finalColor = task.getColor();
                if (finalColor != null) {
                    btnColor.setColorFilter(Color.parseColor(finalColor));
                    int color = Color.parseColor(finalColor);
                    red = Color.red(color);
                    green = Color.green(color);
                    blue = Color.blue(color);
                }

                title.setText(task.getName());
                startDate.setText(task.getStartDateTime());
                endDate.setText(task.getEndDateTime());

                tagArr = task.getTags();
                if (tagArr != null) {
                    for (int i = 0; i < tagArr.size(); i++) {
                        Chip chip = new Chip(this);
                        ChipDrawable drawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.Widget_MaterialComponents_Chip_Entry);
                        chip.setChipDrawable(drawable);

                        chip.setCheckable(false);
                        chip.setClickable(false);
                        chip.setText(tagArr.get(i));
                        chip.setOnCloseIconClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chipGroupTag.removeView(chip);
                            }
                        });
                        chipGroupTag.addView(chip);
                    }
                }
                itemList = task.getSubTask();
                if (itemList != null) {
                    for (int i = 0; i < itemList.size(); i++) {
                        itemNameList.add(itemList.get(i).getName());
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final ColorPicker cp = new ColorPicker(AddTasks.this, red, green, blue);
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Show color picker dialog */
                cp.show();
            }
        });
        /* Set a new Listener called when user click "select" */
        cp.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(@ColorInt int color) {
                // Do whatever you want
                finalColor = "#" + Integer.toHexString(color);
                btnColor.setColorFilter(Color.parseColor(finalColor));
                cp.cancel();
            }
        });
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, setDateListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
                which = "start";
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, setDateListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
                which = "end";
            }
        });
        setDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                chosen = String.format("%02d", dayOfMonth) + "/" + String.format("%02d", month) + "/" + year;
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTasks.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, setTimeListener, hour, min, false);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
            }
        };
        setTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                int hour = hourOfDay % 12;
                chosen += " " + String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                        minute, hourOfDay < 12 ? "AM" : "PM");
                if (which == "start") {
                    startDate.setText(chosen);
                } else {
                    endDate.setText(chosen);
                }
            }

            ;
        };

        editTag.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                    Chip chip = new Chip(v.getContext());
                    ChipDrawable drawable = ChipDrawable.createFromAttributes(v.getContext(), null, 0, R.style.Widget_MaterialComponents_Chip_Entry);
                    chip.setChipDrawable(drawable);

                    chip.setCheckable(false);
                    chip.setClickable(false);

                    if (editTag.getText().toString().length() > 0) {
                        chip.setText((editTag.getText().toString()));
                        chip.setOnCloseIconClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chipGroupTag.removeView(chip);
                            }
                        });
                        chipGroupTag.addView(chip);
                        editTag.setText("");
                    }
                    return true;
                }
                return false;
            }
        });

        itemText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                    if (itemText.getText().toString().length() > 0) {
                        itemNameList.add(itemText.getText().toString());
                        itemList.add(new SubTaskitem(itemText.getText().toString()));
                        itemText.setText("");
                        adapter.notifyDataSetChanged();
                    }
                    return true;
                }
                return false;
            }
        });

        lv.setAdapter(adapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                itemNameList.remove(position);
                itemList.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDone:
                if (finalColor == null) {
                    finalColor = "#000004";
                }
                tagArr = new ArrayList<>();
                for (int i = 0; i < chipGroupTag.getChildCount(); i++) {
                    String tag = ((Chip) chipGroupTag.getChildAt(i)).getText().toString();
                    tagArr.add(tag);
                }

                if (title.getText().toString().length() == 0) {
                    title.setText("Event");
                }
                sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                Date date_end;
                Date date_start;
                try {
                    date_start = sdf.parse(startDate.getText().toString());
                    date_end = sdf.parse(endDate.getText().toString());
                    if (date_start.after(date_end)) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage("Start Date cannot be after End Date!");
                        alertDialog.setNegativeButton("Ok", null);
                        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                        alertDialog.show();
                    } else {
                        addTask();
                        if (getIntent().getExtras() == null) {
                            saveTask();
                        } else {
                            updateTask();
                        }
                        finish();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnDelete:
                DatabaseReference dbR = FirebaseDatabase.getInstance().getReference("tasks").child(key);
                dbR.removeValue();
                TaskPopup popup = new TaskPopup();
                popup.dismiss();
                finish();
                break;
        }
    }

    public void saveTask() {
        // Write task to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("tasks");
        myRef.push().setValue(task);

    }

    public void addTask() {
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        task = new Taskitem(title.getText().toString(), startDate.getText().toString(),
                endDate.getText().toString(), finalColor,
                tagArr, itemList, userID);
    }

    public void updateTask() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("tasks").child(key);
        myRef.setValue(task);
    }
}
