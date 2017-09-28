package com.example.rosan.tasklist;

/* Created by rosan on 28-9-2017. */

public class Task {

    String name;
    private String completed;
    private int _id;

    public Task(String taskName) {
        name = taskName;
    }

    public Task(String taskName, int taskID, String com){
        name = taskName;
        _id = taskID;
        completed = com;

    }

    public String getName(){return name;}

    //public void setTask(String newTask) {name = newTask; }
    public String getCompleted(){return completed; }

    public void setCompleted(String s){ this.completed = s; }

    public int getID() {return _id; }

}
