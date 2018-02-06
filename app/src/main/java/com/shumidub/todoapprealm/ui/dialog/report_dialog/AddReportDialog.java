package com.shumidub.todoapprealm.ui.dialog.report_dialog;

import com.shumidub.todoapprealm.realmcontrollers.reportcontroller.ReportRealmController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by A.shumidub on 05.02.18.
 */

public class AddReportDialog extends ReportDialog {

    @Override
    protected void setTitle() {
       title = ADD_REPORT_TITLE;
    }

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

//        rbHealth.set


    }

    @Override
    protected void setPositiveButtonInterface() {
        positiveButtonInterface = (dialogInterface, i)-> {

            //todo show error
            if (!etDate.getText().toString().isEmpty() && !etDate.getText().toString().isEmpty()){
                String date = etDate.getText().toString();
                //todo set number keyboard
                int dayCount = Integer.valueOf(etCountValue.getText().toString());
                String textReport = etTextReport.getText().toString();
                int soulRaiting = rbSoul.getNumStars();
                int healthRaiting = rbHealth.getNumStars();

                ReportRealmController.addReport(date, dayCount, textReport, soulRaiting, healthRaiting);
            }
        };
    }
}
