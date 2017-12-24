package com.shumidub.todoapprealm;

import android.app.Application;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Артем on 19.12.2017.
 */

public class TodoApp extends Application {

    public final static String TAG = "DEBUG_TAG";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder().build());
    }



}
