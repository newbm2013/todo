package com.shumidub.todoapprealm.realmcontrollers;

import android.os.Build;
import android.util.Log;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.model.CategoryModel;
import com.shumidub.todoapprealm.model.ListModel;
import com.shumidub.todoapprealm.model.TaskModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Артем on 24.12.2017.
 */

public class CategoriesRealmController {

    private static RealmQuery<CategoryModel> categoriesQuery;
    private static List<CategoryModel> categories;


    public static RealmQuery<CategoryModel> getcategoriesQuery(){
        App.initRealm();
//        if (listsQuery == null){..below..}
        categoriesQuery = App.realm.where(CategoryModel.class);
        return categoriesQuery;
    }

    public static List<CategoryModel>  getCategories(){
        categories = getcategoriesQuery().findAll();
        return categories;
    }

    public static boolean categoriesIsEmpry(){
        App.initRealm();
        return getcategoriesQuery().findAll().isEmpty();
    }

    public static long addCategory(String name){
        long idAddedindCategory = TasksRealmController.getIdForNextValue(CategoryModel.class);
        App.initRealm();
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CategoryModel item = realm.createObject(CategoryModel.class);
                item.setId(idAddedindCategory);
                item.setName(name);
                realm.insert(item);
            }
        });

        return idAddedindCategory;
    }

    public static void editCategory(String oldName, String newName){
        App.initRealm();
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                CategoryModel task = getcategoriesQuery().equalTo("name", oldName).findFirst();
//                CategoryModel copyTask = App.realm.copyFromRealm(task);
                task.setName(newName);
//                copyTask.setName(name);
//                realm.insertOrUpdate(task);
//                task.deleteFromRealm();
            }
        });
    }

    public static void deleteCategoryAndChilds(String name){
        App.initRealm();
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CategoryModel category = getcategoriesQuery().equalTo("name", name).findFirst();
                long idCategory = category.getId();
                RealmResults<ListModel> realmResultsListsModel = ListsRealmController.getListsByCategoryId(idCategory);
                ArrayList<Long> arrayList = new ArrayList<>();
                for (ListModel list: realmResultsListsModel){
                    List <TaskModel> tasks = TasksRealmController.getTasks(list.getId());
                    for (TaskModel task: tasks){
                        TasksRealmController.deleteTask(task);
                    }
                }


                Map<String,Long> mapLists = new HashMap<>();


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    realmResultsListsModel.forEach(listModel -> {
                        String nameList = listModel.getName();
                        long idList = listModel.getId();
                        mapLists.put(nameList, idList);
                    });
                }

                realmResultsListsModel.deleteAllFromRealm();

                for (Map.Entry<String,Long> o : mapLists.entrySet()){

                    long idList = o.getValue();
                    String nameList = o.getKey();

                    if (!ListsRealmController.listIsExist(idList)){
                        Log.d("DEBUG_TAG", "LIST: " +nameList +" id: " + idList + " DELETED" );
                    }else{
                        Log.d("DEBUG_TAG", "LIST: " +nameList +" id: " + idList + " NOT DELETED !!!" );
                    }
                }



                category.deleteFromRealm();

                App.initRealm();
                if (!category.isValid()){
                    Log.d("DEBUG_TAG", "CATEGORY DELETED" );
                }else{
                    Log.d("DEBUG_TAG", "CATEGORY NOT DELETED !!!" );
                }
        }
        });
    }
}
