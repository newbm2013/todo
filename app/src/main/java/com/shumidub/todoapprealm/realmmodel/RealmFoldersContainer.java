package com.shumidub.todoapprealm.realmmodel;

import com.shumidub.todoapprealm.realmmodel.notes.FolderNotesObject;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Артем on 25.01.2018.
 *
 */

public class RealmFoldersContainer<R> extends RealmObject {
    public RealmList<FolderTaskObject> folderOfTasksList;
    public RealmList<FolderNotesObject> folderOfNotesList;
}
