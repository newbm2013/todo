package com.shumidub.todoapprealm.ui.dialog.report_dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.shumidub.todoapprealm.realmcontrollers.reportcontroller.ReportRealmController;
import com.shumidub.todoapprealm.realmmodel.report.ReportObject;
import com.shumidub.todoapprealm.ui.fragment.report_section.report_fragment.ReportFragment;

/**
 * Created by A.shumidub on 05.02.18.
 *
 */

public class EditReportDialog extends BaseReportDialog {

    long id;

//    public static EditReportDialog newInstance(long id) {
//        Bundle args = new Bundle();
//        args.putLong("id", id);
//        EditReportDialog fragment = new EditReportDialog();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    protected void setView() {
        super.setView();
    }

    @Override
    protected void setPositiveButtonText() {
        positiveButtonText = EDIT_REPORT_TITLE;
    }

    @Override
    protected void setDialogViews() {
        super.setDialogViews();

        id = ReportFragment.id;

        ReportObject reportObject = ReportRealmController.getReport(id);
                etDate.setText(reportObject.getDate());
        etCountValue.setText(String.valueOf(reportObject.getCountOfDay()));
        etTextReport.setText(reportObject.getReportText());
        rbHealth.setRating(reportObject.getHealthRating());
        rbSoul.setRating(reportObject.getSoulRating());

//        rbHealth.set

    }

    @Override
    protected void setPositiveButtonInterface() {
        positiveButtonInterface = (v)-> {

            //todo show error
            if (!etDate.getText().toString().isEmpty() && !etDate.getText().toString().isEmpty()){
                String date = etDate.getText().toString();
                //todo set number keyboard
                int dayCount = Integer.valueOf(etCountValue.getText().toString());
                String textReport = etTextReport.getText().toString();
                int soulRating = rbSoul.getProgress();
                int healthRating = rbHealth.getProgress();
                ReportRealmController.editReport(id ,date, dayCount, textReport, soulRating, healthRating);
                notifyDataChanged();
                dismiss();
            } else{
                //todo not work
                if (etDate.getText().toString().isEmpty()){
                    setDateError("Should be filled", true);
                } else{
                    setDateError("", false);
                }
                if (etCountValue.getText().toString().isEmpty()){
                    setCountValueError("Should be filled", true);
                }else{
                    setCountValueError("", false);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener((v)->
                positiveButtonInterface.onClick(v));
        if (id == 0) dismiss();
    }
}
