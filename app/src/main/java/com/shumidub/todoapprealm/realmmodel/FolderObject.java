package com.shumidub.todoapprealm.realmmodel;

import io.realm.RealmObject;

/**
 * Created by Артем on 24.12.2017.
 */

public class FolderObject extends RealmObject {

    private String name;
    private long id;

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

}
