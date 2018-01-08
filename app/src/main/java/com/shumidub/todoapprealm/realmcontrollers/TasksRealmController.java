package com.shumidub.todoapprealm.realmcontrollers;

import android.util.Log;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.model.ListModel;
import com.shumidub.todoapprealm.model.TaskModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.annotations.NonNull;
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

    public static TaskModel getTask(long idTask){
        App.initRealm();
        return App.realm.where(TaskModel.class).equalTo("id", idTask).findFirst();
    }

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

    public static  void addTask(String text, boolean done, int count, boolean cycling, int priority, long taskListId ){
        App.initRealm();
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaskModel task = realm.createObject(TaskModel.class);
                long id = getIdForNextValue(TaskModel.class);
                task.setId(id);
//              item.setText(text + id);
                task.setText(text);
                task.setDone(done);
                task.setLastDoneDate(0);
                task.setPriority(priority);
                task.setTaskListId(taskListId);
                task.setCountValue(count);
                task.setCycling(cycling);
                realm.insert(task);
            }
        });
    }


    public static  void editTask(TaskModel task, String text, @NonNull int count, @NonNull boolean cycling, @NonNull int priority ){
        App.initRealm();
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (!text.isEmpty()) task.setText(text);
                task.setPriority(priority);
                task.setCountValue(count);
                task.setCycling(cycling);
            }
        });
    }



    public static void setTaskDone(TaskModel task, boolean done){
        App.initRealm();
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                task.setDone(done);
                if (done) {
                    Calendar cal = Calendar.getInstance();
                    String date = "" + cal.get(Calendar.DAY_OF_YEAR) + cal.get(Calendar.YEAR);
                    task.setLastDoneDate(Integer.valueOf(date));
                }
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

        if (App.realm.isInTransaction()) task.deleteFromRealm();
        else {
            App.realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    task.deleteFromRealm();
                }
            });
        }


        if (App.realm.where(TaskModel.class).equalTo("id", taskId).findFirst() == null){
            Log.d("DEBUG_TAG", "TASK: " + taskText + " id:" + taskId + " DELETED" );
        }else{
            Log.d("DEBUG_TAG", "TASK: " + taskText + " id:" + taskId + " NOT DELETED !!!" );
        }
    }

}
