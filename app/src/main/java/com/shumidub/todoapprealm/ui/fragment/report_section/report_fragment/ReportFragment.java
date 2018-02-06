package com.shumidub.todoapprealm.ui.fragment.report_section.report_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmcontrollers.reportcontroller.ReportRealmController;
import com.shumidub.todoapprealm.realmmodel.report.ReportObject;
import com.shumidub.todoapprealm.ui.actionmode.report.ReportActionModeCallback;
import com.shumidub.todoapprealm.ui.activity.main.MainActivity;
import com.shumidub.todoapprealm.ui.dialog.report_dialog.AddReportDialog;

import java.util.List;

/**
 * Created by Артем on 19.12.2017.
 */

public class ReportFragment extends Fragment{

    RecyclerView recyclerView;
    ActionBar actionBar;
    ReportRecyclerViewAdapter reportRecyclerViewAdapter;
    List<ReportObject> reportObjectList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rv_fragment_template_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        actionBar.setTitle("Report");

        recyclerView = view.findViewById(R.id.rv);


        reportObjectList = ReportRealmController.getReportList();
        reportRecyclerViewAdapter = new ReportRecyclerViewAdapter(reportObjectList);
        recyclerView.setAdapter(reportRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        reportRecyclerViewAdapter.setOnClicked((v, position, reportId)->{
            //todo open full size
        });
        reportRecyclerViewAdapter.setOnLongClicked((v, position, reportId)->{
            android.support.v7.view.ActionMode.Callback actionModeCallback = new ReportActionModeCallback();
            actionBar.startActionMode(actionModeCallback);
            return true;});
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem add = menu.add("add ");
        add.setIcon(R.drawable.ic_edit);
        add.setOnMenuItemClickListener((MenuItem a) -> {
            new AddReportDialog().show(getActivity().getSupportFragmentManager(), AddReportDialog.ADD_REPORT_TITLE);
        return true;
        });

    }

    public void notifyDataChanged(){
        reportObjectList = ReportRealmController.getReportList();
        reportRecyclerViewAdapter.notifyDataSetChanged();
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}
