package com.shumidub.todoapprealm.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.bd.ItemsBD;
import com.shumidub.todoapprealm.model.RealmController;

import java.util.List;

import io.realm.Realm;

/**
 * Created by Артем on 19.12.2017.
 */

public class ItemsFragment extends Fragment {

    public ItemsRecyclerViewAdapter itemsRecyclerViewAdapter;
    RealmController realmController;
    FloatingActionButton addFab;

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
        if (realmController == null) realmController = new RealmController();
        rvItems = view.findViewById(R.id.rv_items);
//        etItems = view.findViewById(R.id.et);
        List<ItemsBD> items = realmController.getItems();
        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        itemsRecyclerViewAdapter = new ItemsRecyclerViewAdapter(items);
        rvItems.setAdapter(itemsRecyclerViewAdapter);




    }


}
