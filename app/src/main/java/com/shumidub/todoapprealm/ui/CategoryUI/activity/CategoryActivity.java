package com.shumidub.todoapprealm.ui.CategoryUI.activity;

import android.content.Intent;
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
    Switch swDefault;
    Switch swCycling;
    Switch swDragon;
    TextView tvTaskCountValue;

    ExpandableListView expandableListView;

    static SimpleExpandableListAdapter simpleExpandableListAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        findViews();
        setEmptyStateIfCategoriesEmpty();

        categoriesAndListsAdapter = new CategoriesAndListsAdapter(this);
        simpleExpandableListAdapter = categoriesAndListsAdapter.getAdapter();

        expandableListView.setAdapter(simpleExpandableListAdapter);
        expandableListView.setOnItemLongClickListener(getLongListener());
        expandableListView.setOnChildClickListener(getChildClickListener());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Categories");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



    }



    @Override
    protected void onResume() {
        super.onResume();
        simpleExpandableListAdapter.notifyDataSetChanged();
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
        swDefault = findViewById(R.id.switch_default);
        swCycling = findViewById(R.id.switch_cycling);
        swDragon = findViewById(R.id.switch_dragon);
        tvTaskCountValue = findViewById(R.id.task_value);
        tvTaskCountValue.setOnClickListener((v) -> onTaskValueClick(tvTaskCountValue));
    }

    private void setEmptyStateIfCategoriesEmpty(){
        if (categoriesIsEmpry()){
            (findViewById(R.id.tv_empty)).setVisibility(View.VISIBLE);
        }
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
                        Map<String, String> map = (Map<String, String>) (simpleExpandableListAdapter.getGroup(i));
                        textCategoryName = map.get(GROUPS);

                        subtitle = "Category";

                        actionMode = startActionMode(getCallback(CATEGORY_ACTIONMODE));

                    } catch (IndexOutOfBoundsException ignored) { }
                    Log.d("DEBUG_TAG", "onItemLongClick: parent");
                }else if (view.getId() == android.R.id.text1) {
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

                    Map<String, String> map = (Map<String, String>) (simpleExpandableListAdapter.getChild(i, i1));
                    String textListName = map.get(CHILDS);

                    if (!text.isEmpty() || !text.equals("")) {
                        TasksRealmController.insertItems(text, false, false, textListName);
                        et.setText("");
                    } else {
                        long listId;
                        if (!ListsRealmController.getTasksList(textListName).equals(null) ||
                                ListsRealmController.getTasksList(textListName) != null) {
                            listId = ListsRealmController.getTasksList(textListName).getId();
                        } else {
                            listId = 0;
                        }
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
    }
}
