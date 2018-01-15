package com.shumidub.todoapprealm.ui;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.ui.TaskUI.adapter.MainPagerAdapter;
import com.shumidub.todoapprealm.ui.TaskUI.fragments.TasksFragment;
import com.shumidub.todoapprealm.ui.TaskUI.fragments.TasksFragment2;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    long time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.closeRealm();
    }

    @Override
    protected void onRestart() {
//        /**Проверка на возможность загрузить список при возврате на экран.
//        * Например, после ухода с экрана, категория со списком могла быть удалена и при возврате на
//        * экран и тапе на список - была ошибка.
//        */
//        if (ListsRealmController.getListById(listId)==null){
//
//            long defaultListId = new SharedPrefHelper(this).getDefaultListId();
//            if (ListsRealmController.getListById(defaultListId)!=null)  listId = defaultListId;
//            else listId = 0;
//            fragmentManager.beginTransaction().replace(R.id.container,
//                    TasksFragment.newInstance(listId)).commitAllowingStateLoss();
////          try fix run method before savedInstance outStatte
////          TasksFragment.newInstance(listId)).commit();
//        }
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        int currentFragmentItem = viewPager.getCurrentItem();
        if (currentFragmentItem == 1){
            for (Fragment fragment: getSupportFragmentManager ().getFragments()){
                if (fragment instanceof TasksFragment){
                    SlidingUpPanelLayout slidingUpPanelLayout = ((TasksFragment) fragment).slidingUpPanelLayout;
                    if ( slidingUpPanelLayout.getPanelState()== SlidingUpPanelLayout.PanelState.EXPANDED){
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        return;
                    } else{
                        onBackPressedWithTimer();
                        return;
                    }
                }
            }
        }if (currentFragmentItem == 2){
            for (Fragment fragment: getSupportFragmentManager ().getFragments()){
                if (fragment instanceof TasksFragment2){
                    SlidingUpPanelLayout slidingUpPanelLayout = ((TasksFragment2) fragment).slidingUpPanelLayout;
                    if ( slidingUpPanelLayout.getPanelState()== SlidingUpPanelLayout.PanelState.EXPANDED){
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        return;
                    } else{
                        onBackPressedWithTimer();
                        return;
                    }
                }
            }
        }else{
            onBackPressedWithTimer();
        }
    }

    private void onBackPressedWithTimer(){
        if (time!=0 && System.currentTimeMillis() - time<2000) super.onBackPressed();
        else{
            time = System.currentTimeMillis();
            Toast.makeText(this, "For exit press again", Toast.LENGTH_SHORT).show();
            Log.d("DTAG", "onBackPressedWithTimer: ");
        }
    }
}