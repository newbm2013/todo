package com.shumidub.todoapprealm.ui.fragment.task_section.small_tasks_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmcontrollers.FolderTaskRealmController;
import com.shumidub.todoapprealm.realmcontrollers.taskcontroller.TasksRealmController;
import com.shumidub.todoapprealm.realmmodel.TaskObject;
import com.shumidub.todoapprealm.ui.actionmode.EmptyActionModeCallback;
import com.shumidub.todoapprealm.ui.activity.main.MainActivity;
import com.shumidub.todoapprealm.ui.actionmode.task.TaskActionModeCallback;

import java.util.List;

/**
 * Created by Артем on 16.01.2018.
 */

public class SmallTasksFragment extends Fragment {


    //TASKS VIEW, ADAPTER
    RecyclerView rvTasks;
    LinearLayout emptyState;
    LinearLayoutManager llm;
    TasksRecyclerViewAdapter tasksRecyclerViewAdapter;
    static ActionMode actionMode;

    TasksRecyclerViewAdapter.OnItemLongClicked onItemLongClicked;
    TasksRecyclerViewAdapter.OnItemClicked onItemClicked;

    public List<TaskObject> tasks;
    public List<TaskObject> doneTasks;
    public boolean isAllTaskShowing;

    //TASKS
    long tasksFolderId;
    public static final String TASK_FOLDER_ID_KEY = "TASK_FOLDER_ID_KEY";

    public static final SmallTasksFragment newInstance(long tasksListId) {
        Bundle args = new Bundle();
        args.putLong(TASK_FOLDER_ID_KEY, tasksListId);
        SmallTasksFragment fragment = new SmallTasksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rv_fragment_template_layout, container, false);
        emptyState = view.findViewById(R.id.empty_state);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        TASK
        isAllTaskShowing = false;
        tasksFolderId = getArguments().getLong(TASK_FOLDER_ID_KEY, 0);
        if (FolderTaskRealmController.getFolder(tasksFolderId)==null) tasksFolderId =0;
        rvTasks = view.findViewById(R.id.rv);
        setTasksListClickListeners();
        setTasks();
        setEmptyStateIfNeed();
    }

    private void setTasksListClickListeners(){
        onItemLongClicked = (View view, int position) -> {
            long idTask = (Long) view.getTag();
            TaskObject task = TasksRealmController.getTask(idTask);
            ActionMode.Callback callback = new TaskActionModeCallback().getCallback(getActivity(), SmallTasksFragment.this, task);
            actionMode = getActivity().startActionMode(callback);
        };

        onItemClicked = (View view, int position) -> {
            if (view!=null){
                long idTask = (Long) view.getTag();
                TaskObject task = TasksRealmController.getTask(idTask);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(task.getText()).create().show();
            }
        };
    }

    public void setTasks(){
        if (tasksFolderId == 0) doneTasks = TasksRealmController.getDoneTasks();
        else doneTasks = TasksRealmController.getDoneTasks(tasksFolderId);

        if (tasksFolderId == 0) tasks = TasksRealmController.getNotDoneTasks();
        else tasks = TasksRealmController.getNotDoneTasks(tasksFolderId);

        if (tasksFolderId !=0){
            ((MainActivity) getActivity()).getSupportActionBar()
                    .setTitle((CharSequence) FolderTaskRealmController.getFolder(tasksFolderId).getName());
        }
        llm = new LinearLayoutManager(getContext());

        rvTasks.setLayoutManager(llm);
        tasksRecyclerViewAdapter = new TasksRecyclerViewAdapter((MainActivity) getActivity(), tasks, doneTasks, this);
        rvTasks.setAdapter(tasksRecyclerViewAdapter);

        tasksRecyclerViewAdapter.setOnLongClicked(onItemLongClicked);
        tasksRecyclerViewAdapter.setOnClicked(onItemClicked);
    }

    public void notifyDataChanged(){
//        //tasksRecyclerViewAdapter, reset task and done task
//        if (tasksRecyclerViewAdapter ==null){
//            tasksRecyclerViewAdapter =  new TasksRecyclerViewAdapter((MainActivity) getActivity(), tasks, doneTasks, this);
//            rvTasks.setAdapter(tasksRecyclerViewAdapter);
//            Log.d("DTAG", "notifyDataChanged: 1");
//        }
//        else{
////          setTasks(); or bellow
//            tasksRecyclerViewAdapter.notifyDataSetChanged();
//            Log.d("DTAG", "notifyDataChanged: 2");
//        }
//
//        try{
//            setEmptyStateIfNeed();
//            Log.d("DTAG", "notifyDataChanged: 3");
//        } catch (NullPointerException e){}


        rvTasks = getView().findViewById(R.id.rv);

        setTasks();
        setEmptyStateIfNeed();

    }

    //Show Done and Not done Tasks
    public void showAllTasks(){
        if(!isAllTaskShowing) {
            int position = llm.findFirstVisibleItemPosition();
            if (tasksFolderId == 0) tasks = TasksRealmController.getTasks();
            else tasks = TasksRealmController.getTasks(tasksFolderId);
            tasksRecyclerViewAdapter = new TasksRecyclerViewAdapter((MainActivity) getActivity(),tasks,doneTasks, this);
            tasksRecyclerViewAdapter.setOnLongClicked(onItemLongClicked);
            tasksRecyclerViewAdapter.setOnClicked(onItemClicked);
            rvTasks.setAdapter(tasksRecyclerViewAdapter);
            rvTasks.scrollToPosition(position);
            isAllTaskShowing = true;
        } else if (isAllTaskShowing){
            if (tasksFolderId == 0) tasks = TasksRealmController.getNotDoneTasks();
            else tasks = TasksRealmController.getNotDoneTasks(tasksFolderId);
            tasksRecyclerViewAdapter = new TasksRecyclerViewAdapter((MainActivity) getActivity(),tasks,doneTasks, this);
            tasksRecyclerViewAdapter.setOnLongClicked(onItemLongClicked);
            tasksRecyclerViewAdapter.setOnClicked(onItemClicked);
            rvTasks.setAdapter(tasksRecyclerViewAdapter);
            isAllTaskShowing = false;
        }
    }

    public void tasksDataChanged(){
        setTasks();
    }

    public void finishActionMode(){
        ((MainActivity) getActivity()).startSupportActionMode(new EmptyActionModeCallback());
    }

    private void setEmptyStateIfNeed(){


        if (tasksRecyclerViewAdapter.getItemCount() == 0){
            emptyState.setVisibility(View.VISIBLE);
        } else {
            emptyState.setVisibility(View.GONE);
        }
    }

}



