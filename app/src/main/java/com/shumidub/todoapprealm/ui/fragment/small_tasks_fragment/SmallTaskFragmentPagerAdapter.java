package com.shumidub.todoapprealm.ui.fragment.small_tasks_fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shumidub.todoapprealm.model.ListModel;
import com.shumidub.todoapprealm.realmcontrollers.ListsRealmController;

import java.util.ArrayList;

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
