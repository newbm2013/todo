package com.shumidub.todoapprealm.ui.CategoryUI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmcontrollers.CategoriesRealmController;
import com.shumidub.todoapprealm.realmcontrollers.ListsRealmController;

import io.reactivex.annotations.NonNull;

/**
 * Created by Артем on 24.12.2017.
 */

public class DialogAddCategoty extends android.support.v4.app.DialogFragment{

    public static String NAME_CATEGORY = "nameCategory";
    public static String MODE_CATEGORY = "ModeCategory";

    public static String ADD_CATEGORY = "Add new Category";
    public static String EDIT_CATEGORY = "Edit Category";
    public static String DELETE_CATEGORY = "Delete Category";

    public static DialogAddCategoty newInstance(String nameCategory, String mode){
        DialogAddCategoty dialogAddCategoty = new DialogAddCategoty();
        Bundle arg = new Bundle();
        arg.putString(NAME_CATEGORY, nameCategory);
        arg.putString(MODE_CATEGORY, mode);
        dialogAddCategoty.setArguments(arg);
        return dialogAddCategoty;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        String nameCategory;
        String title;
        String textButton = "Add";

        if(getArguments()!=null){
            nameCategory = getArguments().getString(NAME_CATEGORY);
            title = getArguments().getString(MODE_CATEGORY);
            textButton = title == EDIT_CATEGORY ? "Done" : "Delete";
        }else title = ADD_CATEGORY;



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setView(R.layout.add_category_layout)
//              .setIcon(R.drawable.ic_launcher_cat)
                .setPositiveButton(textButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        EditText etName = getDialog().findViewById(R.id.name);
                        if (!etName.getText().toString().isEmpty()) {
                            String text = etName.getText().toString();

                            if(title == ADD_CATEGORY)CategoriesRealmController.addCategory(text);
                            else if (title == EDIT_CATEGORY) CategoriesRealmController.editCategory(nameCategory);
                            else if (title == DELETE_CATEGORY) CategoriesRealmController.deleteCategory(nameCategory);

                            Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();
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
