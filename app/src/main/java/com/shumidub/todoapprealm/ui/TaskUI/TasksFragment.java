package com.shumidub.todoapprealm.ui.TaskUI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.model.TaskModel;
import com.shumidub.todoapprealm.realmcontrollers.TasksRealmController;
import com.shumidub.todoapprealm.ui.TaskUI.adapters.DoneTasksRecyclerViewAdapter;
import com.shumidub.todoapprealm.ui.TaskUI.adapters.TasksRecyclerViewAdapter;

import java.util.List;

import static com.shumidub.todoapprealm.App.TAG;

/**
 * Created by Артем on 19.12.2017.
 */

public class TasksFragment extends Fragment {

    long listId;
    public TasksRecyclerViewAdapter tasksRecyclerViewAdapter;
    TasksRealmController realmController;
    RecyclerView rvItems;
    List<TaskModel> tasks;




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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_layout, null);
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

        List<TaskModel> doneTasks;
        if (listId == 0) doneTasks = realmController.getNotDoneTasks();
        else doneTasks = realmController.getNotDoneTasks(listId);


        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        rvItems.setLayoutManager(llm);

        rvItems.setAdapter(new TasksRecyclerViewAdapter(tasks));



















    }
}
