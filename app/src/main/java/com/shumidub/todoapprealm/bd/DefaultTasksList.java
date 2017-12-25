package com.shumidub.todoapprealm.bd;

import io.realm.RealmObject;

/**
 * Created by Артем on 25.12.2017.
 */

public class DefaultTasksList extends RealmObject {

    private long defaulyId;

    public long getDefaulyId() {
        return defaulyId;
    }

    public void setDefaulyId(long defaulyId) {
        this.defaulyId = defaulyId;
    }



    //toDo if null - all
}
