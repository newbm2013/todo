package com.shumidub.todoapprealm.realmmodel;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Артем on 25.01.2018.
 */

public class RealmFoldersContainer<R> extends RealmObject {
    public RealmList<FolderObject> folderOfTasksList;
}
