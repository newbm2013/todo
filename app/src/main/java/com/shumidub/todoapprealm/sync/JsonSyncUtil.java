package com.shumidub.todoapprealm.sync;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.realmmodel.RealmFoldersContainer;

import io.realm.RealmObject;

/**
 * Created by A.shumidub on 19.03.18.
 *
 */

public class JsonSyncUtil {

    RealmFoldersContainer realmFoldersContainer;
    Activity activity;

    public JsonSyncUtil(Activity activity){
        App.initRealm();
        realmFoldersContainer = App.realm.where(RealmFoldersContainer.class).findFirst();
        this.activity = activity;
    }

    public void realmBdToJson(){

        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        String json = gson.toJson(App.realm.copyFromRealm(realmFoldersContainer));




        FileWritter.saveFile(json);



        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, json);
        sendIntent.setType("text/plain");
        activity.startActivity(sendIntent);

    }

    public void realmBdFromJson(){
        String json = FileWritter.readFile();
        if (!TextUtils.isEmpty(json)) {
            GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
            Gson gson = builder.create();
            realmFoldersContainer = gson.fromJson(json, RealmFoldersContainer.class);
//            App.realm.copyToRealm(realmFoldersContainer);
        }
    }


    public boolean jsonIsExist(){
        return  FileWritter.isBackupExist();
    }



}
