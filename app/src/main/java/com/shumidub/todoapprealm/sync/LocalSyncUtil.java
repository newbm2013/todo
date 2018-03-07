package com.shumidub.todoapprealm.sync;

import android.app.Activity;
import android.content.Intent;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.realmmodel.RealmFoldersContainer;
import com.shumidub.todoapprealm.realmmodel.report.ReportObject;
import com.shumidub.todoapprealm.ui.activity.base.BaseActivity;


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

        App.initRealm();
        message = App.realm.where(RealmFoldersContainer.class).findFirst().toString();


//        message = App.realm.where(RealmFoldersContainer.class).findFirst().toString() + indent
//                + App.realm.where(ReportObject.class).findAll().toString();

        return message;
    }


    public void putAllRealmDbAsMessage(){
        putMessage(getRealmDbAsString());
    }





}
