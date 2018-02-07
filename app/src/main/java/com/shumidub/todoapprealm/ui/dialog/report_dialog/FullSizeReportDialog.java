package com.shumidub.todoapprealm.ui.dialog.report_dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmcontrollers.reportcontroller.ReportRealmController;
import com.shumidub.todoapprealm.realmmodel.report.ReportObject;

/**
 * Created by A.shumidub on 07.02.18.
 */

public class FullSizeReportDialog extends DialogFragment {

    ReportObject reportObject;
    long id;

    TextView tvDate;
    TextView tvDayCount;
    TextView tvRetortText;
    TextView tvDayCountFieldName;

    RatingBar ratingBarSoul;
    RatingBar ratingBarHealth;


    public static FullSizeReportDialog newInstance(long id) {
        Bundle args = new Bundle();
        args.putLong("id", id);
        FullSizeReportDialog fragment = new FullSizeReportDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.report_card_view, null, false);


        tvDate = view.findViewById(R.id.tv_date);
        tvDayCount = view.findViewById(R.id.tv_count_value);
        tvRetortText = view.findViewById(R.id.tv_report_text);
        ratingBarSoul = view.findViewById(R.id.ratingbar_soul);
        ratingBarHealth = view.findViewById(R.id.ratingbar_health);
        tvDayCountFieldName = view.findViewById(R.id.tv_count_field_name);

        if (getArguments() != null && getArguments().getLong("id", 0) != 0){
            id = getArguments().getLong("id", 0);
            reportObject = ReportRealmController.getReport(id);
        } else{
            return null;
        }

        tvDayCount.setText(String.valueOf(reportObject.getCountOfDay()));
        tvRetortText.setMaxLines(10000);
        tvRetortText.setText(reportObject.getReportText());
        tvRetortText.setMovementMethod(new ScrollingMovementMethod());
        ratingBarSoul.setRating(reportObject.getSoulRating());
        ratingBarSoul.setIsIndicator(true);
        ratingBarHealth.setRating(reportObject.getHealthRating());
        ratingBarHealth.setIsIndicator(true);


        if (reportObject.isWeekReport()){
            tvDate.setText("Week " + reportObject.getWeekNumber());
            tvDayCountFieldName.setText("Week count");
        }
        else{
            tvDate.setText(reportObject.getDate());
        }

        AlertDialog dialog = builder.setView(view).create();
        return dialog;
    }
}
