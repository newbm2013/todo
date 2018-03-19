package com.shumidub.todoapprealm.ui.dialog.syncdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmcontrollers.notescontroller.FolderNotesRealmController;
import com.shumidub.todoapprealm.sync.LocalSyncUtil;
import com.shumidub.todoapprealm.ui.dialog.report_dialog.AddReportDialog;
import com.shumidub.todoapprealm.ui.fragment.note_fragment.FolderNoteFragment;

import java.util.List;


/**
 * Created by A.shumidub on 05.02.18.
 *
 */

public class SyncDialog extends android.support.v4.app.DialogFragment {


    AlertDialog dialog;



    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.sync_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
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

        view.findViewById(R.id.btnSaveText).setOnClickListener((v)->{
            new LocalSyncUtil(getActivity()).putAllRealmDbAsMessage();
        });

        return dialog;
    }






}