package com.shumidub.todoapprealm.ui.TaskUI;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.model.TaskModel;
import com.shumidub.todoapprealm.realmcontrollers.ListsRealmController;
import com.shumidub.todoapprealm.realmcontrollers.TasksRealmController;
import com.shumidub.todoapprealm.ui.TaskUI.actionmode.TaskActionModeCallback;
import com.shumidub.todoapprealm.ui.TaskUI.adapters.TasksRecyclerViewAdapter;

import java.util.Calendar;
import java.util.List;

import static com.shumidub.todoapprealm.App.TAG;

/**
 * Created by Артем on 19.12.2017.
 */

public class TasksFragment extends Fragment {

    long listId;
//    public TasksRecyclerViewAdapter tasksRecyclerViewAdapter;
    TasksRecyclerViewAdapter adapter;
    public TasksRealmController realmController;
    RecyclerView rvItems;
    public List<TaskModel> tasks;
    public List<TaskModel> doneTasks;
    public boolean isAllTaskShowing;
    LinearLayoutManager llm;

    ActionBar actionBar;

    TasksRecyclerViewAdapter.OnItemLongClicked onItemLongClicked;
    TasksRecyclerViewAdapter.OnItemClicked onItemClicked;




    public static TasksFragment newInstance(long listId){
        TasksFragment itemFragment = new TasksFragment();
        Bundle arg = new Bundle();
        arg.putLong("listId", listId);
        itemFragment.setArguments(arg);
        return itemFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments()!=null){
            listId = getArguments().getLong("listId");
        } else listId = 0; // get default id + set toolbar

        Log.d(TAG, "onCreate: listId = " + listId);

        isAllTaskShowing = false;

        setHasOptionsMenu(true);


        onItemLongClicked = new TasksRecyclerViewAdapter.OnItemLongClicked() {
            @Override
            public void onLongClick(View view, int position) {

                Log.d("DTAG", "onLongClick: ");

                long idTask = (Long) view.getTag();
                TaskModel task = TasksRealmController.getTask(idTask);

                Log.d("DTAG", "onLongClick: " + idTask);

                actionBar = ((MainActivity)getActivity()).getSupportActionBar();
                ActionMode.Callback callback = new TaskActionModeCallback().getCallback(getActivity(), TasksFragment.this, task);
                ActionMode actionMode = getActivity().startActionMode(callback);

            }
        };


        onItemClicked = new TasksRecyclerViewAdapter.OnItemClicked() {
            @Override
            public void onClick(View view, int position) {

                long idTask = (Long) view.getTag();
                TaskModel task = TasksRealmController.getTask(idTask);

                Log.d("DTAG", "onClick: ");
            }
        };

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

        if (realmController == null) realmController = new TasksRealmController();
        rvItems = view.findViewById(R.id.rv_items);


        Log.d(TAG, "onViewCreated: listId = " + listId);

        if (listId == 0) tasks = realmController.getNotDoneTasks();
        else tasks = realmController.getNotDoneTasks(listId);

        if (listId !=0){
            ((MainActivity) getActivity()).getSupportActionBar()
                    .setTitle((CharSequence) ListsRealmController.getListById(listId).getName());
        }


        if (listId == 0) doneTasks = realmController.getDoneTasks();
        else doneTasks = realmController.getDoneTasks(listId);


        llm = new LinearLayoutManager(getContext());

        rvItems.setLayoutManager(llm);


        adapter = new TasksRecyclerViewAdapter(tasks, this);

        rvItems.setAdapter(adapter);

        adapter.setOnLongClicked(onItemLongClicked);



        adapter.setOnClicked(onItemClicked);


        




    }

    public void showAllTasks(){

        if(!isAllTaskShowing) {
            int position = llm.findFirstVisibleItemPosition();
            if (listId == 0) tasks = realmController.getTasks();
            else tasks = realmController.getTasks(listId);
            adapter = new TasksRecyclerViewAdapter(tasks, this);
            adapter.setOnLongClicked(onItemLongClicked);
            adapter.setOnClicked(onItemClicked);
            rvItems.setAdapter(adapter);
            rvItems.scrollToPosition(position);
            isAllTaskShowing = true;
        } else if (isAllTaskShowing){
            if (listId == 0) tasks = realmController.getNotDoneTasks();
            else tasks = realmController.getNotDoneTasks(listId);
            adapter = new TasksRecyclerViewAdapter(tasks, this);
            adapter.setOnLongClicked(onItemLongClicked);
            adapter.setOnClicked(onItemClicked);
            rvItems.setAdapter(adapter);
            isAllTaskShowing = false;
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        int count = 0;

        for (TaskModel task : doneTasks){
            String date = "" + Calendar.getInstance().get(Calendar.DAY_OF_YEAR) +
                    Calendar.getInstance().get(Calendar.YEAR);
            if (task.getLastDoneDate() == Integer.valueOf(date)) count = count + task.getCountValue();
        }

        MenuItem countMenu = menu.add("" + count);
        countMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void notifyDataChanged(){
        boolean b =adapter == null;
        Log.d("DTAG", "notifyDataChanged: " + adapter + " " + b );
        adapter.notifyDataSetChanged();
    }

}
