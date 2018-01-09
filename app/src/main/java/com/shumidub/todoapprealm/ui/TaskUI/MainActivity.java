package com.shumidub.todoapprealm.ui.TaskUI;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.realmcontrollers.ListsRealmController;
import com.shumidub.todoapprealm.sharedpref.SharedPrefHelper;
import com.shumidub.todoapprealm.ui.CategoryUI.activity.CategoryActivity;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FrameLayout container;
    long listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container =  findViewById(R.id.container);
        fragmentManager = getSupportFragmentManager();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Tasks");
        long defaultListId = new SharedPrefHelper(this).getDefaultListId();
        Intent intent = getIntent();
        listId= intent.getLongExtra("textId", defaultListId);
        Log.d("DTAG", "onCreate: ");

        if (ListsRealmController.getListById(listId)==null) listId=0;

        fragmentManager.beginTransaction().replace(R.id.container,
                TasksFragment.newInstance(listId)).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("Category");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setIntent(new Intent(this, CategoryActivity.class));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.closeRealm();
    }

    @Override
    protected void onRestart() {

        /**Проверка на возможность загрузить список при возврате на экран.
        * Например, после ухода с экрана, категория со списком могла быть удалена и при возврате на
        * экран и тапе на список - была ошибка.
        */

        if (ListsRealmController.getListById(listId)==null){

            long defaultListId = new SharedPrefHelper(this).getDefaultListId();
            if (ListsRealmController.getListById(defaultListId)!=null)  listId = defaultListId;
            else listId = 0;

            fragmentManager.beginTransaction().replace(R.id.container,
                    TasksFragment.newInstance(listId)).commit();
        }

        super.onRestart();
    }
}