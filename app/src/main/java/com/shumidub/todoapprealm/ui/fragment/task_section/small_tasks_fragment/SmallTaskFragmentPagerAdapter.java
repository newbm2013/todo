package com.shumidub.todoapprealm.ui.fragment.task_section.small_tasks_fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.realmcontrollers.taskcontroller.FolderTaskRealmController;
import com.shumidub.todoapprealm.realmmodel.task.FolderTaskObject;

import java.util.ArrayList;

import io.realm.RealmList;

/**
 * Created by Артем on 16.01.2018.
 */

public class SmallTaskFragmentPagerAdapter extends FragmentPagerAdapter {


    public SmallTaskFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        ArrayList<Long> arrayList = new ArrayList<>();

        for (int i = 0; i<App.folderOfTasksListFromContainer.size(); i++){
            arrayList.add(App.folderOfTasksListFromContainer.get(i).getId());
        }

        Log.d("DTAG2425", "folderIdArray = : " + arrayList.toString());

        long id = App.folderOfTasksListFromContainer.get(position).getId();

        Log.d("DTAG2425", "getItem: folderID = " + id);
        Log.d("DTAG2425", " ");
        return SmallTasksFragment.newInstance (id);
    }

    @Override
    public int getCount() {
        return App.folderOfTasksListFromContainer.size();
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        //todo What?

    }
}
