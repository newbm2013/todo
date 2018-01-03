package com.shumidub.todoapprealm.ui.CategoryUI.adapter;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleExpandableListAdapter;
import java.util.List;
import java.util.Map;

import static com.shumidub.todoapprealm.ui.CategoryUI.adapter.CategoriesAndListsAdapter.GROUP_ID;
import static com.shumidub.todoapprealm.ui.CategoryUI.adapter.CategoriesAndListsAdapter.groups;

/**
 * Created by Артем on 26.12.2017.
 */

public class CustomSimpleExpandableListAdapter extends SimpleExpandableListAdapter{

    CustomSimpleExpandableListAdapter(Context context, List<? extends Map<String,
             ?>> groupData, int groupLayout, String[] groupFrom, int[] groupTo,
             List<? extends List<? extends Map<String, ?>>> childData, int childLayout,
             String[] childFrom, int[] childTo) {
        super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        View v;
        v = super.getGroupView(groupPosition, isExpanded, convertView, parent);
        Long tagId =  Long.valueOf(groups.get(groupPosition).get(GROUP_ID));
        Pair<String, Long> pair = new Pair<String, Long>("parent", tagId);
        v.setTag(pair);
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
        Pair<String, Long> pair = new Pair<String, Long>("child", 330l);
        v.setTag(pair);
        return v;
    }
}
