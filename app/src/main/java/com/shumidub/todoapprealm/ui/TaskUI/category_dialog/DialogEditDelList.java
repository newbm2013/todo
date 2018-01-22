package com.shumidub.todoapprealm.ui.TaskUI.category_dialog;

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
import com.shumidub.todoapprealm.sharedpref.SharedPrefHelper;
import com.shumidub.todoapprealm.ui.MainActivity;
import com.shumidub.todoapprealm.ui.TaskUI.fragments.TasksFragment;

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
            defaultListId = spHelper.getDefaultListId();
            currentIsDefaultList = defaultListId == idList;  // SP, можно только выбрать тру и тогда перепишется значение, с тру на фолсе нельзя
            currentIsCyclingList = list.isCycling();

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (title == EDIT_LIST ){
            View view = getActivity().getLayoutInflater().inflate(R.layout.add_list_layout, null);
            etName = view.findViewById(R.id.name);
            swIsDefault = view.findViewById(R.id.switch_default);
            swIsCycling = view.findViewById(R.id.switch_cycling);

            etName.setText(list.getName());
            swIsDefault.setChecked(currentIsDefaultList);
            if (currentIsDefaultList){
                swIsDefault.setEnabled(false);
                swIsDefault.setTextColor(getResources().getColor(R.color.colorAccent));
            }
//          swIsCycling.setChecked(list.isCycling());
            swIsCycling.setChecked(false);
            swIsCycling.setEnabled(false);

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

                            boolean isDefault = swIsDefault.isChecked();
                            boolean isCycling = swIsCycling.isChecked();

                            String text = etName.getText().toString();
                            ListsRealmController.editList(list, text, isDefault, isCycling);
                            if(!currentIsDefaultList && isDefault) spHelper.setDefauiltListId(idList);

                            tasksFragment.finishActionMode();
                            tasksFragment.dataChanged();

                            Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                        }

                        else if (title == DELETE_LIST){
                            if (list.getId() != defaultListId ) {
                                ListsRealmController.deleteLists(list);
                                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                tasksFragment.finishActionMode();
                                tasksFragment.dataChanged();
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
