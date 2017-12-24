package com.shumidub.todoapprealm.ui;

import android.content.res.Configuration;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.TodoApp;
import com.shumidub.todoapprealm.bd.ItemsBD;
import com.shumidub.todoapprealm.model.RealmController;

import io.realm.Realm;
import io.realm.RealmObject;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FrameLayout container2;
    FrameLayout container;
    ConstraintLayout cl;
    RealmController realmController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realmController = RealmController.getRealmController();
        Log.d(TodoApp.TAG, "onCreate: " + realmController.hashCode());

        cl = findViewById(R.id.constr);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, new ItemsFragment()).commit();

        fragmentManager.beginTransaction().replace(R.id.container2, new CategoriesFragment()).commit();

        container2 =  findViewById(R.id.container2);
        container =  findViewById(R.id.container);
        container2.setVisibility(View.INVISIBLE);

        cl.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                boolean someHasFocus = false;

                if(cl.hasFocus())
                    someHasFocus = true;

                if(someHasFocus){
                    if(bottom>oldBottom){
                        container2.setVisibility(View.INVISIBLE);
                        container.setVisibility(View.VISIBLE);

                    }else if(bottom<oldBottom){
                        container.setVisibility(View.INVISIBLE);
                        container2.setVisibility(View.VISIBLE);
                    }

                }else{
                    container2.setVisibility(View.INVISIBLE);
                    container.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TodoApp.TAG, "onDestroy: " );
        realmController.closeRealm();
    }
}

