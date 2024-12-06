package com.example.ca1;

import java.io.Serializable;

public class SubTaskitem implements Serializable {
    private String name;
    private Boolean completed;

    public SubTaskitem() {

    }
    //    make SubTaskitem using only title
    public SubTaskitem(String name) {
        this.name = name;
        this.completed = false;
    }

    public SubTaskitem(String name, Boolean completed) {
        this.name = name;
        this.completed = completed;
    }

    public String getName() {
        return name;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}