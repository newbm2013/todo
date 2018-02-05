package com.shumidub.todoapprealm.ui.dialog.report_dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.ui.activity.main.MainActivity;

import io.reactivex.annotations.NonNull;

/**
 * Created by A.shumidub on 05.02.18.
 *
 */

public class ReportDialog extends android.support.v4.app.DialogFragment {

    public static final String ADD_REPORT_TITLE = "Add new report";
    public static final String EDIT_REPORT_TITLE = "Edit report";
    public static final String DELETE_REPORT_TITLE = "Delete report";


    public static final String ADD_BUTTON_TEXT = "ADD";

    MainActivity activity;
    EditText  etDate;
    EditText  etCountValue;
    EditText  etTextReport;
    RatingBar rbHealth;
    RatingBar rbSoul;

    String title;
    View view;
    String positiveButtonText;

    PositiveButtonInterface positiveButtonInterface;
    interface PositiveButtonInterface {
        void onClick(DialogInterface dialogInterface, int i);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        setTitle();
        setView();
        setPositiveButtonText();
        setPositiveButtonInterface();

        etDate = view.findViewById(R.id.name);
        etCountValue = view.findViewById(R.id.name);
        etTextReport = view.findViewById(R.id.name);
        rbHealth = view.findViewById(R.id.ratingbar_health);
        rbSoul = view.findViewById(R.id.ratingbar_soul);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
            .setView(view)
            .setPositiveButton(positiveButtonText, (dialogInterface, i) ->
                positiveButtonInterface.onClick(dialogInterface, i))
            .setNegativeButton("Cancel", (dialog, i) ->  dialog.cancel());

        AlertDialog dialog = builder.create();
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

    public void setTitle(){
        title = "title";
    }

    public void setView(){
        view = getActivity().getLayoutInflater().inflate(R.layout., null);
    }

    public void setPositiveButtonText() {
        positiveButtonText = "positiveButtonText";
    }

    public void setPositiveButtonInterface() {
        positiveButtonInterface = (dialogInterface, i) -> Log.d("DTAG", "setPositiveButtonInterface");
    }
}


