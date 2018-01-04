package com.shumidub.todoapprealm.realmcontrollers;

import android.util.Log;

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

//   public static List<TaskModel> getItems(){
//        App.initRealm();
//        return App.realm.where(TaskModel.class).findAll().sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
//    }

//    public static List<TaskModel> getItems(long listId){
//        App.initRealm();
//        return App.realm.where(TaskModel.class).equalTo("taskListId", listId).findAll().sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
//    }

    public static List<TaskModel> getTasks(){
        App.initRealm();
        return App.realm.where(TaskModel.class)
                .findAll()
                .sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }

    public static List<TaskModel> getNotDoneTasks(){
        App.initRealm();
        return App.realm.where(TaskModel.class)
                .equalTo("done", false)
                .findAll()
                .sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }

    public static List<TaskModel> getDoneTasks(){
        App.initRealm();
        return App.realm.where(TaskModel.class)
                .equalTo("done", true)
                .findAll()
                .sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }

    public static List<TaskModel> getTasks(long listId){
        App.initRealm();
        return App.realm.where(TaskModel.class)
                .equalTo("taskListId", listId)
                .findAll()
                .sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }

    public static List<TaskModel> getNotDoneTasks(long listId){
        App.initRealm();
        return App.realm.where(TaskModel.class)
                .equalTo("taskListId", listId)
                .equalTo("done", false)
                .findAll()
                .sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }

    public static List<TaskModel> getDoneTasks(long listId){
        App.initRealm();
        return App.realm.where(TaskModel.class)
                .equalTo("taskListId", listId)
                .equalTo("done", true)
                .findAll()
                .sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }

    public static  void insertItems(String text, boolean done, boolean important, long taskListId ){
        App.initRealm();
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaskModel item = realm.createObject(TaskModel.class);
                long id = getIdForNextValue(TaskModel.class);
                item.setId(id);
                item.setText(text + id);
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

    public static void setTaskDone(TaskModel task, boolean done){
        App.initRealm();
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                task.setDone(done);
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


    public static void deleteTask(long taskId){
        App.initRealm();
        TaskModel task = App.realm.where(TaskModel.class).equalTo("id", taskId).findFirst();
        String taskText = task.getText();
        task.deleteFromRealm();
        if (App.realm.where(TaskModel.class).equalTo("id", taskId).findFirst() == null){
            Log.d("DEBUG_TAG", "TASK: " + taskText + " id:" + taskId + " DELETED" );
        }else{
            Log.d("DEBUG_TAG", "TASK: " + taskText + " id:" + taskId + " NOT DELETED !!!" );
        }
    }

    public static void deleteTask(TaskModel task){
        App.initRealm();
        long taskId = task.getId();
        String taskText = task.getText();
        task.deleteFromRealm();
        if (App.realm.where(TaskModel.class).equalTo("id", taskId).findFirst() == null){
            Log.d("DEBUG_TAG", "TASK: " + taskText + " id:" + taskId + " DELETED" );
        }else{
            Log.d("DEBUG_TAG", "TASK: " + taskText + " id:" + taskId + " NOT DELETED !!!" );
        }
    }

}
