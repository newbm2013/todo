package com.shumidub.todoapprealm.bd;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Артем on 19.12.2017.
 */

public class ItemsBD extends RealmObject {

    private long id;
    private String text;
    private boolean done;
    private long taskListId;
    private boolean important;
    private Date lastDoneDate;
    private boolean isCycling;



    private int countValue;

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

    public boolean isImportant() {return important;}
    public void setImportant(boolean important) {this.important = important;}

    public Date getLastDoneDate() { return lastDoneDate;}
    public void setLastDoneDate(Date lastDoneDate) {this.lastDoneDate = lastDoneDate;}

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



}
