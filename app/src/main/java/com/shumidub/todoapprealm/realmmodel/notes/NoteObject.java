package com.shumidub.todoapprealm.realmmodel.notes;

import com.shumidub.todoapprealm.realmmodel.BaseRealmObject;

import io.realm.RealmObject;

/**
 * Created by Артем on 19.12.2017.
 *
 */

public class NoteObject extends BaseRealmObject {

    private long id;
    private String text;

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

}
