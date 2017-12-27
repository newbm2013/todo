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
import android.widget.Switch;
import android.widget.TextView;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.model.CategoryRealmController;
import com.shumidub.todoapprealm.model.TasksRealmController;
import com.shumidub.todoapprealm.ui.TaskUI.MainActivity;

import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class CategoryActivity extends AppCompatActivity {

    EditText et;
    Switch swDefault;
    Switch swCycling;
    ExpandableListView expandableListView;
    android.widget.SimpleExpandableListAdapter simpleExpandableListAdapter;
    ActionMode actionMode;
    ActionMode.Callback callback;
    public static String textCategoryName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        findViews();
        CategoriesAndListsAdapter categoriesAndListsAdapter = new CategoriesAndListsAdapter(this);
        simpleExpandableListAdapter = categoriesAndListsAdapter.getAdapter();
        expandableListView.setAdapter(simpleExpandableListAdapter);

        if (categoriesAndListsAdapter.categoryTasksListsBD.isEmpty()){
            ((TextView) findViewById(R.id.tv_empty)).setVisibility(View.VISIBLE);
        }

        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (view.getId() == R.id.parent_text1) {
                    try{
                        Map<String, String> map = (Map<String, String>) (simpleExpandableListAdapter.getGroup(i));
                        textCategoryName = map.get(CategoriesAndListsAdapter.GROUPS);
                        actionMode = startActionMode(callback);
                    } catch (IndexOutOfBoundsException ignored){}
//                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    Log.d("DEBUG_TAG", "onItemLongClick: if");
                }else{
                    Log.d("DEBUG_TAG", "onItemLongClick: else");
                }
                return true;
            }
        });

        callback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuItem addList = menu.add("add list to " + textCategoryName);
                addList.setOnMenuItemClickListener((MenuItem a) -> {
                    DialogAddList dialogAddList = new DialogAddList();
                    dialogAddList.show(getSupportFragmentManager(), "category");
                    return true;
                });
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) { return false; }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) { return false;}

            @Override
            public void onDestroyActionMode(ActionMode actionMode) { }
        };

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
               String text = et.getText().toString();

               Map<String, String> map = (Map<String, String>) (simpleExpandableListAdapter.getChild(i, i1));
               String textListName = map.get(CategoriesAndListsAdapter.CHILDS);

               if (!text.isEmpty() || !text.equals("")) {
                   TasksRealmController.insertItems(text, false, false, textListName);
                   et.setText("");
               }else{
                   long listId;
                   if (!CategoryRealmController.getTasksList(textListName).equals(null) ||
                           CategoryRealmController.getTasksList(textListName) != null){
                       listId = CategoryRealmController.getTasksList(textListName).getId();
                   }else {
                       listId = 0;
                   }
                   Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK);
                   intent.putExtra("textId", listId);
                   startActivity(intent);
               }
               return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem addCategory = menu.add("add category");
        addCategory.setOnMenuItemClickListener((MenuItem a) -> {
           (new DialogAddCategoty()).show(getSupportFragmentManager(), "category");
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
    }
}
