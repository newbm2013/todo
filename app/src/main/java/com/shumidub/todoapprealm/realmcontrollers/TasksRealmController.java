package com.shumidub.todoapprealm.realmcontrollers;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.model.ListModel;
import com.shumidub.todoapprealm.model.TaskModel;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by Артем on 21.12.2017.
 */

public class TasksRealmController {

   public static List<TaskModel> getItems(){
        App.initRealm();
        return App.realm.where(TaskModel.class).findAll().sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }

    public static List<TaskModel> getItems(long listId){
        App.initRealm();
        return App.realm.where(TaskModel.class).equalTo("taskListId", listId).findAll().sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }

    public static  void insertItems(String text, boolean done, boolean important, long taskListId ){
        App.initRealm();
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaskModel item = realm.createObject(TaskModel.class);
                item.setId(getIdForNextValue(TaskModel.class));
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
        App.initRealm();
        long taskListId = App.realm.where(ListModel.class).equalTo("name", taskListName).findFirst().getId();
        insertItems(text,done,important,taskListId);
    }

    public void insertItems(String text){
        insertItems(text, false, false, 0);
    }

    public static void setDoneByItemsId(boolean b, long id){
        App.initRealm();
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaskModel items = realm.where(TaskModel.class).equalTo("id", id).findFirst();
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
