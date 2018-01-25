package com.shumidub.todoapprealm.ui.activity.mainactivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.ui.actionmode.EmptyActionMode;
import com.shumidub.todoapprealm.ui.fragment.lists_and_sliding_fragment.TasksFragment;
import com.shumidub.todoapprealm.ui.fragment.small_tasks_fragment.SmallTasksFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    long time = 0;
    ActionBar actionBar;
    MainPagerAdapter mainPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();

        viewPager = findViewById(R.id.viewpager);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainPagerAdapter);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==1){
                    actionBar.setTitle(TasksFragment.getTitle());
                }else {
                    actionBar.setTitle("Notes/Reports");
                }
                ActionMode actionMode = startSupportActionMode(new EmptyActionMode());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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