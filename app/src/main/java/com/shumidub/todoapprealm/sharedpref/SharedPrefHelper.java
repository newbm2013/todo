package com.shumidub.todoapprealm.sharedpref;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import static android.app.PendingIntent.getActivity;

/**
 * Created by root on 26.12.17.
 */

public class SharedPrefHelper {

    static final String ID_LIST = "id_list";

    Activity activity;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public SharedPrefHelper(Activity activity){
        this.activity = activity;
    }

    public void setDefauiltListId(long idList){
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(ID_LIST, idList);
        editor.commit();
    }

    public void getDefaultListId(){
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        sharedPref.getLong(ID_LIST, 0);
    }
}
