package com.shumidub.todoapprealm.ui.activity.main_activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.shumidub.todoapprealm.ui.fragment.custom_fragment.CustomFragment;
import com.shumidub.todoapprealm.ui.fragment.folder_panel_sliding_fragment.FolderSlidingPanelFragment;

/**
 * Created by user on 12.01.18.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1){
            return new FolderSlidingPanelFragment();
        }
        else {
            return new CustomFragment();
        }
    }
}