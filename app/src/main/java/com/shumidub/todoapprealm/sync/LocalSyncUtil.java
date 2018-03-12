package com.shumidub.todoapprealm.sync;

import android.app.Activity;
import android.content.Intent;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.realmmodel.FolderTaskObject;
import com.shumidub.todoapprealm.realmmodel.RealmFoldersContainer;
import com.shumidub.todoapprealm.realmmodel.TaskObject;
import com.shumidub.todoapprealm.realmmodel.notes.FolderNotesObject;
import com.shumidub.todoapprealm.realmmodel.notes.NoteObject;
import com.shumidub.todoapprealm.realmmodel.report.ReportObject;
import com.shumidub.todoapprealm.ui.activity.base.BaseActivity;

import io.realm.RealmList;


public class LocalSyncUtil {

    Activity activity;

    public LocalSyncUtil(Activity activity){
        this.activity = activity;
    }

    public void putMessage(String msg){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setType("text/plain");
        activity.startActivity(sendIntent);
    }

    private String getRealmDbAsString(){
        String message = "";
        String indent = "    ";
        String nextLine = "\n";

        App.initRealm();


        RealmList<FolderNotesObject> folderOfNotesList = App.realm.where(RealmFoldersContainer.class).findFirst().folderOfNotesList;

        for (FolderNotesObject folderNotesObject : folderOfNotesList){

            //todo add folderOfNotesInfo

            RealmList<NoteObject> notesList = folderNotesObject.getTasks();
            for (NoteObject notesObject : notesList){

                //todo add notesInfo

            }
        }



        RealmList<FolderTaskObject> folderOfTasksList = App.realm.where(RealmFoldersContainer.class).findFirst().folderOfNotesList;

        for (FolderTaskObject folderTaskObject : folderOfTasksList){

            //todo add folderOfTasksInfo

            RealmList<TaskObject> taskList = folderTaskObject.getTasks();
            for (TaskObject taskObject : taskList){

                //todo add tasksInfo

            }
        }


        for (ReportObject reportObject : App.realm.where(ReportObject.class).findAll()){

            //todo add report

        }



        return message;


    }


    public void putAllRealmDbAsMessage(){
        putMessage(getRealmDbAsString());
    }



}
