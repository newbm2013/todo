package com.shumidub.todoapprealm.ui.CategoryUI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmcontrollers.ListsRealmController;

import io.reactivex.annotations.NonNull;

/**
 * Created by Артем on 24.12.2017.
 */

public class DialogAddList extends android.support.v4.app.DialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add new list to " + CategoryActivity.textCategoryName)
                .setView(R.layout.add_list_layout)
//              .setIcon(R.drawable.ic_launcher_cat)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String text = ((EditText) getDialog().findViewById(R.id.name)).getText().toString();
                        String categoryName = CategoryActivity.textCategoryName;
                        boolean isDefault = ((Switch) getDialog().findViewById(R.id.switch_default)).isChecked();
                        boolean isCycling = ((Switch) getDialog().findViewById(R.id.switch_cycling)).isChecked();
                        if (!text.isEmpty() && !categoryName.isEmpty()){
                            ListsRealmController.addTasksLists(text, isDefault, isCycling, categoryName );
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }


}
