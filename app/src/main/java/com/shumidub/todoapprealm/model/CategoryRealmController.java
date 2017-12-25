package com.shumidub.todoapprealm.model;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.bd.CategoryTasksListsBD;
import com.shumidub.todoapprealm.bd.ItemsBD;
import com.shumidub.todoapprealm.bd.TasksListBD;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Артем on 24.12.2017.
 */

public class CategoryRealmController  {

    public static List<CategoryTasksListsBD> getCategoryTasksList(){
        return App.realm.where(CategoryTasksListsBD.class).findAll();
    }

    public static List<TasksListBD> getTasksList(){
        return App.realm.where(TasksListBD.class).findAll();
    }

    public static List<TasksListBD> getTasksList(long idCategory){
        return App.realm.where(TasksListBD.class).equalTo("idCategory", idCategory).findAll();
    }


    public static TasksListBD getTasksList(String nameList){
        return App.realm.where(TasksListBD.class).equalTo("id", nameList).findFirst();
    }


    public static void addCategory(String name){
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CategoryTasksListsBD item = realm.createObject(CategoryTasksListsBD.class);
                item.setId(TasksRealmController.getIdForNextValue(CategoryTasksListsBD.class));
                item.setName(name);
                realm.insert(item);
            }
        });
    }

    public static void addTasksLists(String name,boolean isDefault, boolean isCycling, long idCategory ){
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TasksListBD item = realm.createObject(TasksListBD.class);
                item.setId(TasksRealmController.getIdForNextValue(TasksListBD.class));
                item.setName(name);
                item.setDefault(isDefault);
                item.setCycling(isCycling);
                item.setIdCategory(idCategory);
                realm.insert(item);
            }
        });
    }

    public static void addTasksLists(String name,boolean isDefault, boolean isCycling, String nameCategory ){
        long id = App.realm.where(CategoryTasksListsBD.class).equalTo("name", nameCategory).findFirst().getId();
        addTasksLists(name,isDefault,isCycling, id);
    }
}
