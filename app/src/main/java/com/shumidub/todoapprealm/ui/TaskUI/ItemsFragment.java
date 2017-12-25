package com.shumidub.todoapprealm.ui.TaskUI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.bd.ItemsBD;
import com.shumidub.todoapprealm.model.TasksRealmController;
import com.shumidub.todoapprealm.ui.TaskUI.adapters.ItemsRecyclerViewAdapter;

import java.util.List;

/**
 * Created by Артем on 19.12.2017.
 */

public class ItemsFragment extends Fragment {

    public static ItemsFragment newInstance(long listId){
        ItemsFragment itemFragment = new ItemsFragment();

        Bundle arg = new Bundle();
        arg.putLong("listId", listId);
        itemFragment.setArguments(arg);
        return itemFragment;
    }

    public ItemsRecyclerViewAdapter itemsRecyclerViewAdapter;
    TasksRealmController realmController;



    RecyclerView rvItems;
    EditText etItems;


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
//        etItems = view.findViewById(R.id.et);

        long listId = getArguments().getLong("listId", 0);

        List<ItemsBD> items;

        if (listId == 0) items = realmController.getItems();
        else  items = realmController.getItems(listId);


        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        itemsRecyclerViewAdapter = new ItemsRecyclerViewAdapter(items);
        rvItems.setAdapter(itemsRecyclerViewAdapter);








    }


}
