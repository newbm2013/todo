package com.shumidub.todoapprealm.model;

import android.util.Log;

import com.shumidub.todoapprealm.TodoApp;
import com.shumidub.todoapprealm.bd.ItemsBD;
import com.shumidub.todoapprealm.ui.ItemsFragment;
import com.shumidub.todoapprealm.ui.ItemsRecyclerViewAdapter;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.shumidub.todoapprealm.TodoApp.TAG;

/**
 * Created by Артем on 21.12.2017.
 */

public class RealmController {

   static RealmController realmController = new RealmController();
   static Realm realm;


   public static RealmController getRealmController(){
       if (realm == null){
           realm = Realm.getDefaultInstance();
       }

       return realmController;
   }



    public List<ItemsBD> getItems(){
        if (realm == null) realm = Realm.getDefaultInstance();
        Log.d(TAG, "getItems: " + realm.hashCode());
        return realm.where(ItemsBD.class).findAll().sort("done", Sort.ASCENDING, "id",Sort.ASCENDING);
    }

    public void insertItems(String text, boolean done ){
        if (realm == null) realm = Realm.getDefaultInstance();
        Log.d(TAG, "getItems: " + realm.hashCode());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ItemsBD items = realm.createObject(ItemsBD.class);
                items.setId(getIdForNextValue(ItemsBD.class));
                items.setDone(done);
                items.setText(text);
                realm.insert(items);
            }
        });
    }

    public void insertItems(String text){
        insertItems(text, false);
    }

    public void setDoneByItemsId(boolean b, long id){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ItemsBD items = realm.where(ItemsBD.class).equalTo("id", id).findFirst();
                items.setDone(b);

            }
        });
    }

    private long getIdForNextValue(Class bdClass){
        long id =  System.currentTimeMillis();
       //todo need get max and return max + 1
        return id;
    }

    public void closeRealm(){
        Log.d(TAG, "closeRealm: " + realm.hashCode());
        if (realm != null) realm.close();
    }



}
