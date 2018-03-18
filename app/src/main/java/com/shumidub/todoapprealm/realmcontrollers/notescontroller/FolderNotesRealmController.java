package com.shumidub.todoapprealm.realmcontrollers.notescontroller;


import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.realmmodel.notes.FolderNotesObject;
import com.shumidub.todoapprealm.realmmodel.notes.NoteObject;

import io.realm.RealmList;

public class FolderNotesRealmController implements INotesController {

    //FolderNotesContainer
    public static RealmList<FolderNotesObject> getFolderNotesContainerList(){return null;}

    //FolderNotes

    public static FolderNotesObject getFolderNote(long id) {
        App.initRealm();
        return App.realm.where(FolderNotesObject.class).equalTo("id", id).findFirst();
    }

    public static long addFolderNote(String name){
        long id = getNewValidFolderNotesId();
        App.initRealm();
        App.realm.executeTransaction((realm -> {
            FolderNotesObject folder = App.realm.createObject(FolderNotesObject.class);
            folder.setId(id);
            folder.setName(name);
            App.folderOfNotesContainerList.add(folder);
        }));
        return id;
    }

    public static void editFolderNote(long id, String name){
        App.initRealm();
        App.realm.executeTransaction((r) -> {
            App.realm.where(FolderNotesObject.class).equalTo("id", id).findFirst().setName(name);
        });

    }

    public static void delFolderNote(long id){
        App.initRealm();
        App.realm.executeTransaction((r) -> {
            App.realm.where(FolderNotesObject.class).equalTo("id", id).findFirst().deleteFromRealm();
        });
    }


    public static void reorderFolderNote(int from, int to){
        App.initRealm();
        App.realm.executeTransaction((r) -> {
            App.folderOfNotesContainerList.add(to, App.folderOfNotesContainerList.remove(from));
        });
    }

    public static long getNewValidFolderNotesId() {
        long id =  System.currentTimeMillis();
        App.initRealm();
        while ((App.realm.where(FolderNotesObject.class).equalTo("id", id)).findFirst()!=null){
            id ++;
        }
        return id;
    }

    //Notes

    public static RealmList<NoteObject> getNotesList(long idFolderNotesObject){
        return getFolderNote(idFolderNotesObject).getTasks();
    }

    public static NoteObject getNote(long idNotesObject){
        App.initRealm();
        return App.realm.where(NoteObject.class).equalTo("id", idNotesObject).findFirst();
    }

    public static long addNote(long idFolderNotesObject, String text){
        long id = getNewValidNotesId();
        App.initRealm();
        FolderNotesObject folderNotesObject
                = App.realm.where(FolderNotesObject.class)
                .equalTo("id", idFolderNotesObject).findFirst();
        App.realm.executeTransaction((realm -> {
            NoteObject noteObject = App.realm.createObject(NoteObject.class);
            noteObject.setId(id);
            noteObject.setText(text);
            folderNotesObject.getTasks().add(noteObject);
        }));
        return id;
    }

    public static void editNote(long idNotesObject, String text ){
        App.initRealm();
        App.initRealm();
        App.realm.executeTransaction((r) -> {
            App.realm.where(NoteObject.class).equalTo("id", idNotesObject)
                    .findFirst().setText(text);
        });
    }

    public static void delNote(long idNotesObject){
        App.initRealm();
        App.initRealm();
        App.realm.executeTransaction((r) -> {
            App.realm.where(NoteObject.class).equalTo("id", idNotesObject)
                    .findFirst().deleteFromRealm();
        });
    }

    public static void reorderNote(long idFolderNotesObject, long idNotesObject, int from, int to){
        App.initRealm();
        RealmList <NoteObject> notesList
                = App.realm.where(FolderNotesObject.class)
                .equalTo("id", idFolderNotesObject).findFirst().getTasks();
        App.realm.executeTransaction((r) -> {
            notesList.add(to, notesList.remove(from));
        });
    }

    public static long getNewValidNotesId() {
        long id =  System.currentTimeMillis();
        App.initRealm();
        while ((App.realm.where(NoteObject.class).equalTo("id", id)).findFirst()!=null){
            id ++;
        }
        return id;
    }
}
