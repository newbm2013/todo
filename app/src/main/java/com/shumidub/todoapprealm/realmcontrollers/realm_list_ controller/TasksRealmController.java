package com.shumidub.todoapprealm.realmcontrollers;

import android.util.Log;
import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.realmmodel.FolderObject;
import com.shumidub.todoapprealm.realmmodel.TaskObject;
import java.util.Calendar;
import java.util.List;
import io.reactivex.annotations.NonNull;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;

/**
 * Created by Артем on 21.12.2017.
 */

public class TasksRealmController {

    //GET TASKS
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
    public static RealmList<TaskObject> getTasks(long folderId){
        App.initRealm();
        //todo !!! new need test + need think and add about sort

//        return App.realm.where(TaskObject.class)
//                .equalTo("taskFolderId", folderId)
//                .findAll()
//                .sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);

   return ((FolderObject) App.realm.where(FolderObject.class)
                  .equalTo("id", folderId).findFirst()).folderTasks;
    }

    /** get not done tasks by id*/
    public static List<TaskObject> getNotDoneTasks(long folderId){
        App.initRealm();
        return App.realm.where(TaskObject.class)
                .equalTo("taskFolderId", folderId)
                .equalTo("done", false)
                .findAll()
                .sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }

    /** get done tasks by id*/
    public static List<TaskObject> getDoneTasks(long folderId){
        App.initRealm();
        return App.realm.where(TaskObject.class)
                .equalTo("taskFolderId", folderId)
                .equalTo("done", true)
                .findAll()
                .sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }

    /** get done and not done tasks but where countAccumulation more than 0 */
    public static List<TaskObject> getDoneAndPartiallyDoneTasks(long folderId){
        App.initRealm();
        return App.realm.where(TaskObject.class)
                .equalTo("taskFolderId", folderId)
                .notEqualTo("countAccumulation", 0)
                .findAll()
                .sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }

    //SINGLE TASK
    /** get task by id*/
    public static TaskObject getTask(long idTask){
        App.initRealm();
        return App.realm.where(TaskObject.class).equalTo("id", idTask).findFirst();
    }

    /** add task*/
    public static  void addTask(String text, int count, int maxAccumulation, boolean cycling, int priority, long taskFolderId ){
        App.initRealm();
        App.realm.executeTransaction((transaction) -> {
            TaskObject task = App.realm.createObject(TaskObject.class);
            long id = getIdForNextValue();
            task.setId(id);
            task.setText(text);
            task.setLastDoneDate(0);
            task.setPriority(priority);
            task.setTaskFolderId(taskFolderId);
            task.setCountValue(count);
            task.setMaxAccumulation(maxAccumulation);
            task.setCountAccumulation(0);
            task.setCycling(cycling);
            App.realm.insert(task);
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
        App.realm.executeTransaction((transaction)-> {
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
        });
    }

    /**delete task*/
    public static void deleteTask(TaskObject task) {
        App.initRealm();
        long taskId = task.getId();
        String taskText = task.getText();

        if (App.realm.isInTransaction()) {
            task.getDateCountAccumulation().clear();
            task.deleteFromRealm();
        } else {
            App.realm.executeTransaction((transaction) -> {
                task.getDateCountAccumulation().clear();
                task.deleteFromRealm();
            });
        }

        if (App.realm.where(TaskObject.class).equalTo("id", taskId).findFirst() == null){
            Log.d("DEBUG_TAG", "TASK: " + taskText + " id:" + taskId + " DELETED" );
        }else{
            Log.d("DEBUG_TAG", "TASK: " + taskText + " id:" + taskId + " NOT DELETED !!!" );
        }
    }

    /**delete task by id*/
    public static void deleteTask(long id){
        deleteTask(App.realm.where(TaskObject.class).equalTo("id", id).findFirst());
    }

    /** get unique id*/
    private static long getIdForNextValue(){
        long id =  System.currentTimeMillis();
        while ((App.realm.where(TaskObject.class).equalTo("id", id)).findFirst()!=null){
            id ++;
        }
        return id;
    }
}
