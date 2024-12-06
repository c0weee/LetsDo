package com.example.ca1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Taskitem implements Serializable {
    private String name;
    private String startDateTime;
    private String endDateTime;
    private String color;
    private ArrayList<SubTaskitem> subTask;
    private String userID;
    private ArrayList<String> tags;
    private Boolean completed;
    private String key;

    // used to display tasks
    public Taskitem(String name, String startDateTime, String color) {
        this.name = name;
        this.startDateTime = startDateTime;
        this.color = color;
    }

    // without completed field. use case: when task is created
    public Taskitem(String name, String startDateTime, String endDateTime,
                    String color, ArrayList<String> tags, ArrayList<SubTaskitem> subTask, String userID) {
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.color = color;
        this.subTask = subTask;
        this.userID = userID;
        this.tags = tags;
        this.completed = false;
    }

    // with completed. use case: place data from database
    public Taskitem(String name, String startDateTime, String endDateTime,
                    String color, ArrayList<String> tags, ArrayList<SubTaskitem> subTask,
                    String userID, Boolean completed, String key) {
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.color = color;
        this.subTask = subTask;
        this.userID = userID;
        this.tags = tags;
        this.key = key;
    }

    public String getName(){
        return name;
    }

    public String getStartDateTime(){
        return startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public String getColor(){
        return color;
    }

    public ArrayList<String> getTags(){
        return tags;
    }

    public ArrayList<SubTaskitem> getSubTask() {
        return subTask;
    }

    public String getUserID() {
        return userID;
    }

    public Boolean getCompleted() { return completed; }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void setSubTask(ArrayList<SubTaskitem> subTask) {
        this.subTask = subTask;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setCompleted(Boolean completed) { this.completed = completed; }

//    public Taskitem hashToObject(Map.Entry<String,Object> entry){
//        String key=entry.getKey();
//        Long price=entry.getValue();
//
//        String[] keys = key.split("//-");
//        // some logic to set name and city
//        res.name = keys[0];
//        if(keys.length > 1) {
//            res.city = keys[1];
//        }
//        //set price
//        price=value;
//        return res;
//    }
}
