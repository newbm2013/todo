package com.shumidub.todoapprealm.realmcontrollers;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.model.ListModel;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Артем on 24.12.2017.
 */

public class ListsRealmController {

    private static RealmQuery<ListModel> listsQuery;
    private static RealmResults<ListModel> lists;


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


    // need to delete
    public static ListModel getTasksList(String nameList){
        App.initRealm();
        return App.realm.where(ListModel.class).equalTo("name", nameList).findFirst();
    }

    public static long addTasksLists(String name){
        long id = TasksRealmController.getIdForNextValue(ListModel.class);
        App.initRealm();
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ListModel item = realm.createObject(ListModel.class);
                item.setId(id);
                item.setName(name);
                realm.insert(item);
            }
        });
        return id;
    }


    public static long editList(long id, String name){
        App.initRealm();
        ListModel list = getListById(id);
        return editList(list, name);
    }


    public static long editList(ListModel list, String name){

        App.initRealm();
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                list.setName(name);
            }
        });
        return list.getId();
    }


    public static void deleteLists(ListModel list){
        App.initRealm();
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                list.deleteFromRealm();
            }
        });
    }

    public static void deleteLists(long idList){
        App.initRealm();
        ListModel list = getListsQuery().equalTo("id", idList).findFirst();
        deleteLists(list);
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

    public static boolean listsIsEmpty(){
        App.initRealm();
        return (App.realm.where(ListModel.class).findAll() == null
                || App.realm.where(ListModel.class).findAll().size() == 0);
    }
}
