package com.shumidub.todoapprealm.realmcontrollers;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.model.CategoryModel;
import com.shumidub.todoapprealm.model.ListModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * Created by Артем on 24.12.2017.
 */

public class CategoriesRealmController {

    private static RealmQuery<CategoryModel> categoriesQuery;
    private static List<CategoryModel> categories;



    public static RealmQuery<CategoryModel> getcategoryQuery(){
        App.initRealm();
//        if (listsQuery == null){..below..}
        categoriesQuery = App.realm.where(CategoryModel.class);
        return categoriesQuery;
    }

    public static List<CategoryModel>  getCategories(){
        categories = getcategoryQuery().findAll();
        return categories;
    }

    public static void addCategory(String name){
        App.initRealm();
        App.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CategoryModel item = realm.createObject(CategoryModel.class);
                item.setId(TasksRealmController.getIdForNextValue(CategoryModel.class));
                item.setName(name);
                realm.insert(item);
            }
        });
    }
}
