package com.shumidub.todoapprealm.ui.folder_dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmcontrollers.FolderRealmController;
import com.shumidub.todoapprealm.ui.activity.main_activity.MainActivity;
import com.shumidub.todoapprealm.ui.fragment.folder_panel_sliding_fragment.FolderSlidingPanelFragment;

import io.reactivex.annotations.NonNull;

/**
 * Created by Артем on 24.12.2017.
 */

public class AddFolderDialog extends android.support.v4.app.DialogFragment {

    EditText etName;
    MainActivity activity;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_folder_layout, null);
        etName = view.findViewById(R.id.name);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add new folder ")
                .setView(view)
//              .setIcon(R.drawable.ic_launcher_cat)
                .setPositiveButton("Add", (dialogInterface, i) -> {
                        String text = ((EditText) getDialog().findViewById(R.id.name)).getText().toString();
                        if (!text.isEmpty()){
                            long idFolder = FolderRealmController.addFolder(text);
                            Toast.makeText(getContext(),"Done", Toast.LENGTH_SHORT).show();
                            activity = (MainActivity) getActivity();
                            for (Fragment fragment : activity.getSupportFragmentManager().getFragments()){
                                if (fragment instanceof FolderSlidingPanelFragment){
                                    ((FolderSlidingPanelFragment) fragment).notifyListsDataChanged();
                                }
                            }
                        }
                })
                .setNegativeButton("Cancel", (dialog, i) ->  dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener((DialogInterface dialogInterface, int keyCode,KeyEvent event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                // do nothing
                return true;
            }
            return false;
        });
        return dialog;
    }
}
