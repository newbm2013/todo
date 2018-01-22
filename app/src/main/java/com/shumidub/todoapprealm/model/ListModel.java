package com.shumidub.todoapprealm.model;

import io.realm.RealmObject;

/**
 * Created by Артем on 24.12.2017.
 */

public class ListModel extends RealmObject {

    private String name;
    private long id;
    private boolean isCycling;
//  private boolean isDefault; no field only view for DefaultTaskList setDefault

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isCycling() {
        return isCycling;
    }

    public void setCycling(boolean cycling) {
        isCycling = cycling;
    }

}
