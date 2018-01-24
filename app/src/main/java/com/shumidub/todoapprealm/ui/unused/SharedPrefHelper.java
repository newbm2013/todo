package com.shumidub.todoapprealm.ui.unused;

import android.app.Activity;
import android.content.SharedPreferences;

import com.shumidub.todoapprealm.model.ListModel;
import com.shumidub.todoapprealm.realmcontrollers.ListsRealmController;

import static android.app.PendingIntent.getActivity;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by root on 26.12.17.
 */

public class SharedPrefHelper {

    static final String ID_LIST = "id_list";
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    Activity activity;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public SharedPrefHelper(Activity activity){
        this.activity = activity;
        sharedPref =  activity.getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void setDefauiltListId(long idList){
        editor.putLong(ID_LIST, idList);
        editor.apply();
    }

    public long getDefaultListId(){
        long defaultListId = sharedPref.getLong(ID_LIST, 0);
        ListModel list = ListsRealmController.getListById(defaultListId);
        if (list == null)  defaultListId = 0;
        return defaultListId;
    }
}



