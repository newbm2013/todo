package com.shumidub.todoapprealm;

import android.app.Application;

import com.shumidub.todoapprealm.realmcontrollers.FolderTaskRealmController;
import com.shumidub.todoapprealm.realmcontrollers.taskcontroller.TasksRealmController;
import com.shumidub.todoapprealm.realmmodel.FolderTaskObject;
import com.shumidub.todoapprealm.realmmodel.RealmFoldersContainer;
import com.shumidub.todoapprealm.realmmodel.notes.FolderNotesObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmModel;

/**
 * Created by Артем on 19.12.2017.
 *
 */

public class App extends Application {

    public static Realm realm;
    public static RealmFoldersContainer realmFoldersContainer;
    public static RealmList<FolderTaskObject> folderOfTasksListFromContainer;
    public static RealmList<FolderNotesObject> folderOfNotesContainerList;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build());
        initRealm();
        initContainers();
        if(BuildConfig.DEBUG && FolderTaskRealmController.listOfFolderIsEmpty()) addContent();
    }

    public static void initRealm() {
        if (realm == null) realm = Realm.getDefaultInstance();
    }

    public static void closeRealm(){
        realm = null;
    }

    private static void initContainers(){
        App.initRealm();
        realm.executeTransaction((realm) -> {
            if (!FolderTaskRealmController.containerOfFolderIsExist()){
                realmFoldersContainer = realm.createObject(RealmFoldersContainer.class);
            } else {
                realmFoldersContainer = realm.where(RealmFoldersContainer.class).findFirst();
            }

        });

        folderOfTasksListFromContainer = realmFoldersContainer.folderOfTasksList;
        folderOfNotesContainerList = realmFoldersContainer.folderOfNotesList;
    }

    private void addContent() {

        long[] folderId = new long[1];

        realm.executeTransaction((Realm realm) -> {

            final FolderTaskObject folderObject
                    = realm.createObject(FolderTaskObject.class);
            folderObject.setId(System.currentTimeMillis());
            folderObject.setName("folderObject 1");

            final FolderTaskObject folderObject2
                    = realm.createObject(FolderTaskObject.class);
            folderObject2.setId(System.currentTimeMillis());
            folderObject2.setName("folderObject 2");


            folderOfTasksListFromContainer.add(folderObject);
            folderOfTasksListFromContainer.add(folderObject2);

            final RealmModel gettedFolderObject = folderOfTasksListFromContainer.get(0);

            folderId[0] = ((FolderTaskObject)gettedFolderObject).getId();

        });

        for (int i = 0; i < 20; i++) {
            TasksRealmController.addTask("task " + i, 1, 1,
                    false, 1, folderId[0]);
        }

        folderOfTasksListFromContainer.toArray();
        folderOfTasksListFromContainer.toArray();
        folderOfTasksListFromContainer.toArray();

    }
}
