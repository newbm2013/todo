package com.shumidub.todoapprealm;

import android.app.Application;
import android.util.Log;
import com.shumidub.todoapprealm.realmcontrollers.FolderRealmController;
import com.shumidub.todoapprealm.realmcontrollers.TasksRealmController;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Артем on 19.12.2017.
 */

public class App extends Application {

    public static Realm realm;

    public final static String TAG = "DEBUG_TAG";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build());
        initRealm();
//        if(BuildConfig.DEBUG && FolderRealmController.listOfFolderIsEmpty()) addContent();
        Log.d("DTAG", "addContent: " + FolderRealmController.getFolders().size());
    }

    public static void initRealm() {
        if (realm == null) realm = Realm.getDefaultInstance();
    }

    public static void closeRealm(){
        realm = null;
    }

    private void addContent() {
        initRealm();
        for (int i2 =0; i2<40; i2++) {
            long idList = FolderRealmController.addFolder("List "+i2);
            if (i2%3!=0){
                int i4 = 0;
                for (int i3=0; i3<30; i3++){
                    if (i4<10) i4++;
                    else i4=1;
                    boolean b = i3%4 ==0;
                    int maxAccumulation = i4/2 > 0 ? i4/2 : 1;
                    TasksRealmController.addTask("Task " +i3, i4, maxAccumulation  , b, 2, idList);
                }
            }
        }
    }
}
