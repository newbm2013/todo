package com.shumidub.todoapprealm;

import android.app.Application;
import android.util.Log;
import com.shumidub.todoapprealm.realmcontrollers.FolderRealmController;
import com.shumidub.todoapprealm.realmmodel.FolderObject;
import com.shumidub.todoapprealm.realmmodel.RealmFoldersContainer;
import com.shumidub.todoapprealm.realmmodel.TaskObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmModel;

/**
 * Created by Артем on 19.12.2017.
 */

public class App extends Application {

    public static Realm realm;
    public static RealmFoldersContainer realmFoldersContainer;
    public static RealmList<FolderObject> folderOfTasksListContainer;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build());
        initRealm();

        //todo добавить в контроллере проверку на существование и использовать эту проверку здесь, а то вдруг нулл
        realmFoldersContainer= realm.where(RealmFoldersContainer.class).findFirst();
        folderOfTasksListContainer = realmFoldersContainer.folderOfTasksList;

        if(BuildConfig.DEBUG && FolderRealmController.listOfFolderIsEmpty())  addContent();
        Log.d("DTAG", "addContent: " + FolderRealmController.getFolders().size());
    }

    public static void initRealm() {
        if (realm == null) realm = Realm.getDefaultInstance();
    }

    public static void closeRealm(){
        realm = null;
    }

//    private void addContent() {
//        initRealm();
//        for (int i2 =0; i2<40; i2++) {
//            long idList = FolderRealmController.addFolder("List "+i2);
//            if (i2%3!=0){
//                int i4 = 0;
//                for (int i3=0; i3<30; i3++){
//                    if (i4<10) i4++;
//                    else i4=1;
//                    boolean b = i3%4 ==0;
//                    int maxAccumulation = i4/2 > 0 ? i4/2 : 1;
//                    TasksRealmController.addTask("Task " +i3, i4, maxAccumulation  , b, 2, idList);
//                }
//            }
//        }
//    }


    private void addContent() {
        realm.executeTransaction((Realm realm) -> {

            if (realm.where(RealmFoldersContainer.class).findFirst() == null) {
                RealmFoldersContainer foldersOfTaskContainer = realm.createObject(RealmFoldersContainer.class);

                final FolderObject folderObject
                        = realm.createObject(FolderObject.class);
                folderObject.setId(System.currentTimeMillis());
                folderObject.setName("folderObject 1");

                final FolderObject folderObject2
                        = realm.createObject(FolderObject.class);
                folderObject2.setId(System.currentTimeMillis());
                folderObject2.setName("folderObject 2");


                foldersOfTaskContainer.folderOfTasksList.add(folderObject);
                foldersOfTaskContainer.folderOfTasksList.add(folderObject2);

                final RealmModel gettedFolderObject = foldersOfTaskContainer.folderOfTasksList.get(0);

                for (int i = 0; i < 100; i++) {
                    TaskObject task = realm.createObject(TaskObject.class);
                    task.setId(System.currentTimeMillis());
                    task.setText("task № " + i);
                    ((FolderObject) gettedFolderObject).folderTasks.add(task);
                }
            }
        });
    }

}
