package com.shumidub.todoapprealm.ui.fragment.report_section.report_fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmcontrollers.reportcontroller.ReportRealmController;
import com.shumidub.todoapprealm.realmmodel.report.ReportObject;
import com.shumidub.todoapprealm.ui.actionmode.EmptyActionModeCallback;
import com.shumidub.todoapprealm.ui.actionmode.report.ReportActionModeCallback;
import com.shumidub.todoapprealm.ui.activity.main.MainActivity;
import com.shumidub.todoapprealm.ui.dialog.report_dialog.AddReportDialog;
import com.shumidub.todoapprealm.ui.dialog.report_dialog.FullSizeReportDialog;
import com.shumidub.todoapprealm.ui.dialog.syncdialog.SyncDialog;

import java.util.List;

/**
 * Created by Артем on 19.12.2017.
 *
 */

public class ReportFragment extends Fragment{

    RecyclerView recyclerView;
    LinearLayout emptyState;
    ActionBar actionBar;
    ReportRecyclerViewAdapter reportRecyclerViewAdapter;
//    List<ReportObject> reportObjectList;
    public boolean actionModeIsEnabled = false;

    public static long id = 0l;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rv_fragment_template_layout, container, false);
        return view;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);



        recyclerView = view.findViewById(R.id.rv);
        emptyState = view.findViewById(R.id.empty_state);

//        reportObjectList = ReportRealmController.getReportList();
        reportRecyclerViewAdapter = new ReportRecyclerViewAdapter(getContext());
        recyclerView.setAdapter(reportRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        reportRecyclerViewAdapter.setOnClicked((v, position, reportId)->{
            FullSizeReportDialog dialogFullSize = FullSizeReportDialog.newInstance(reportId);
            if (dialogFullSize!=null) dialogFullSize.show(getFragmentManager(), "Full_size");
        });
        reportRecyclerViewAdapter.setOnLongClicked((v, position, reportId)->{
            setId(reportId);
            ActionMode.Callback actionModeCallback
                    = new ReportActionModeCallback().getReportActionMode(getActivity(), reportId);
            actionBar.startActionMode(actionModeCallback);
            actionModeIsEnabled = true;
            return true;});

        setEmptyStateIfNeed();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem add = menu.add(3,3,3,"add ");
        add.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        add.setIcon(R.drawable.ic_add);
        add.setOnMenuItemClickListener((MenuItem a) -> {
            new AddReportDialog().show(getActivity().getSupportFragmentManager(), AddReportDialog.ADD_REPORT_TITLE);

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInputFromWindow(
                    getActivity().getWindow().getDecorView().getApplicationWindowToken(),
                    InputMethodManager.SHOW_FORCED, 0);

            return true;
        });

        MenuItem sync = menu.add(1,1,1,"Sync ");
        sync.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        sync.setIcon(R.drawable.ic_sync);
        sync.setOnMenuItemClickListener((MenuItem a) -> {

            if (permissionIsAllowed() == true){
                new SyncDialog().show(getActivity().getSupportFragmentManager(), "SYNC_DIALOG");
            }else{
                requiredWritePermisson();
                if (permissionIsAllowed() == true){
                    new SyncDialog().show(getActivity().getSupportFragmentManager(), "SYNC_DIALOG");
                }else {
                    ((MainActivity) getActivity()).showToast("Need allow permission!");
                }
            }

            return true;
        });
    }

    public void notifyDataChanged(){
//        reportObjectList = ReportRealmController.getReportList();
        reportRecyclerViewAdapter.notifyDataSetChanged();

        setEmptyStateIfNeed();

//        reportRecyclerViewAdapter = new ReportRecyclerViewAdapter(reportObjectList);
//        recyclerView.setAdapter(reportRecyclerViewAdapter);

    }


    private boolean permissionIsAllowed(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else{
            return true;
        }
    }

    private void requiredWritePermisson(){
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
    }

    public static void setId(long idReport){
        id = idReport;
    }

    @SuppressLint("RestrictedApi")
    public void finishActionMode(){
        actionBar.startActionMode(new EmptyActionModeCallback());
        actionModeIsEnabled = false;
    }


    private void setEmptyStateIfNeed(){

        RecyclerView.Adapter adapter = recyclerView.getAdapter();

        if (adapter.getItemCount() == 0){
            emptyState.setVisibility(View.VISIBLE);
        } else {
            emptyState.setVisibility(View.GONE);
        }
    }



}
