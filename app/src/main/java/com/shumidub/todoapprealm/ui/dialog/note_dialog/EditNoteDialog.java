package com.shumidub.todoapprealm.ui.dialog.note_dialog;

import android.view.View;

import com.shumidub.todoapprealm.realmcontrollers.notescontroller.FolderNotesRealmController;

/**
 * Created by Артем on 08.02.2018.
 *
 */

public class EditNoteDialog extends AddNoteDialog {

    long id;
    String positiveButtonText = "Edit";
    PositiveButtonInterface positiveButtonInterface = new PositiveButtonInterface() {
        @Override
        public void onClick() {
            String text = etText.getText().toString();

            if(type == TYPE_FOLDER){
                FolderNotesRealmController.editFolderNote(id, text);
            }else if(type == TYPE_NOTE){
                FolderNotesRealmController.editNote(id, text);
            }

        }
    };

    @Override
    protected void setEtText() {

    }
}
