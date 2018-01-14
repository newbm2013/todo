package com.shumidub.todoapprealm.model;

import io.realm.RealmObject;

/**
 * Created by Артем on 19.12.2017.
 */

public class TaskModel extends RealmObject {

    private long id;
    private String text;
    private boolean done;
    private long taskListId;
    private int lastDoneDate;
    private boolean isCycling;
    private int countValue;
//    private int maxAccumulation;
//
//    public int getMaxAccumulation() {
//        return maxAccumulation;
//    }
//
//    public void setMaxAccumulation(int maxAccumulation) {
//        this.maxAccumulation = maxAccumulation;
//    }

    private int priority;

    public long getId() {return id;}
    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public boolean isDone() {
        return done;
    }
    public void setDone(boolean done) {
        this.done = done;
    }

    public long getTaskListId() {return taskListId; }
    public void setTaskListId(long taskListId) {this.taskListId = taskListId;}



    public int getLastDoneDate() { return lastDoneDate;}
    public void setLastDoneDate(int lastDoneDate) {this.lastDoneDate = lastDoneDate;}

    public boolean isCycling() {
        return isCycling;
    }

    public void setCycling(boolean cycling) {
        isCycling = cycling;}

    public int getCountValue() {
        return countValue;
    }

    public void setCountValue(int countValue) {
        this.countValue = countValue;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


}
