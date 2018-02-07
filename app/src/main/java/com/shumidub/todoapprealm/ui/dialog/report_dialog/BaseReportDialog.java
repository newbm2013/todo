package com.shumidub.todoapprealm.ui.dialog.report_dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.ui.activity.main.MainActivity;
import com.shumidub.todoapprealm.ui.fragment.report_section.report_fragment.ReportFragment;

import java.util.Calendar;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by A.shumidub on 05.02.18.
 *
 */

public class BaseReportDialog extends android.support.v4.app.DialogFragment {

    public static final String ADD_REPORT_TITLE = "Add new report";
    public static final String EDIT_REPORT_TITLE = "Edit report";
    public static final String DELETE_REPORT_TITLE = "Delete report";

    public static final String ADD_BUTTON_TEXT = "ADD";

    protected MainActivity activity;
    protected EditText etDate;
    protected EditText etCountValue;
    protected EditText etTextReport;
    protected RatingBar rbHealth;
    protected RatingBar rbSoul;
    protected Switch switchWeek;
    protected LinearLayout llSwitchWeekContainer;

    protected TextInputLayout tilDate;
    protected TextInputLayout tilCountValue;

    String title;
    View view;
    String positiveButtonText;
    PositiveButtonInterface positiveButtonInterface;

    int currentWeekNumber;
    Calendar calendar;


    AlertDialog dialog;

    interface PositiveButtonInterface {
        void onClick(View view);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        currentWeekNumber = calendar.get(Calendar.WEEK_OF_YEAR);

        activity = (MainActivity) getActivity();
        setTitle();
        setView();
        setPositiveButtonText();
        setPositiveButtonInterface();
        setDialogViews();




        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder .setView(view)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
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

    private void setTitle() {
        title = "title";
    }

    protected void setView() {
        view = getActivity().getLayoutInflater().inflate(R.layout.report_dialog_add_edit_show_fully_size, null);
    }

    protected void setPositiveButtonText() {
        positiveButtonText = "positiveButtonText";
    }

    protected void setPositiveButtonInterface() {
        positiveButtonInterface = (v) -> Log.d("DTAG", "setPositiveButtonInterface");
    }

    protected void setDialogViews() {
        etDate = view.findViewById(R.id.tv_date);
        etCountValue = view.findViewById(R.id.tv_count_value);
        etTextReport = view.findViewById(R.id.tv_report_text);
        rbHealth = view.findViewById(R.id.ratingbar_health);
        rbSoul = view.findViewById(R.id.ratingbar_soul);
        tilDate = view.findViewById(R.id.til_date);
        tilCountValue = view.findViewById(R.id.til_count_value);
        switchWeek = view.findViewById(R.id.switch_week);
        llSwitchWeekContainer = view.findViewById(R.id.ll_week_switc_container);
    }

    protected void notifyDataChanged() {
        List<android.support.v4.app.Fragment> fragments
                = (getActivity()).getSupportFragmentManager().getFragments();

        for (android.support.v4.app.Fragment fragment : fragments) {
            if (fragment instanceof ReportFragment) {
                ((ReportFragment) fragment).notifyDataChanged();
            }
        }
    }

    protected void setDateError(String errorText, boolean errorEnable){
        tilDate.setErrorEnabled(errorEnable);
        tilDate.setError(errorText);
    }

    protected void setCountValueError(String errorText, boolean errorEnable){
        tilCountValue.setErrorEnabled(errorEnable);
        tilCountValue.setError(errorText);
    }
}