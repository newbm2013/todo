package com.shumidub.todoapprealm.ui.fragment.report_section.report_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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
import com.shumidub.todoapprealm.ui.actionmode.report.ReportActionModeCallback;
import com.shumidub.todoapprealm.ui.activity.main.MainActivity;

/**
 * Created by Артем on 19.12.2017.
 */

public class ReportFragment extends Fragment{

    RecyclerView recyclerView;
    ActionBar actionBar;

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


        ReportRecyclerViewAdapter reportRecyclerViewAdapter
                = new ReportRecyclerViewAdapter(ReportRealmController.getReportList());
        recyclerView.setAdapter(reportRecyclerViewAdapter);

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

        MenuItem editList = menu.add("add ");
        editList.setIcon(R.drawable.ic_edit);
        MenuItem menuItem = editList.setOnMenuItemClickListener((MenuItem a) -> {


            return true;
        });

    }
}
