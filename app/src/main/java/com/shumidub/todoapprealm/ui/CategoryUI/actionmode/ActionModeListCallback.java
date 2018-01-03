package com.shumidub.todoapprealm.ui.CategoryUI.actionmode;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.ui.CategoryUI.activity.CategoryActivity;
import com.shumidub.todoapprealm.ui.CategoryUI.dialog.DialogAddEditDelCategory;
import com.shumidub.todoapprealm.ui.CategoryUI.dialog.DialogAddList;

import static com.shumidub.todoapprealm.ui.CategoryUI.activity.CategoryActivity.listName;
import static com.shumidub.todoapprealm.ui.CategoryUI.activity.CategoryActivity.textCategoryName;


/**
 * Created by Артем on 03.01.2018.
 */

public class ActionModeListCallback {

    ActionMode.Callback mListCallback;



    public ActionMode.Callback getCategoryActionModeCallback(CategoryActivity activity) {

        mListCallback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {

                MenuItem editCategory = menu.add("edit ");
                editCategory.setIcon(R.drawable.ic_launcher_foreground);
                editCategory.setOnMenuItemClickListener((MenuItem a) -> {

//                    DialogAddEditDelCategory editCategoryDialog =
//                            DialogAddEditDelCategory.newInstance(textCategoryName, DialogAddEditDelCategory.EDIT_CATEGORY);
//                    editCategoryDialog.show(activity.getSupportFragmentManager(), "editcategory");
                    return true;
                });


                MenuItem deleteCategore = menu.add("delete ");
                deleteCategore.setIcon(R.drawable.ic_launcher_foreground);
                deleteCategore.setOnMenuItemClickListener((MenuItem a) -> {

//                    DialogAddEditDelCategory deleteCategoryDialog =
//                            DialogAddEditDelCategory.newInstance(textCategoryName, DialogAddEditDelCategory.DELETE_CATEGORY);
//                    deleteCategoryDialog.show(activity.getSupportFragmentManager(), "deletecategory");
                    return true;
                });

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                actionMode.setTitle(listName);
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
