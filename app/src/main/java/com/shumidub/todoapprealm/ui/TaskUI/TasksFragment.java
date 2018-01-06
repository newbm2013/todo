package com.shumidub.todoapprealm.ui.TaskUI;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.model.TaskModel;
import com.shumidub.todoapprealm.realmcontrollers.ListsRealmController;
import com.shumidub.todoapprealm.realmcontrollers.TasksRealmController;
import com.shumidub.todoapprealm.ui.TaskUI.adapters.DoneTasksRecyclerViewAdapter;
import com.shumidub.todoapprealm.ui.TaskUI.adapters.TasksRecyclerViewAdapter;

import java.util.List;

import static com.shumidub.todoapprealm.App.TAG;
import static com.shumidub.todoapprealm.ui.CategoryUI.activity.CategoryActivity.listId;

/**
 * Created by Артем on 19.12.2017.
 */

public class TasksFragment extends Fragment {

    long listId;
    public TasksRecyclerViewAdapter tasksRecyclerViewAdapter;
    public TasksRealmController realmController;
    RecyclerView rvItems;
    public List<TaskModel> tasks;
    public List<TaskModel> doneTasks;
    public boolean isAllTaskShowing;
    LinearLayoutManager llm;




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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_layout, null);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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




        rvItems.setAdapter(new TasksRecyclerViewAdapter(tasks, this));



    }

    public void showAllTasks(){
        int position = llm.findFirstVisibleItemPosition();

        if (listId == 0) tasks = realmController.getTasks();
        else tasks = realmController.getTasks(listId);
        rvItems.setAdapter(new TasksRecyclerViewAdapter(tasks, this));

        rvItems.scrollToPosition(position);

        isAllTaskShowing = true;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        int count = 0;

        for (TaskModel task : doneTasks){
            count = count + task.getCountValue();
        }

        MenuItem countMenu = menu.add("" + count);
        countMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        super.onCreateOptionsMenu(menu, inflater);
    }
}
