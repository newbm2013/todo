package com.shumidub.todoapprealm.ui.CategoryUI.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shumidub.todoapprealm.R;

public class CategoryActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
    }


    @Override
    protected void onResume() {

//
//        categoriesAndListsAdapter = new CategoriesAndListsAdapter(this);
//        simpleExpandableListAdapter = categoriesAndListsAdapter.getAdapter();
//        expandableListView.setAdapter(simpleExpandableListAdapter);
//        setEmptyStateIfCategoriesEmpty();


        //need scroll to child or catogory by id from child or better normal notify data set changed without on resume ...


        super.onResume();
    }







}
