package com.shumidub.todoapprealm.realmcontrollers;

import android.util.Log;
import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.realmmodel.TaskObject;
import java.util.Calendar;
import java.util.List;
import io.reactivex.annotations.NonNull;
import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by Артем on 21.12.2017.
 */

public class TasksRealmController {

    /** get task by id*/
    public static TaskObject getTask(long idTask){
        App.initRealm();
        return App.realm.where(TaskObject.class).equalTo("id", idTask).findFirst();
    }

    /** get all tasks*/
    public static List<TaskObject> getTasks(){
        App.initRealm();
        return App.realm.where(TaskObject.class).findAll()
                .sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }

    /** get not done tasks*/
    public static List<TaskObject> getNotDoneTasks(){
        App.initRealm();
        return App.realm.where(TaskObject.class)
                .equalTo("done", false)
                .findAll()
                .sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }

    /** get done tasks*/
    public static List<TaskObject> getDoneTasks(){
        App.initRealm();
        return App.realm.where(TaskObject.class)
                .equalTo("done", true)
                .findAll()
                .sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }

    /** done and not done tasks but where countAccumulation more than 0 */
    public static List<TaskObject> getDoneAndPartiallyDoneTasks(){
        App.initRealm();
        return App.realm.where(TaskObject.class)
                .notEqualTo("countAccumulation", 0)
                .findAll()
                .sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }

    /** get tasks by folder id*/
    public static List<TaskObject> getTasks(long folderId){
        App.initRealm();
        return App.realm.where(TaskObject.class)
                .equalTo("taskListId", folderId)
                .findAll()
                .sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }

    public static List<TaskObject> getNotDoneTasks(long listId){
        App.initRealm();
        return App.realm.where(TaskObject.class)
                .equalTo("taskListId", listId)
                .equalTo("done", false)
                .findAll()
                .sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }


    public static List<TaskObject> getDoneTasks(long listId){
        App.initRealm();
        return App.realm.where(TaskObject.class)
                .equalTo("taskListId", listId)
                .equalTo("done", true)
                .findAll()
                .sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }

    //done and not done tasks but where countAccumulation more than 0
    public static List<TaskObject> getDoneAndPartiallyDoneTasks(long listId){
        App.initRealm();
        return App.realm.where(TaskObject.class)
                .equalTo("taskListId", listId)
                .notEqualTo("countAccumulation", 0)
                .findAll()
                .sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }




    public static  void addTask(String text, int count, int maxAccumulation, boolean cycling, int priority, long taskListId ){
        App.initRealm();
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaskObject task = realm.createObject(TaskObject.class);
                long id = getIdForNextValue(TaskObject.class);
                task.setId(id);
//              item.setText(text + id);
                task.setText(text);
                task.setLastDoneDate(0);
                task.setPriority(priority);
                task.setTaskListId(taskListId);
                task.setCountValue(count);
                task.setMaxAccumulation(maxAccumulation);
                task.setCountAccumulation(0);
                task.setCycling(cycling);
                realm.insert(task);
            }
        });
    }


    public static  void editTask(TaskObject task, String text, @NonNull int count, @NonNull int maxAccumulation, @NonNull boolean cycling, @NonNull int priority ){
        App.initRealm();
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (!text.isEmpty()) task.setText(text);
                task.setPriority(priority);
                task.setCountValue(count);
                task.setMaxAccumulation(maxAccumulation);
                task.setCycling(cycling);
            }
        });
    }



    public static void setTaskDoneOrParticullaryDone(TaskObject task, boolean done){
        App.initRealm();
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                if(done == false){
                    task.setDone(done);
                    task.clearDateCountAccumulation();
                    task.setLastDoneDate(0);
                }

                if (done) {

                    Calendar cal = Calendar.getInstance();
                    int date = Integer.valueOf("" + cal.get(Calendar.DAY_OF_YEAR) + cal.get(Calendar.YEAR));

                    task.addDateCountAccumulation(date);
                    task.setLastDoneDate(date);

                    if (task.getCountAccumulation() >= task.getMaxAccumulation()){
                        task.setDone(done);
                    }
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

//
//    public static void deleteTask(long taskId){
//        App.initRealm();
//        TaskObject task = App.realm.where(TaskObject.class).equalTo("id", taskId).findFirst();
//        String taskText = task.getText();
//
//        task.deleteFromRealm();
//        if (App.realm.where(TaskObject.class).equalTo("id", taskId).findFirst() == null){
//            Log.d("DEBUG_TAG", "TASK: " + taskText + " id:" + taskId + " DELETED" );
//        }else{
//            Log.d("DEBUG_TAG", "TASK: " + taskText + " id:" + taskId + " NOT DELETED !!!" );
//        }
//    }

    public static void deleteTask(TaskObject task){
        App.initRealm();
        long taskId = task.getId();
        String taskText = task.getText();

        if (App.realm.isInTransaction()){
            task.getDateCountAccumulation().clear();
            task.deleteFromRealm();
        }
        else {
            App.realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    task.getDateCountAccumulation().clear();
                    task.deleteFromRealm();
                }
            });
        }


        if (App.realm.where(TaskObject.class).equalTo("id", taskId).findFirst() == null){
            Log.d("DEBUG_TAG", "TASK: " + taskText + " id:" + taskId + " DELETED" );
        }else{
            Log.d("DEBUG_TAG", "TASK: " + taskText + " id:" + taskId + " NOT DELETED !!!" );
        }
    }

}
