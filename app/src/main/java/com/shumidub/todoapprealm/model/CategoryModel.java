package com.shumidub.todoapprealm.model;

import io.realm.RealmObject;

/**
 * Created by Артем on 24.12.2017.
 */

public class CategoryModel extends RealmObject{

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
