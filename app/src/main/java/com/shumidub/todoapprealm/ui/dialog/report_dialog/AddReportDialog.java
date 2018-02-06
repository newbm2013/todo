package com.shumidub.todoapprealm.ui.dialog.report_dialog;

import com.shumidub.todoapprealm.realmcontrollers.reportcontroller.ReportRealmController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by A.shumidub on 05.02.18.
 *
 */

public class AddReportDialog extends BaseReportDialog {

    @Override
    protected void setView() {
        super.setView();
    }

    @Override
    protected void setPositiveButtonText() {
        positiveButtonText = ADD_BUTTON_TEXT;
    }

    @Override
    protected void setDialogViews() {
        super.setDialogViews();
        String defaultDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date(System.currentTimeMillis()));
        int defaultCount = 100;
        etDate.setText(defaultDate);
        etCountValue.setText("" + defaultCount);
    }

    @Override
    protected void setPositiveButtonInterface() {
        positiveButtonInterface = (v)-> {
            if (!etDate.getText().toString().isEmpty() && !etCountValue.getText().toString().isEmpty()){
                String date = etDate.getText().toString();
                int dayCount = Integer.valueOf(etCountValue.getText().toString());
                String textReport = etTextReport.getText().toString();
                int soulRating = rbSoul.getProgress();
                int healthRating = rbHealth.getProgress();
                ReportRealmController.addReport(date, dayCount, textReport, soulRating, healthRating, switcWeek.isChecked());
                notifyDataChanged();
                dismiss();
            } else {
                if (etDate.getText().toString().isEmpty()) {
                    setDateError("Should be filled", true);
                } else {
                    setDateError("", false);
                }
                if (etCountValue.getText().toString().isEmpty()) {
                    setCountValueError("Should be filled", true);
                } else {
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
    }
}
