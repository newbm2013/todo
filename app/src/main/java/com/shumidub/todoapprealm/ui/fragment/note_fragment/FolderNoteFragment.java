package com.shumidub.todoapprealm.ui.fragment.note_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shumidub.todoapprealm.R;

/**
 * Created by Артем on 19.12.2017.
 *
 */

public class FolderNoteFragment extends Fragment{

    RecyclerView rv;
    FolderNotesRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_fragment_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.recycle_view);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter();


    }

    protected void setAdapter(){
        adapter = new FolderNotesRecyclerViewAdapter();
        adapter.setOnClickListener((h,p,id)->{

        });
        adapter.setOnLongClickListener((h,p,id)->{

            return true;
        });
        rv.setAdapter(adapter);
    }
}
