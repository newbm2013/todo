package com.shumidub.todoapprealm.ui.TaskUI.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmcontrollers.TasksRealmController;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by Артем on 19.12.2017.
 */

public class TasksFragment2 extends TasksFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.item_fragment_layout, container, false);
        View view = inflater.inflate(R.layout.task_slideup_panel_layout2, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llBottomFooter.setAlpha(0.0f);
        llBottomFooter.setVisibility(View.GONE);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    @Override
    protected ExpandableListView.OnChildClickListener getChildClickListener() {
        if (childClickListener == null) {
            childClickListener = new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                    String text = et.getText().toString();
                    int count = Integer.valueOf(tvTaskCountValue.getText().toString());
                    int maxAccumulation = Integer.valueOf(tvTaskMaxAccumulate.getText().toString());

                    if (!text.isEmpty() || !text.equals("")) {
                        TasksRealmController.addTask(text, count , maxAccumulation, cycling, priority,
                                ((Pair<String, Long>) view.getTag()).second );
                        priority = 0;
                        cycling = false;
                        et.setText("");
                    } else {
                        tasksListId = ((Pair<String, Long>) view.getTag()).second;
                        tasksDataChanged();
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }
                    return false;
                }
            };
        }
        return childClickListener;
    }



}
