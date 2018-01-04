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
import com.shumidub.todoapprealm.realmcontrollers.ListsRealmController;

import io.reactivex.annotations.NonNull;

/**
 * Created by Артем on 24.12.2017.
 */

public class DialogEditDelList extends android.support.v4.app.DialogFragment{

    public static String ID_LIST = "idList";
    public static String MODE_LIST = "ModeList";

    public static String EDIT_LIST = "Edit ";
    public static String DELETE_LIST = "Delete ";

    long idList;
    String title;

    public static DialogEditDelList newInstance(long idList, String mode){
        DialogEditDelList dialog = new DialogEditDelList();
        Bundle arg = new Bundle();
        arg.putLong(ID_LIST, idList);
        arg.putString(MODE_LIST, mode);
        dialog.setArguments(arg);
        return dialog;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        String textButton = "Edit";

        if(getArguments()!=null){
            idList = getArguments().getLong(ID_LIST);
            title = getArguments().getString(MODE_LIST);
            if (title == DELETE_LIST) textButton = "DELETE";
            if (title == EDIT_LIST) textButton = "Done";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (title == EDIT_LIST ){
            View view = getActivity().getLayoutInflater().inflate(R.layout.add_category_layout, null);
            EditText etName = view.findViewById(R.id.name);
            etName.setText(ListsRealmController.getListById(idList).getName());
            builder.setView(view);
        }

        builder.setTitle(title)
//                .setIcon(R.drawable.ic_launcher_cat)
                .setPositiveButton(textButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        EditText etName = getDialog().findViewById(R.id.name);

                        if (title == EDIT_LIST ){

                            if (!etName.getText().toString().isEmpty()) {
                                String text = etName.getText().toString();
                                ListsRealmController.editList(idList, text, false, false, 0);
                                Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();
                            }else {Toast.makeText(getContext(), "can't be empty", Toast.LENGTH_SHORT).show();}

                        }


                        else if (title == DELETE_LIST){
                            ListsRealmController.deleteLists(idList);
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
