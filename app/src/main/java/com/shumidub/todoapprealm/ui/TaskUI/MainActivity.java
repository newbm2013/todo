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
import com.shumidub.todoapprealm.sharedpref.SharedPrefHelper;
import com.shumidub.todoapprealm.ui.CategoryUI.activity.CategoryActivity;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FrameLayout container;

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

        Log.d("DT", "onCreate: " + intent);

        long listId;
//        if (getIntent() == null) listId = defaultListId;
//        else
        listId= intent.getLongExtra("textId", defaultListId);

        Log.d("DT", "dli " + defaultListId);



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


}

