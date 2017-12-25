package com.shumidub.todoapprealm.ui.CategoryUI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.EditText;
import android.widget.Toast;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.model.CategoryRealmController;

import io.reactivex.annotations.NonNull;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;

/**
 * Created by Артем on 24.12.2017.
 */

public class DialogAddCategoty extends android.support.v4.app.DialogFragment{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add new Category")
                .setView(R.layout.add_category_layout)
//              .setIcon(R.drawable.ic_launcher_cat)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        EditText etName = getDialog().findViewById(R.id.name);
                        if (!etName.getText().toString().isEmpty()) {
                            String text = etName.getText().toString();
                            CategoryRealmController.addCategory(text);
                            Toast.makeText(getContext(), "new category added", Toast.LENGTH_SHORT).show();
                        }else {Toast.makeText(getContext(), "can't be empty", Toast.LENGTH_SHORT).show();}
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
