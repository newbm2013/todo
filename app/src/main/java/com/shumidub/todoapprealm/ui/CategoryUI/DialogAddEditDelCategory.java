package com.shumidub.todoapprealm.ui.CategoryUI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmcontrollers.CategoriesRealmController;

import io.reactivex.annotations.NonNull;

/**
 * Created by Артем on 24.12.2017.
 */

public class DialogAddEditDelCategory extends android.support.v4.app.DialogFragment{

    public static String NAME_CATEGORY = "nameCategory";
    public static String MODE_CATEGORY = "ModeCategory";

    public static String ADD_CATEGORY = "Add new Category";
    public static String EDIT_CATEGORY = "Edit Category";
    public static String DELETE_CATEGORY = "Delete Category";

    String nameCategory;
    String title;

    public static DialogAddEditDelCategory newInstance(String nameCategory, String mode){
        DialogAddEditDelCategory dialogAddCategoty = new DialogAddEditDelCategory();
        Bundle arg = new Bundle();
        arg.putString(NAME_CATEGORY, nameCategory);
        arg.putString(MODE_CATEGORY, mode);
        dialogAddCategoty.setArguments(arg);
        return dialogAddCategoty;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        String textButton = "Add";

        if(getArguments()!=null){
            nameCategory = getArguments().getString(NAME_CATEGORY);
            title = getArguments().getString(MODE_CATEGORY);
            if (title == DELETE_CATEGORY) textButton = "DELETE";
            if (title == EDIT_CATEGORY) textButton = "Done";
        }else title = ADD_CATEGORY;


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (title == DELETE_CATEGORY && nameCategory!=null){}
        else{
            View view = getActivity().getLayoutInflater().inflate(R.layout.add_category_layout, null);

            if (title == EDIT_CATEGORY && nameCategory!=null ){
                EditText etName = view.findViewById(R.id.name);
                etName.setText(nameCategory);
            }

            builder.setView(view);

        }


        builder.setTitle(title)
//                .setIcon(R.drawable.ic_launcher_cat)
                .setPositiveButton(textButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        EditText etName = getDialog().findViewById(R.id.name);

                        if(title == ADD_CATEGORY){
                            if (!etName.getText().toString().isEmpty()) {
                                String text = etName.getText().toString();
                                CategoriesRealmController.addCategory(text);

                                Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();
                            }else {Toast.makeText(getContext(), "can't be empty", Toast.LENGTH_SHORT).show();}

                        }


                        else if (title == EDIT_CATEGORY && nameCategory!=null ){

                            if (!etName.getText().toString().isEmpty()) {
                                String text = etName.getText().toString();
                                CategoriesRealmController.editCategory(nameCategory, text);
                                Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();
                            }else {Toast.makeText(getContext(), "can't be empty", Toast.LENGTH_SHORT).show();}

                        }


                        else if (title == DELETE_CATEGORY && nameCategory!=null){
                            CategoriesRealmController.deleteCategoryAndChilds(nameCategory);
                        }

//                            CategoryActivity.notifyDataSetChanged();
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
