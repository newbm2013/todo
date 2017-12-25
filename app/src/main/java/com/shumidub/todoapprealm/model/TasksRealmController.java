package com.shumidub.todoapprealm.model;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.bd.ItemsBD;
import com.shumidub.todoapprealm.bd.TasksListBD;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by Артем on 21.12.2017.
 */


public class TasksRealmController {

   public static List<ItemsBD> getItems(){
        if (App.realm == null) App.initRealm();
        return App.realm.where(ItemsBD.class).findAll().sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }

    public static List<ItemsBD> getItems(long listId){
        if (App.realm == null) App.initRealm();
        return App.realm.where(ItemsBD.class).equalTo("taskListId", listId).findAll().sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }

    public static  void insertItems(String text, boolean done, boolean important, long taskListId ){
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ItemsBD item = realm.createObject(ItemsBD.class);
                item.setId(getIdForNextValue(ItemsBD.class));
                item.setText(text);
                item.setDone(done);
                item.setLastDoneDate(new Date());
                item.setImportant(important);
                item.setTaskListId(taskListId);
                item.setCountValue(1);
                item.setCycling(false);
                realm.insert(item);
            }
        });
    }

    public static  void insertItems(String text, boolean done, boolean important, String taskListName ){
        long taskListId = App.realm.where(TasksListBD.class).equalTo("name", taskListName).findFirst().getId();
        insertItems(text,done,important,taskListId);
    }

    public void insertItems(String text){
        insertItems(text, false, false, 0);
    }

    public static void setDoneByItemsId(boolean b, long id){
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ItemsBD items = realm.where(ItemsBD.class).equalTo("id", id).findFirst();
                items.setDone(b);
            }
        });
    }

    public static long getIdForNextValue(Class bdClass){
        long id =  System.currentTimeMillis();
//        while (!((App.realm.where(bdClass).equalTo("id", id)).isEmpty)){
//            id ++;
//        }
        return id;
    }
}
