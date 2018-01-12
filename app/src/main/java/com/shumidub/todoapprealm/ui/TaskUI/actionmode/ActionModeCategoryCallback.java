package com.shumidub.todoapprealm.ui.TaskUI.actionmode;


import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.ui.TaskUI.category_dialog.DialogAddEditDelCategory;
import com.shumidub.todoapprealm.ui.TaskUI.category_dialog.DialogAddList;
import com.shumidub.todoapprealm.ui.MainActivity;

import static com.shumidub.todoapprealm.ui.TaskUI.fragments.TasksFragment.textCategoryName;


/**
 * Created by Артем on 03.01.2018.
 */

public class ActionModeCategoryCallback {

    ActionMode.Callback mCategoryCallback;



    public ActionMode.Callback getCategoryActionModeCallback(MainActivity activity, long idOnTag) {

        mCategoryCallback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuItem addList = menu.add("add list to " + textCategoryName);
                addList.setIcon(R.drawable.ic_add);
                addList.setOnMenuItemClickListener((MenuItem a) -> {
                    DialogAddList dialogAddList = new DialogAddList();
                    dialogAddList.show(activity.getSupportFragmentManager(), "addtocategory");
                    return true;
                });

                MenuItem editCategory = menu.add("edit ");
                editCategory.setIcon(R.drawable.ic_edit);
                editCategory.setOnMenuItemClickListener((MenuItem a) -> {

                    DialogAddEditDelCategory editCategoryDialog =
                            DialogAddEditDelCategory.newInstance(idOnTag, DialogAddEditDelCategory.EDIT_CATEGORY);
                    editCategoryDialog.show(activity.getSupportFragmentManager(), "editcategory");
                    return true;
                });


                MenuItem deleteCategore = menu.add("delete ");
                deleteCategore.setIcon(R.drawable.ic_del);
                deleteCategore.setOnMenuItemClickListener((MenuItem a) -> {

                    DialogAddEditDelCategory deleteCategoryDialog =
                            DialogAddEditDelCategory.newInstance(idOnTag, DialogAddEditDelCategory.DELETE_CATEGORY);
                    deleteCategoryDialog.show(activity.getSupportFragmentManager(), "deletecategory");
                    return true;
                });

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                actionMode.setTitle(textCategoryName);
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

        return mCategoryCallback;
    }
}
