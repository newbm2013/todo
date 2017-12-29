package com.shumidub.todoapprealm.ui.CategoryUI;

import android.content.Intent;
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
import com.shumidub.todoapprealm.ui.TaskUI.MainActivity;

import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.shumidub.todoapprealm.realmcontrollers.CategoriesRealmController.categoriesIsEmpry;
import static com.shumidub.todoapprealm.ui.CategoryUI.CategoriesAndListsAdapter.CHILDS;
import static com.shumidub.todoapprealm.ui.CategoryUI.CategoriesAndListsAdapter.GROUPS;

public class CategoryActivity extends AppCompatActivity {

    EditText et;
    Switch swDefault;
    Switch swCycling;
    Switch swDragon;
    TextView tvTaskCountValue;

    ExpandableListView expandableListView;

    SimpleExpandableListAdapter simpleExpandableListAdapter;
    AdapterView.OnItemLongClickListener longListener;
    ExpandableListView.OnChildClickListener childClickListener;
    CategoriesAndListsAdapter categoriesAndListsAdapter;

    ActionMode actionMode;
    ActionMode.Callback callback;

    public static String textCategoryName;
    public static long idCategory;

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
        return super.onOptionsItemSelected(item);
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
            longListener = new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (view.getId() == R.id.parent_text1) {
                        try{
                            Map<String, String> map = (Map<String, String>) (simpleExpandableListAdapter.getGroup(i));
                            textCategoryName = map.get(GROUPS);
                            actionMode = startActionMode(getActionModeCallback());
                        } catch (IndexOutOfBoundsException ignored){}
//                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        Log.d("DEBUG_TAG", "onItemLongClick: if");
                    }else{
                        Log.d("DEBUG_TAG", "onItemLongClick: else");
                    }
                    return true;
                }
            };
        }
        return longListener;
    }

    private ActionMode.Callback getActionModeCallback(){
        if (callback == null) {
            callback = new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    MenuItem addList = menu.add("add list to " + textCategoryName);
                    addList.setIcon(R.drawable.ic_launcher_foreground);
                    addList.setOnMenuItemClickListener((MenuItem a) -> {
                        DialogAddList dialogAddList = new DialogAddList();
                        dialogAddList.show(getSupportFragmentManager(), "addtocategory");
                        return true;
                    });

                    MenuItem editCategory = menu.add("edit ");
                    editCategory.setIcon(R.drawable.ic_launcher_foreground);
                    editCategory.setOnMenuItemClickListener((MenuItem a) -> {

                        DialogAddEditDelCategory editCategoryDialog =
                                DialogAddEditDelCategory.newInstance(textCategoryName, DialogAddEditDelCategory.EDIT_CATEGORY);
                        editCategoryDialog.show(getSupportFragmentManager(), "editcategory");
                        return true;
                    });


                    MenuItem deleteCategore = menu.add("delete ");
                    deleteCategore.setIcon(R.drawable.ic_launcher_foreground);
                    deleteCategore.setOnMenuItemClickListener((MenuItem a) -> {

                        DialogAddEditDelCategory deleteCategoryDialog =
                                DialogAddEditDelCategory.newInstance(textCategoryName, DialogAddEditDelCategory.DELETE_CATEGORY);
                        deleteCategoryDialog.show(getSupportFragmentManager(), "deletecategory");
                        return true;
                    });

                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    actionMode.setTitle(textCategoryName);
                    return false; }

                @Override
                public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) { return false;}

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {
                }
            };
        }
        return callback;
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
