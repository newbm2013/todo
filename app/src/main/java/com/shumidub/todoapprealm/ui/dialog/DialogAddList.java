package com.shumidub.todoapprealm.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmcontrollers.ListsRealmController;
import com.shumidub.todoapprealm.ui.activity.mainactivity.MainActivity;
import com.shumidub.todoapprealm.ui.fragment.lists_and_sliding_fragment.TasksFragment;

import io.reactivex.annotations.NonNull;

/**
 * Created by Артем on 24.12.2017.
 */

public class DialogAddList extends android.support.v4.app.DialogFragment {

    EditText etName;
    Switch swIsDefault;
    Switch swIsCycling;
    MainActivity activity;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.add_list_layout, null);
        etName = view.findViewById(R.id.name);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add new list "
        )
                .setView(view)
//              .setIcon(R.drawable.ic_launcher_cat)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String text = ((EditText) getDialog().findViewById(R.id.name)).getText().toString();
                        if (!text.isEmpty()){
                            long idList = ListsRealmController.addTasksLists(text);
                            Toast.makeText(getContext(),"Done", Toast.LENGTH_SHORT).show();

                            activity = (MainActivity) getActivity();
                            for (Fragment fragment : activity.getSupportFragmentManager().getFragments()){
                                if (fragment instanceof TasksFragment){
                                    ((TasksFragment) fragment).notifyListsDataChanged();
                                }
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    // do nothing
                    return true;
                }
                return false;
            }
        });
        return dialog;
    }
}
