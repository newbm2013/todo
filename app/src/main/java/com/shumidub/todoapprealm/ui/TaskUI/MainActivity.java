package com.shumidub.todoapprealm.ui.TaskUI;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.ui.CategoryUI.CategoryActivity;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container =  findViewById(R.id.container);
        fragmentManager = getSupportFragmentManager();
        if (!savedInstanceState.isEmpty()
                && savedInstanceState.getLong("textListId", 0) != 0){
            fragmentManager.beginTransaction().replace(R.id.container,
                    ItemsFragment.newInstance(savedInstanceState.getLong("textListId"))).commit();
        }
        else{
            fragmentManager.beginTransaction().replace(R.id.container, new ItemsFragment()).commit();
        }

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

