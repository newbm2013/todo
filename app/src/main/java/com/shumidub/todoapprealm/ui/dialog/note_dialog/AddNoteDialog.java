package com.shumidub.todoapprealm.ui.dialog.note_dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmcontrollers.notescontroller.FolderNotesRealmController;
import com.shumidub.todoapprealm.ui.activity.main.MainActivity;
import com.shumidub.todoapprealm.ui.fragment.note_fragment.FolderNoteFragment;
import com.shumidub.todoapprealm.ui.fragment.note_fragment.NoteFragment;
import com.shumidub.todoapprealm.ui.fragment.report_section.report_fragment.ReportFragment;

import java.util.Calendar;
import java.util.List;


/**
 * Created by A.shumidub on 05.02.18.
 *
 */

public class AddNoteDialog extends android.support.v4.app.DialogFragment {

    protected MainActivity activity;
    protected EditText etText;
    protected TextInputLayout tilText;

    int type;
    long id;


    AlertDialog dialog;

    public static final int TYPE_FOLDER = 7;
    public static final int TYPE_NOTE = 5;

    public static final String TYPE = "Type";
    public static final String ID = "Id";


    String positiveButtonText = "Add";
    PositiveButtonInterface positiveButtonInterface = new PositiveButtonInterface() {
        @Override
        public void onClick() {
            String text = etText.getText().toString();

            if(type == TYPE_FOLDER){
                FolderNotesRealmController.addFolderNote(text);
            }else if(type == TYPE_NOTE){
                FolderNotesRealmController.addNote(id, text);
            }
        }
    };

    interface PositiveButtonInterface {
        void onClick();
    }

    public static AddNoteDialog newInstance(int type, long id) {

        Bundle args = new Bundle();
        args.putLong(ID,id);
        args.putInt(TYPE,type);
        AddNoteDialog fragment = new AddNoteDialog();
        fragment.setArguments(args);
        return fragment;
    }



    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (getArguments() != null){
            type = getArguments().getInt(TYPE, -1);
            id = getArguments().getInt(ID, -1);

            if ((type != TYPE_FOLDER && type != TYPE_NOTE) ){
                return null;
            }
        }

        activity = (MainActivity) getActivity();

        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.note_and_folder_add_edit_dialog, null);

        etText = view.findViewById(R.id.note_text);
        tilText = view.findViewById(R.id.til_note_text);

        setEtText();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder .setView(view)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}})
                .setNegativeButton("Cancel", (dialog, i) -> dialog.cancel());

        dialog = builder.create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener((DialogInterface dialogInterface, int keyCode, KeyEvent event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                // do nothing
                return true;
            }
            return false;
        });

        return dialog;
    }


    protected void notifyDataChanged() {
        List<android.support.v4.app.Fragment> fragments
                = (getActivity()).getSupportFragmentManager().getFragments();

        for (android.support.v4.app.Fragment fragment : fragments) {
            if (fragment instanceof NoteFragment) {
                ((NoteFragment) fragment).notifyDataChanged();
            }
            else if (fragment instanceof FolderNoteFragment) {
                ((FolderNoteFragment) fragment).notifyDataChanged();
            }
        }
    }

    protected void setEtText(){}

    @Override
    public void onStart() {
        super.onStart();
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener((v)->{
            if (etText.getText().toString().isEmpty()) {
                tilText.setErrorEnabled(true);
                tilText.setError("Should be filled");
            } else{
                positiveButtonInterface.onClick();
                notifyDataChanged();
                dialog.dismiss();
            }
        });

    }
}