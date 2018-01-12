package com.shumidub.todoapprealm.ui.TaskUI.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shumidub.todoapprealm.ui.TaskUI.fragments.CustomFragment;
import com.shumidub.todoapprealm.ui.TaskUI.fragments.TasksFragment;
import com.shumidub.todoapprealm.ui.TaskUI.fragments.TasksFragment2;

/**
 * Created by user on 12.01.18.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1){
            return new TasksFragment();
        }
        else if (position == 2){
            return new TasksFragment2();
        }
        else {
            return new CustomFragment();
        }
    }
}
