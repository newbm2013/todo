package com.shumidub.todoapprealm.ui.CategoryUI;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.bd.CategoryTasksListsBD;
import com.shumidub.todoapprealm.bd.TasksListBD;
import com.shumidub.todoapprealm.model.CategoryRealmController;
import com.shumidub.todoapprealm.model.TasksRealmController;
import com.shumidub.todoapprealm.ui.TaskUI.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class CategoryActivity extends AppCompatActivity {

    EditText et;
    Boolean keyboardOpen = false;

    ExpandableListView expandableListView;
    final String GROUPS = "groups";
    final String CHILDS = "childs";

    ActionMode actionMode;
    ActionMode.Callback callback;

    public static String textCategoryName;

    ConstraintLayout cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        cl = findViewById(R.id.cl);

        et = findViewById(R.id.et);
        expandableListView = findViewById(R.id.expandedable_list_view);

        Map<String, String> map;
        List<CategoryTasksListsBD> categoryTasksListsBD = CategoryRealmController.getCategoryTasksList();
        List<TasksListBD> tasksListBDLIst = CategoryRealmController.getTasksList();

        ArrayList<Map<String, String>> groups = new ArrayList<>();

        for ( CategoryTasksListsBD item: categoryTasksListsBD) {
            map = new HashMap<>();
            map.put(GROUPS, item.getName());
            groups.add(map);
        }

        String groupFrom[] = new String[] { GROUPS };
        int groupTo[] = new int[] {R.id.parent_text1};

        ArrayList<ArrayList<Map<String, String>>> childs = new ArrayList<>();

        ArrayList<Map<String, String>> arrayList;

        for (CategoryTasksListsBD category: categoryTasksListsBD){
            List<TasksListBD> tasksList = CategoryRealmController.getTasksList(category.getId());
            arrayList = new ArrayList<>();

            for (TasksListBD item : tasksList){
                map = new HashMap<>();
                map.put(CHILDS, item.getName());
                arrayList.add(map);
            }
            childs.add(arrayList);
        }

        String childFrom[] = new String[] { CHILDS};
        int childTo[] = new int[] { android.R.id.text1 };

        SimpleExpandableListAdapter simpleExpandableListAdapter = new SimpleExpandableListAdapter(
                this,
                groups,R.layout.group_expandable_list, groupFrom, groupTo,
                childs, android.R.layout.simple_list_item_1, childFrom, childTo);

        expandableListView.setAdapter(simpleExpandableListAdapter);

        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (view.getId() == R.id.parent_text1) {
                    try{
                        Map<String, String> map = (Map<String, String>) (simpleExpandableListAdapter.getGroup(i));
                        textCategoryName = map.get(GROUPS);
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

        if (categoryTasksListsBD.isEmpty()){
            ((TextView) findViewById(R.id.tv_empty)).setVisibility(View.VISIBLE);
        }

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
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        };

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
               String text = et.getText().toString();

               Map<String, String> map = (Map<String, String>) (simpleExpandableListAdapter.getChild(i, i1));
               String textListName = map.get(CHILDS);

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


}
