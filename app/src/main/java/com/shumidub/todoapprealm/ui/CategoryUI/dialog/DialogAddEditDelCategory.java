package com.shumidub.todoapprealm.ui.CategoryUI.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmcontrollers.CategoriesRealmController;
import com.shumidub.todoapprealm.ui.CategoryUI.activity.CategoryActivity;

import io.reactivex.annotations.NonNull;

/**
 * Created by Артем on 24.12.2017.
 */

public class DialogAddEditDelCategory extends android.support.v4.app.DialogFragment{

    public static String ID_CATEGORY = "nameCategory";
    public static String MODE_CATEGORY = "ModeCategory";

    public static String ADD_CATEGORY = "Add new Category";
    public static String EDIT_CATEGORY = "Edit Category";
    public static String DELETE_CATEGORY = "Delete Category";

    Long idCategory;
    String title;

    CategoryActivity activity;

    public static DialogAddEditDelCategory newInstance(Long idCategory, String mode){
        DialogAddEditDelCategory dialogAddCategoty = new DialogAddEditDelCategory();
        Bundle arg = new Bundle();
        arg.putLong(ID_CATEGORY, idCategory);
        arg.putString(MODE_CATEGORY, mode);
        dialogAddCategoty.setArguments(arg);
        return dialogAddCategoty;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        String textButton = "Add";

        if(getArguments()!=null){
            idCategory = getArguments().getLong(ID_CATEGORY);
            title = getArguments().getString(MODE_CATEGORY);
            if (title == DELETE_CATEGORY) textButton = "DELETE";
            if (title == EDIT_CATEGORY) textButton = "Done";
        }else title = ADD_CATEGORY;


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (title == DELETE_CATEGORY && idCategory!=null){}
        else{
            View view = getActivity().getLayoutInflater().inflate(R.layout.add_category_layout, null);

            if (title == EDIT_CATEGORY && idCategory!=null ){
                EditText etName = view.findViewById(R.id.name);
                etName.setText(CategoriesRealmController.getCategory(idCategory).getName());
            }
            builder.setView(view);
        }


        builder.setTitle(title)
//                .setIcon(R.drawable.ic_launcher_cat)
                .setPositiveButton(textButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        activity = (CategoryActivity) getActivity();

                        EditText etName = getDialog().findViewById(R.id.name);

                        if(title == ADD_CATEGORY){
                            if (!etName.getText().toString().isEmpty()) {
                                String text = etName.getText().toString();
                                CategoriesRealmController.addCategory(text);

                                Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();

                                activity.finishActionMode();
                                activity.dataChanged();


                            }else {Toast.makeText(getContext(), "can't be empty", Toast.LENGTH_SHORT).show();}

                        }

                        else if (title == EDIT_CATEGORY && idCategory!=null ){

                            if (!etName.getText().toString().isEmpty()) {
                                String text = etName.getText().toString();
                                CategoriesRealmController.editCategory(idCategory, text);
                                Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();

                                activity.finishActionMode();
                                activity.dataChanged();


                            }else {Toast.makeText(getContext(), "can't be empty", Toast.LENGTH_SHORT).show();}

                        }


                        else if (title == DELETE_CATEGORY && idCategory!=null){
                            CategoriesRealmController.deleteCategoryAndChilds(idCategory);

                            Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();

                            activity.finishActionMode();
                            activity.dataChanged();
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
