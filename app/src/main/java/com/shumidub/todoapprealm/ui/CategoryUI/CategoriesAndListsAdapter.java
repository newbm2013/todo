package com.shumidub.todoapprealm.ui.CategoryUI;

import android.app.Activity;
import android.content.Context;
import android.widget.SimpleExpandableListAdapter;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.bd.CategoryTasksListsBD;
import com.shumidub.todoapprealm.bd.TasksListBD;
import com.shumidub.todoapprealm.model.CategoryRealmController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Артем on 26.12.2017.
 */

public class CategoriesAndListsAdapter {

    static final String GROUPS = "groups";
    static final String CHILDS = "childs";
    SimpleExpandableListAdapter simpleExpandableListAdapter;
    List<CategoryTasksListsBD> categoryTasksListsBD;
    List<TasksListBD> tasksList;

    public CategoriesAndListsAdapter(Context context) {

            Map<String, String> map;
            categoryTasksListsBD = CategoryRealmController.getCategoryTasksList();
            ArrayList<Map<String, String>> groups = new ArrayList<>();
            for (CategoryTasksListsBD item : categoryTasksListsBD) {
                map = new HashMap<>();
                map.put(GROUPS, item.getName());
                groups.add(map);
            }
            String groupFrom[] = new String[]{GROUPS};
            int groupTo[] = new int[]{R.id.parent_text1};

            ArrayList<ArrayList<Map<String, String>>> childs = new ArrayList<>();
            ArrayList<Map<String, String>> arrayList;
            for (CategoryTasksListsBD category : categoryTasksListsBD) {
                tasksList = CategoryRealmController.getTasksList(category.getId());
                arrayList = new ArrayList<>();
                for (TasksListBD item : tasksList) {
                    map = new HashMap<>();
                    map.put(CHILDS, item.getName());
                    arrayList.add(map);
                }
                childs.add(arrayList);
            }
            String childFrom[] = new String[]{CHILDS};
            int childTo[] = new int[]{android.R.id.text1};
            simpleExpandableListAdapter = new android.widget.SimpleExpandableListAdapter(
                    context,
                    groups, R.layout.group_expandable_list, groupFrom, groupTo,
                    childs, android.R.layout.simple_list_item_1, childFrom, childTo);
        }

    public SimpleExpandableListAdapter getAdapter(){
        return simpleExpandableListAdapter;
    }
}
