package com.shumidub.todoapprealm.ui.fragment.task_section.small_tasks_fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.shumidub.todoapprealm.realmcontrollers.FolderTaskRealmController;
import com.shumidub.todoapprealm.realmmodel.FolderTaskObject;

import java.util.ArrayList;

import io.realm.RealmList;

/**
 * Created by Артем on 16.01.2018.
 */

public class SmallTaskFragmentPagerAdapter extends FragmentPagerAdapter {


    RealmList<FolderTaskObject> folderTaskModel;
    ArrayList <Long> folderTaskModelModelId = new ArrayList<>();

    public SmallTaskFragmentPagerAdapter(FragmentManager fm) {

        super(fm);
        setTaskList();
    }

    @Override
    public Fragment getItem(int position) {
        return SmallTasksFragment.newInstance (folderTaskModelModelId.get(position));
    }

    @Override
    public int getCount() {
        return folderTaskModelModelId.size();
    }


    //есть реалм результ фолдеров
    // для размера результа - создаем лист только с ид
    // todo для чего это?
    private void setTaskList(){
        folderTaskModel = FolderTaskRealmController.getFoldersList();
        for (FolderTaskObject folderObject : folderTaskModel){
            folderTaskModelModelId.add(folderObject.getId());
        }
    }



//    public void  notifyTaskListChanged(){
//        setTaskList();
//        notifyDataSetChanged();
//    }

    @Override
    public void notifyDataSetChanged() {
        setTaskList();
        super.notifyDataSetChanged();
    }


}
