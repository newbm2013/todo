package com.shumidub.todoapprealm.ui.CategoryUI.actionmode;


import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.ui.CategoryUI.activity.CategoryActivity;

import com.shumidub.todoapprealm.ui.CategoryUI.dialog.DialogEditDelList;


import static com.shumidub.todoapprealm.ui.CategoryUI.activity.CategoryActivity.titleList;
import static com.shumidub.todoapprealm.ui.CategoryUI.dialog.DialogEditDelList.DELETE_LIST;
import static com.shumidub.todoapprealm.ui.CategoryUI.dialog.DialogEditDelList.EDIT_LIST;


/**
 * Created by Артем on 03.01.2018.
 */

public class ActionModeListCallback {

    ActionMode.Callback mListCallback;



    public ActionMode.Callback getListActionModeCallback(CategoryActivity activity, long idOnTag) {

        mListCallback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {


                MenuItem editList = menu.add("edit ");
                editList.setIcon(R.drawable.ic_edit);
                editList.setOnMenuItemClickListener((MenuItem a) -> {
                    DialogEditDelList dialog = DialogEditDelList.newInstance(idOnTag, EDIT_LIST);
                    dialog.show(activity.getSupportFragmentManager(), "editlist");
                    return true;
                });


                MenuItem deleteList = menu.add("delete ");
                deleteList.setIcon(R.drawable.ic_del);
                deleteList.setOnMenuItemClickListener((MenuItem a) -> {
                    DialogEditDelList dialog = DialogEditDelList.newInstance(idOnTag, DELETE_LIST);
                    dialog.show(activity.getSupportFragmentManager(), "deletelist");
                    return true;
                });
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                actionMode.setTitle(titleList);

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
        return mListCallback;
    }
}
