package com.shumidub.todoapprealm.ui.CategoryUI.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.model.CategoryModel;
import com.shumidub.todoapprealm.realmcontrollers.CategoriesRealmController;
import com.shumidub.todoapprealm.realmcontrollers.ListsRealmController;
import com.shumidub.todoapprealm.realmcontrollers.TasksRealmController;
import com.shumidub.todoapprealm.ui.CategoryUI.actionmode.ActionModeCategoryCallback;
import com.shumidub.todoapprealm.ui.CategoryUI.actionmode.ActionModeListCallback;
import com.shumidub.todoapprealm.ui.CategoryUI.dialog.DialogAddEditDelCategory;
import com.shumidub.todoapprealm.ui.CategoryUI.adapter.CategoriesAndListsAdapter;
import com.shumidub.todoapprealm.ui.TaskUI.MainActivity;

import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.shumidub.todoapprealm.realmcontrollers.CategoriesRealmController.categoriesIsEmpry;
import static com.shumidub.todoapprealm.ui.CategoryUI.adapter.CategoriesAndListsAdapter.CHILDS;
import static com.shumidub.todoapprealm.ui.CategoryUI.adapter.CategoriesAndListsAdapter.GROUPS;

public class CategoryActivity extends AppCompatActivity {

    EditText et;
    TextView tvTaskCountValue;
    TextView tvTaskPriority;
    TextView tvTaskCycling;

    ExpandableListView expandableListView;

    SimpleExpandableListAdapter simpleExpandableListAdapter;
    AdapterView.OnItemLongClickListener longListener;
    ExpandableListView.OnChildClickListener childClickListener;
    CategoriesAndListsAdapter categoriesAndListsAdapter;

    ActionMode actionMode;

    ActionMode.Callback categoryCallback;
    ActionMode.Callback listCallback;

    public static String textCategoryName;
    public static String titleList;
    public static long idCategory;
    public static String listName;
    public static long listId;

    static final int CATEGORY_ACTIONMODE = 1;
    static final int LIST_ACTIONMODE = 2;

    public static Long idOnTag;
    int expandedGroup = -1;

    int priority = 0;
    boolean cycling = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        findViews();
        setEmptyStateIfCategoriesEmpty();



        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Categories");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        expandableListView.setOnItemLongClickListener(getLongListener());
        expandableListView.setOnChildClickListener(getChildClickListener());

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {

                Log.d("DTAG", "onGroupExpand: " +i);
                expandedGroup = i;


//
//                if (expandedGroup !=-1 && expandedGroup!=i) expandableListView.collapseGroup(expandedGroup);
//
//                expandableListView.setSelectedGroup(i);


            }
        });
