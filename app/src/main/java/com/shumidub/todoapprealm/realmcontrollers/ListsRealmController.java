package com.shumidub.todoapprealm.realmcontrollers;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.model.CategoryModel;
import com.shumidub.todoapprealm.model.ListModel;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Артем on 24.12.2017.
 */

public class ListsRealmController {

    private static RealmQuery<ListModel> listsQuery;
    private static RealmResults<ListModel> lists;
    static long currentListId; //for TaskActivity
    static long defaultListId; //for TaskActivity, if getExtraLong=0 + when set defaultId, find other and set
    //false or better save in one olace


    public static RealmQuery<ListModel> getListsQuery(){
        App.initRealm();
//        if (listsQuery == null){...code bellow...}
            listsQuery = App.realm.where(ListModel.class);
        return listsQuery;
    }

    public static RealmResults<ListModel> getLists(){
        App.initRealm();
//        if (lists == null || lists.isEmpty()){...code bellow...}
            lists = getListsQuery().findAll();
        return lists;
    }

    public static RealmResults<ListModel> getListsByCategoryId(long categoryId){
        return getListsQuery().equalTo("idCategory", categoryId).findAll();
    }

    public static ListModel getListById(long listId){
        return getListsQuery().equalTo("id", listId).findFirst();
    }

    public static void setCurrentListId(long listId){
        currentListId = listId;
    }

    public static long getDefaultListId(long listId){
        return defaultListId = getListsQuery().equalTo("isDefault", true).findFirst().getId();
    }

    // need to delete
    public static ListModel getTasksList(String nameList){
//        Log.d(TAG, "getTasksList: "
//                + App.realm.toString()
//                + " "
//                + App.realm.where(ListModel.class).toString());
        App.initRealm();
        return App.realm.where(ListModel.class).equalTo("name", nameList).findFirst();
    }

    public static long addTasksLists(String name, boolean isDefault, boolean isCycling, long idCategory ){
        long id = TasksRealmController.getIdForNextValue(ListModel.class);
        App.initRealm();
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ListModel item = realm.createObject(ListModel.class);
                item.setId(id);
                item.setName(name + id);
                item.setCycling(isCycling);
                item.setIdCategory(idCategory);
                realm.insert(item);
//                if (isDefault) new SharedPrefHelper(null).setDefauiltListId(id);
            }
        });
        return id;
    }


    public static long editList(long id, String name, boolean isDefault, boolean isCycling, long idCategory ){

        App.initRealm();
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ListModel item = getListById(id);
                item.setName(name + id);
//              item.setCycling(isCycling);
//              if (idCategory != 0) item.setIdCategory(idCategory);
//
//              if (isDefault) new SharedPrefHelper(null).setDefauiltListId(id);
            }
        });
        return id;
    }

    public static void addTasksLists(String name,boolean isDefault, boolean isCycling, String nameCategory ){
        App.initRealm();
        long id = App.realm.where(CategoryModel.class).equalTo("name", nameCategory).findFirst().getId();
        addTasksLists(name,isDefault,isCycling, id);
    }

    public static void deleteLists(long idList){
        App.initRealm();
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ListModel item = getListsQuery().equalTo("id", idList).findFirst();
                item.deleteFromRealm();
            }
        });
    }

    public static boolean listIsExist(ListModel list){
        App.initRealm();
        return list.isValid();
    }

    public static boolean listIsExist(long idList){
        App.initRealm();
        if ( App.realm.where(ListModel.class).equalTo("id", idList).findFirst() == null){
            return false;
        }else {
            return App.realm.where(ListModel.class).equalTo("id", idList).findFirst().isValid();
        }
    }
}
