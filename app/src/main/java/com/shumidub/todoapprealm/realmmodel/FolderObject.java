package com.shumidub.todoapprealm.realmmodel;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Артем on 24.12.2017.
 */

public class FolderObject extends RealmObject {

    private String name;
    private long id;
    public io.realm.RealmList<TaskObject> folderTasks;

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

    public RealmList<TaskObject> getTasks() {
        return folderTasks;
    }
    public void setTasks(RealmList<TaskObject> tasks) {
        this.folderTasks = folderTasks;
    }
}