//
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {

//                TextView text = view.findViewById(R.id.parent_text1);
//
//                if (expandableListView.isGroupExpanded(i)) {
//                    view.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                    text.setTextColor(Color.BLACK);
//                }else{
//                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLigth));
//                    text.setTextColor(Color.WHITE);
//                }

//                int childCount =  ListsRealmController.getListsByCategoryId(((Pair<String,Long>)view.getTag()).second).size();
//
//                if (childCount>0 && expandedGroup !=-1 && expandedGroup!=i){
//                    expandableListView.collapseGroup(expandedGroup);
//                    expandedGroup = i;
//                    expandableListView.setSelectedGroup(expandedGroup);
//                }else expandedGroup =i;
//
//
//
//                Log.d("DTAG", "onGroupClick: " +expandedGroup);

                return false;
            }
        });




    }


    @Override
    protected void onResume() {


        categoriesAndListsAdapter = new CategoriesAndListsAdapter(this);
        simpleExpandableListAdapter = categoriesAndListsAdapter.getAdapter();
        expandableListView.setAdapter(simpleExpandableListAdapter);
        setEmptyStateIfCategoriesEmpty();


        //need scroll to child or catogory by id from child or better normal notify data set changed without on resume ...
//        if (expandedGroup!=-1){
//            expandableListView.expandGroup(expandedGroup);
//            expandableListView.setSelectedGroup(expandedGroup);
//        }



        super.onResume();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem addCategory = menu.add("add category");

        addCategory.setOnMenuItemClickListener((MenuItem a) -> {
           (new DialogAddEditDelCategory()).show(getSupportFragmentManager(), "category");
           return true;
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void findViews(){
        expandableListView = findViewById(R.id.expandedable_list_view);
        et = findViewById(R.id.et);

        tvTaskCycling = findViewById(R.id.task_cycling);
        tvTaskPriority = findViewById(R.id.task_priority);

        tvTaskCountValue = findViewById(R.id.task_value);
        tvTaskCountValue.setOnClickListener((v) -> onTaskValueClick(tvTaskCountValue));

        tvTaskPriority.setOnClickListener((v) -> onTaskPriorityClick(tvTaskPriority));
        tvTaskCycling.setOnClickListener((v) -> onTaskCyclingClick(tvTaskCycling));
    }

    private void setEmptyStateIfCategoriesEmpty(){
        if (categoriesIsEmpry()){
            (findViewById(R.id.tv_empty)).setVisibility(View.VISIBLE);
        } else (findViewById(R.id.tv_empty)).setVisibility(View.INVISIBLE);
    }

    private AdapterView.OnItemLongClickListener getLongListener(){
        if (longListener == null) {
            longListener = (adapterView, view, i,l) -> {

                String type = ((Pair<String, Long>) view.getTag()).first;
                idOnTag = ((Pair<String, Long>) view.getTag()).second;
                Log.d("DEBUG_TAG", "onItemLongClick: view " + type + " /" + idOnTag );

                String subtitle = "";

                if (view.getId() == R.id.parent_text1) {
                    try {
                        CategoryModel categoty = CategoriesRealmController.getCategory(idOnTag);
                        textCategoryName =categoty.getName();

                        subtitle = "Category";

                        actionMode = startActionMode(getCallback(CATEGORY_ACTIONMODE));

                    } catch (IndexOutOfBoundsException ignored) { }
                    Log.d("DEBUG_TAG", "onItemLongClick: parent  index out");
                }else if (view.getId() == R.id.child_text1) {
                        try {
                        titleList = ListsRealmController.getListById(idOnTag).getName();
                        actionMode = null;
                        subtitle = "List";
                        actionMode = startActionMode(getCallback(LIST_ACTIONMODE));



                    } catch (IndexOutOfBoundsException ignored) { }
                }


                actionMode.setSubtitle(subtitle);

                return true;
            };
        }
        return longListener;
    }

    private ActionMode.Callback getCallback(int callbackType){
         if(callbackType == CATEGORY_ACTIONMODE){
//             if ( categoryCallback == null) {
             categoryCallback = new ActionModeCategoryCallback().getCategoryActionModeCallback(this, idOnTag);
             return categoryCallback;
         } else if (callbackType == LIST_ACTIONMODE) {
//             if ( listCallback == null) {
             listCallback = new ActionModeListCallback().getListActionModeCallback(this, idOnTag);
             return listCallback;
         }
         else return null;
    }

    private ExpandableListView.OnChildClickListener getChildClickListener(){
        if (childClickListener == null) {
            childClickListener = new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                    String text = et.getText().toString();
                    int count = Integer.valueOf(tvTaskCountValue.getText().toString());




                    if (!text.isEmpty() || !text.equals("")) {
                        TasksRealmController.addTask(text, false, count , cycling, priority,
                                ((Pair<String, Long>) view.getTag()).second );
                        priority = 0;
                        cycling = false;
                        et.setText("");
                    } else {
                        long listId = ((Pair<String, Long>) view.getTag()).second;
                        Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("textId", listId);
                        startActivity(intent);
                    }
                    return false;
                }
            };
        }
        return childClickListener;
    }

    public void onTaskValueClick(View view) {
        int i = Integer.valueOf(tvTaskCountValue.getText().toString());
        if (i<10){
            i++;
        }else if (i>9){
            i=1;
        }
        ((TextView) view).setText("" + i);

        if (i<2) ((TextView) view).setTextColor(getResources().getColor(R.color.colorWhite));
        else ((TextView) view).setTextColor(getResources().getColor(R.color.colorAccent));

    }

    public void onTaskPriorityClick(View view) {

        if (priority>2) priority =0;
        else priority ++;

        if (priority>1){
            String text = "!";
            int i = priority;
            while (i>1){
                text +="!";
                i--;
            }
            ((TextView) view).setText(text);
        } else ((TextView) view).setText("!");

        if (priority>0) ((TextView) view).setTextColor(getResources().getColor(R.color.colorAccent));
        else ((TextView) view).setTextColor(getResources().getColor(R.color.colorWhite));


    }

    public void onTaskCyclingClick(View view) {

        cycling = !cycling;

        if (cycling) ((TextView) view).setTextColor(getResources().getColor(R.color.colorAccent));
        else ((TextView) view).setTextColor(getResources().getColor(R.color.colorWhite));

    }




    public void dataChanged(){
//

        onResume();

    }

    public void finishActionMode(){
        if (actionMode!=null) actionMode.finish();
    }
}
