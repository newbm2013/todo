package com.shumidub.todoapprealm.ui.TaskUI.adapter;
import android.content.Context;
import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.model.ListModel;
import com.shumidub.todoapprealm.realmcontrollers.ListsRealmController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Артем on 26.12.2017.
 */

public class CategoriesAndListsAdapter {

    public static final String GROUPS = "groups";
    public static final String GROUP_ID = "group_id";
    public static final String CHILD_ID = "child_id";
    public static final String CHILDS = "childs";
    static CustomSimpleExpandableListAdapter simpleExpandableListAdapter;

    public static ArrayList<Map<String, String>> groups;
    public static ArrayList<ArrayList<Map<String, String>>> childs;

    public CategoriesAndListsAdapter(Context context) {

        groups = new ArrayList<>();

        Map<String, String> map;
        List<CategoryModel> categories = CategoriesRealmController.getCategories();

        for ( CategoryModel item: categories) {
            map = new HashMap<>();
            map.put(GROUPS, item.getName());
            map.put(GROUP_ID, String.valueOf(item.getId()));
            groups.add(map);
        }

        String groupFrom[] = new String[] { GROUPS };
        int groupTo[] = new int[] {R.id.parent_text1};

        childs = new ArrayList<>();

        ArrayList<Map<String, String>> arrayList;

        for (CategoryModel category: categories){
            List<ListModel> tasksList = ListsRealmController.getListsByCategoryId(category.getId());
            arrayList = new ArrayList<>();

            for (ListModel item : tasksList){
                map = new HashMap<>();
                map.put(CHILDS, item.getName());
                map.put(CHILD_ID, String.valueOf(item.getId()));
                arrayList.add(map);
            }
            childs.add(arrayList);
        }

        String childFrom[] = new String[] { CHILDS};
        int childTo[] = new int[] { R.id.child_text1 };

            simpleExpandableListAdapter = new CustomSimpleExpandableListAdapter(
                    context,
                    groups, R.layout.group_expandable_list, groupFrom, groupTo,
                    childs, R.layout.child_expandable_list, childFrom, childTo);
        }

    public CustomSimpleExpandableListAdapter getAdapter(){
        return simpleExpandableListAdapter;
    }

}
