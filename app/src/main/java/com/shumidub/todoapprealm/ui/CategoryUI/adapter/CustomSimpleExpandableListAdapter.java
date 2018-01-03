package com.shumidub.todoapprealm.ui.CategoryUI.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleExpandableListAdapter;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.model.CategoryModel;
import com.shumidub.todoapprealm.model.ListModel;
import com.shumidub.todoapprealm.realmcontrollers.CategoriesRealmController;
import com.shumidub.todoapprealm.realmcontrollers.ListsRealmController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Артем on 26.12.2017.
 */

public class CustomSimpleExpandableListAdapter extends SimpleExpandableListAdapter{

    public static final String GROUPS = "groups";
    public static final String CHILDS = "childs";
    static SimpleExpandableListAdapter simpleExpandableListAdapter;

    public CustomSimpleExpandableListAdapter(Context context, List<? extends Map<String, ?>> groupData, int groupLayout, String[] groupFrom, int[] groupTo, List<? extends List<? extends Map<String, ?>>> childData, int childLayout, String[] childFrom, int[] childTo) {
        super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo);
    }

    @Override
    public View newChildView(boolean isLastChild, ViewGroup parent){
       View childView =  super.newChildView(isLastChild, parent);
       childView.setTag("child");
       return childView;
    }

    @Override
    public View newGroupView(boolean isExpanded, ViewGroup parent) {
        View groupView = super.newGroupView(isExpanded, parent);
        groupView.setTag("group");
        return groupView;
    }

    public void customSimpleExpandableListAdapter43 (Context context) {


        Map<String, String> map;
        List<CategoryModel> categories = CategoriesRealmController.getCategories();
        List<ListModel> lists = ListsRealmController.getLists();



        ArrayList<Map<String, String>> groups = new ArrayList<>();

        for ( CategoryModel item: categories) {
            map = new HashMap<>();
            map.put(GROUPS, item.getName());
            groups.add(map);
        }

        String groupFrom[] = new String[] { GROUPS };
        int groupTo[] = new int[] {R.id.parent_text1};

        ArrayList<ArrayList<Map<String, String>>> childs = new ArrayList<>();

        ArrayList<Map<String, String>> arrayList;

        for (CategoryModel category: categories){
            List<ListModel> tasksList = ListsRealmController.getListsByCategoryId(category.getId());
            arrayList = new ArrayList<>();

            for (ListModel item : tasksList){
                map = new HashMap<>();
                map.put(CHILDS, item.getName());
                arrayList.add(map);
            }
            childs.add(arrayList);
        }

        String childFrom[] = new String[] { CHILDS};
        int childTo[] = new int[] { android.R.id.text1 };



            simpleExpandableListAdapter = new SimpleExpandableListAdapter(
                    context,
                    groups, R.layout.group_expandable_list, groupFrom, groupTo,
                    childs, android.R.layout.simple_list_item_1, childFrom, childTo);
        }

    public SimpleExpandableListAdapter getAdapter(){
        return simpleExpandableListAdapter;
    }
}
