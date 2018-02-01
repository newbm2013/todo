package com.shumidub.todoapprealm.ui.actionmode.report;



import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.ui.fragment.task_section.folder_panel_sliding_fragment.FolderSlidingPanelFragment;


/**
 * Created by Артем on 03.01.2018.
 */

public class ReportActionModeCallback implements ActionMode.Callback {

    ActionMode.Callback mCallback;
    FolderSlidingPanelFragment folderSlidingPanelFragment;

    /*

    public ActionMode.Callback getListActionModeCallback(MainActivity activity, FolderSlidingPanelFragment folderSlidingPanelFragment, long idOnTag) {

        this.folderSlidingPanelFragment = folderSlidingPanelFragment;

        mCallback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {


                MenuItem editList = menu.add("edit ");
                editList.setIcon(R.drawable.ic_edit);
                editList.setOnMenuItemClickListener((MenuItem a) -> {

//                    EditDelFolderDialog dialog = EditDelFolderDialog.newInstance(idOnTag, EDIT_LIST, folderSlidingPanelFragment);
//                    dialog.show(activity.getSupportFragmentManager(), "editlist");



                    return true;
                });


                MenuItem deleteList = menu.add("delete ");
                deleteList.setIcon(R.drawable.ic_del);
                deleteList.setOnMenuItemClickListener((MenuItem a) -> {

//
//                    EditDelFolderDialog dialog = EditDelFolderDialog.newInstance(idOnTag, DELETE_LIST, folderSlidingPanelFragment);
//                    dialog.show(activity.getSupportFragmentManager(), "deletelist");
//


                    return true;
                });
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                actionMode.setTitle("Report");
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
        return mCallback;
    }

    */





    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {

        MenuItem editList = menu.add("edit ");
        editList.setIcon(R.drawable.ic_edit);
        editList.setOnMenuItemClickListener((MenuItem a) -> {

//                    EditDelFolderDialog dialog = EditDelFolderDialog.newInstance(idOnTag, EDIT_LIST, folderSlidingPanelFragment);
//                    dialog.show(activity.getSupportFragmentManager(), "editlist");



            return true;
        });


        MenuItem deleteList = menu.add("delete ");
        deleteList.setIcon(R.drawable.ic_del);
        deleteList.setOnMenuItemClickListener((MenuItem a) -> {

//
//                    EditDelFolderDialog dialog = EditDelFolderDialog.newInstance(idOnTag, DELETE_LIST, folderSlidingPanelFragment);
//                    dialog.show(activity.getSupportFragmentManager(), "deletelist");
//


            return true;
        });
        return true;

    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        actionMode.setTitle("Report");
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {

    }
}
