package com.shumidub.todoapprealm.ui.TaskUI.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shumidub.todoapprealm.model.ListModel;
import com.shumidub.todoapprealm.model.TaskModel;
import com.shumidub.todoapprealm.realmcontrollers.ListsRealmController;
import com.shumidub.todoapprealm.ui.TaskUI.adapter.TasksRecyclerViewAdapter;
import com.shumidub.todoapprealm.ui.TaskUI.fragments.SmallTasksFragment;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by Артем on 16.01.2018.
 */

public class SmallTaskFragmentPagerAdapter extends FragmentPagerAdapter {

    RealmResults<ListModel> listModels;
    ArrayList <Long> listModelId = new ArrayList<>();









    public SmallTaskFragmentPagerAdapter(FragmentManager fm) {

        super(fm);


       listModels = ListsRealmController.getLists();
       for (ListModel listModel: listModels){
           listModelId.add(listModel.getId());
       }



    }

    @Override
    public Fragment getItem(int position) {
        return SmallTasksFragment.newInstance (listModelId.get(position));
    }

    @Override
    public int getCount() {
        return listModelId.size();
    }
}
