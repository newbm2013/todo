package com.shumidub.todoapprealm.ui.category_dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.model.ListModel;
import com.shumidub.todoapprealm.realmcontrollers.ListsRealmController;
import com.shumidub.todoapprealm.ui.unused.SharedPrefHelper;
import com.shumidub.todoapprealm.ui.activity.mainactivity.MainActivity;
import com.shumidub.todoapprealm.ui.fragment.lists_and_sliding_fragment.TasksFragment;

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
    ListModel list;
    String currentTextList;
    boolean currentIsDefaultList;
    boolean currentIsCyclingList;
    long currentIdCategoryList; // for future for remove
    EditText etName;
    Switch swIsDefault;
    Switch swIsCycling;
    long defaultListId;
    MainActivity activity;
    static TasksFragment tasksFragment;


    public static DialogEditDelList newInstance(long idList, String mode, TasksFragment fragment){
        tasksFragment = fragment;
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

        SharedPrefHelper spHelper = new SharedPrefHelper(getActivity());

        String textButton = "Edit";

        if(getArguments()!=null){
            idList = getArguments().getLong(ID_LIST);
            title = getArguments().getString(MODE_LIST);
            if (title == DELETE_LIST) textButton = "DELETE";
            if (title == EDIT_LIST) textButton = "Done";

            list = ListsRealmController.getListById(idList);

            currentTextList = list.getName();


        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (title == EDIT_LIST ){
            View view = getActivity().getLayoutInflater().inflate(R.layout.add_list_layout, null);
            etName = view.findViewById(R.id.name);

            etName.setText(list.getName());



            builder.setView(view);
        } else if (title == DELETE_LIST ){
            builder.setMessage("Are you sure?");
        }

        builder.setTitle(title)
//                .setIcon(R.drawable.ic_launcher_cat)
                .setPositiveButton(textButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        activity = (MainActivity) getActivity();

                        if (title == EDIT_LIST ) {


                            String text = etName.getText().toString();
                            ListsRealmController.editList(list, text);


                            tasksFragment.finishActionMode();
                            tasksFragment.notifyListsDataChanged();

                            Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                        }

                        else if (title == DELETE_LIST){
                            if (list.getId() != defaultListId ) {
                                ListsRealmController.deleteLists(list);
                                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                tasksFragment.finishActionMode();
                                tasksFragment.notifyListsDataChanged();
                            }else{
                                Toast.makeText(getContext(),
                                        "Can't delete default list", Toast.LENGTH_SHORT).show();
                            }
                        }
//                            CategoryActivity.notifyDataSetChanged();
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
