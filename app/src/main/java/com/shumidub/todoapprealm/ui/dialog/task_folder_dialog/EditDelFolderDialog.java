package com.shumidub.todoapprealm.ui.dialog.task_folder_dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmcontrollers.FolderTaskRealmController;
import com.shumidub.todoapprealm.realmmodel.FolderTaskObject;

import com.shumidub.todoapprealm.ui.activity.main.MainActivity;
import com.shumidub.todoapprealm.ui.fragment.task_section.folder_panel_sliding_fragment.fragment.FolderSlidingPanelFragment;

import io.reactivex.annotations.NonNull;

/**
 * Created by Артем on 24.12.2017.
 */

public class EditDelFolderDialog extends android.support.v4.app.DialogFragment{

    public static String ID_FOLDER = "idFolder";
    public static String MODE_LIST = "ModeList";
    public static String EDIT_LIST = "Edit ";
    public static String DELETE_LIST = "Delete ";
    long idFolder;
    String title;
    FolderTaskObject folderObject;
    String currentTextList;
    EditText etName;
    CheckBox cbIsDaily;
    long defaultFolderId;
    MainActivity activity;
    static FolderSlidingPanelFragment folderSlidingPanelFragment;

    public static EditDelFolderDialog newInstance(long idList, String mode, FolderSlidingPanelFragment fragment){
        folderSlidingPanelFragment = fragment;
        EditDelFolderDialog dialog = new EditDelFolderDialog();
        Bundle arg = new Bundle();
        arg.putLong(ID_FOLDER, idList);
        arg.putString(MODE_LIST, mode);
        dialog.setArguments(arg);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String textButton = "Edit";

        if(getArguments()!=null){
            idFolder = getArguments().getLong(ID_FOLDER);
            title = getArguments().getString(MODE_LIST);
            if (title == DELETE_LIST) textButton = "DELETE";
            if (title == EDIT_LIST) textButton = "Done";

            folderObject = FolderTaskRealmController.getFolder(idFolder);
            currentTextList = folderObject.getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (title == EDIT_LIST ){
            View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_folder_layout, null);
            etName = view.findViewById(R.id.name);
            cbIsDaily = view.findViewById(R.id.checkboxIsDaily);
            etName.setText(folderObject.getName());
            cbIsDaily.setChecked(folderObject.isDaily());
            builder.setView(view);
        } else if (title == DELETE_LIST ){
            builder.setMessage("Are you sure?");
        }
        builder.setTitle(title)
//                .setIcon(R.drawable.ic_launcher_cat)
                .setPositiveButton(textButton, (dialog, i) -> {
                    activity = (MainActivity) getActivity();
                    if (title == EDIT_LIST ) {
                        String text = etName.getText().toString();
                        FolderTaskRealmController.editFolder(folderObject, text, cbIsDaily.isChecked());
                        folderSlidingPanelFragment.finishActionMode();
                        folderSlidingPanelFragment.notifyListsDataChanged();
                        activity.showToast("Done");
//                        Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();

                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getDialog().getWindow().getDecorView().getWindowToken(), 0);

                    } else if (title == DELETE_LIST){
                        if (folderObject.getId() != defaultFolderId) {
                            FolderTaskRealmController.deleteFolder(folderObject);
                            activity.showToast("Deleted");
//                            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                            folderSlidingPanelFragment.finishActionMode();
                            folderSlidingPanelFragment.notifyListsDataChanged();
                        }else{
                            activity.showToast("Can't delete default folderObject");
//                            Toast.makeText(getContext(),
//                                    "Can't delete default folderObject", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getDialog().getWindow().getDecorView().getWindowToken(), 0);
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener( (dialogInterface, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    // do nothing
                    return true;
                }
                return false;
        });

        dialog.getWindow().setBackgroundDrawableResource( R.color.colorBackgroundActivity );
        return dialog;
    }
}
