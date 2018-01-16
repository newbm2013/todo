package com.shumidub.todoapprealm.ui.TaskUI.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.model.TaskModel;
import com.shumidub.todoapprealm.realmcontrollers.ListsRealmController;
import com.shumidub.todoapprealm.realmcontrollers.TasksRealmController;
import com.shumidub.todoapprealm.sharedpref.SharedPrefHelper;
import com.shumidub.todoapprealm.ui.MainActivity;
import com.shumidub.todoapprealm.ui.TaskUI.actionmode.TaskActionModeCallback;
import com.shumidub.todoapprealm.ui.TaskUI.adapter.TasksRecyclerViewAdapter;

import java.util.List;

/**
 * Created by Артем on 16.01.2018.
 */

public class SmallTasksFragment extends Fragment {


    //TASKS VIEW, ADAPTER
    RecyclerView rvItems;
    LinearLayoutManager llm;
    TasksRecyclerViewAdapter adapter;
    ActionMode actionMode;

    TasksRecyclerViewAdapter.OnItemLongClicked onItemLongClicked;
    TasksRecyclerViewAdapter.OnItemClicked onItemClicked;

    public List<TaskModel> tasks;
    public List<TaskModel> doneTasks;
    public boolean isAllTaskShowing;

    //TASKS
    long tasksListId;
    public static final String TASKSK_LIST_ID_KEY = "TASKSK_LIST_ID_KEY";


    public static final SmallTasksFragment newInstance(long tasksListId) {

        Bundle args = new Bundle();
        args.putLong(TASKSK_LIST_ID_KEY, tasksListId);
        SmallTasksFragment fragment = new SmallTasksFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        TASK
        isAllTaskShowing = false;
//        long defaultListId = new SharedPrefHelper(getActivity()).getDefaultListId();
//        tasksListId = defaultListId;

        tasksListId = getArguments().getLong(TASKSK_LIST_ID_KEY, 0);

        if (ListsRealmController.getListById(tasksListId)==null) tasksListId=0;
        rvItems = view.findViewById(R.id.rv_items);
        setTasks();
        setTasksListClickListeners();



    }

    private void setTasksListClickListeners(){
        onItemLongClicked = new TasksRecyclerViewAdapter.OnItemLongClicked() {
            @Override
            public void onLongClick(View view, int position) {
                long idTask = (Long) view.getTag();
                TaskModel task = TasksRealmController.getTask(idTask);

                ActionMode.Callback callback = new TaskActionModeCallback().getCallback(getActivity(), TasksFragment.this, task);
                actionMode = getActivity().startActionMode(callback);
            }
        };

        onItemClicked = new TasksRecyclerViewAdapter.OnItemClicked() {
            @Override
            public void onClick(View view, int position) {
                if (view!=null){
                    long idTask = (Long) view.getTag();
                    TaskModel task = TasksRealmController.getTask(idTask);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(task.getText()).create().show();
                }
            }
        };
    }


    public void setTasks(){
        if (tasksListId == 0) doneTasks = TasksRealmController.getDoneTasks();
        else doneTasks = TasksRealmController.getDoneTasks(tasksListId);



        if (tasksListId == 0) tasks = TasksRealmController.getNotDoneTasks();
        else tasks = TasksRealmController.getNotDoneTasks(tasksListId);

        if (tasksListId !=0){
            ((MainActivity) getActivity()).getSupportActionBar()
                    .setTitle((CharSequence) ListsRealmController.getListById(tasksListId).getName());
        }
        llm = new LinearLayoutManager(getContext());
        rvItems.setLayoutManager(llm);
        adapter = new TasksRecyclerViewAdapter(tasks, doneTasks, this);
        rvItems.setAdapter(adapter);

        adapter.setOnLongClicked(onItemLongClicked);
        adapter.setOnClicked(onItemClicked);
    }

    public void notifyDataChanged(){

        //adapter, reset task and done task

        if (adapter==null){
            adapter =  new TasksRecyclerViewAdapter(tasks, doneTasks, this);
        }
        else{
//            setTasks(); or bellow
            adapter.notifyDataSetChanged();
        }
    }

    //Show Done and Not done Tasks
    public void showAllTasks(){
        if(!isAllTaskShowing) {
            int position = llm.findFirstVisibleItemPosition();
            if (tasksListId == 0) tasks = TasksRealmController.getTasks();
            else tasks = TasksRealmController.getTasks(tasksListId);
            adapter = new TasksRecyclerViewAdapter(tasks,doneTasks, this);
            adapter.setOnLongClicked(onItemLongClicked);
            adapter.setOnClicked(onItemClicked);
            rvItems.setAdapter(adapter);
            rvItems.scrollToPosition(position);
            isAllTaskShowing = true;
        } else if (isAllTaskShowing){
            if (tasksListId == 0) tasks = TasksRealmController.getNotDoneTasks();
            else tasks = TasksRealmController.getNotDoneTasks(tasksListId);
            adapter = new TasksRecyclerViewAdapter(tasks,doneTasks, this);
            adapter.setOnLongClicked(onItemLongClicked);
            adapter.setOnClicked(onItemClicked);
            rvItems.setAdapter(adapter);
            isAllTaskShowing = false;
        }
    }


    public void tasksDataChanged(){
        setTasks();
    }


    public void finishActionMode(){
        if (actionMode!=null) actionMode.finish();
    }

}



//todo клики актионмоды таск ресайкл вью адаптер , где лонг клик может использоваться, деваулт лист ид , запуск