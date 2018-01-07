package com.shumidub.todoapprealm;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.shumidub.todoapprealm.model.CategoryModel;
import com.shumidub.todoapprealm.model.TaskModel;
import com.shumidub.todoapprealm.realmcontrollers.CategoriesRealmController;
import com.shumidub.todoapprealm.realmcontrollers.ListsRealmController;
import com.shumidub.todoapprealm.realmcontrollers.TasksRealmController;

import java.util.List;

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
                .deleteRealmIfMigrationNeeded()
                .build());
        initRealm();
        if(BuildConfig.DEBUG && CategoriesRealmController.categoriesIsEmpry()) addContent();
    }

    public static void initRealm() {
        if (realm == null) realm = Realm.getDefaultInstance();
    }

    public static void closeRealm(){
        realm = null;
    }

    private void addContent() {
        initRealm();
        for (int i = 0; i<20; i++) {
            long idCategory = CategoriesRealmController.addCategory("Category ");
            if(i%2==0){
                for (int i2 =0; i2<10; i2++) {
                    long idList = ListsRealmController.addTasksLists("List " , false, false, idCategory);
                    if (i2%2==0){
                        for (int i3=0; i3<100; i3++){
                            TasksRealmController.insertItems("Task ", false, false, idList);
                        }
                    }
                }
            }
        }
    }
}
