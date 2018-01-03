package com.shumidub.todoapprealm.ui.CategoryUI.adapter;
import android.content.Context;
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

public class CategoriesAndListsAdapter {

    public static final String GROUPS = "groups";
    public static final String GROUP_ID = "group_id";
    public static final String CHILDS = "childs";
    static CustomSimpleExpandableListAdapter simpleExpandableListAdapter;

    public static ArrayList<Map<String, String>> groups = new ArrayList<>();

    public CategoriesAndListsAdapter(Context context) {

        Map<String, String> map;
        List<CategoryModel> categories = CategoriesRealmController.getCategories();
        List<ListModel> lists = ListsRealmController.getLists();

        for ( CategoryModel item: categories) {
            map = new HashMap<>();
            map.put(GROUPS, item.getName());
            map.put(GROUP_ID, String.valueOf(item.getId()));
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

            simpleExpandableListAdapter = new CustomSimpleExpandableListAdapter(
                    context,
                    groups, R.layout.group_expandable_list, groupFrom, groupTo,
                    childs, android.R.layout.simple_list_item_1, childFrom, childTo);
        }

    public CustomSimpleExpandableListAdapter getAdapter(){
        return simpleExpandableListAdapter;
    }
}
