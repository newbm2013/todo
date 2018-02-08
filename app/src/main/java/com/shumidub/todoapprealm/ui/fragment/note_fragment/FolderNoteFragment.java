package com.shumidub.todoapprealm.ui.fragment.note_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.ui.activity.main.MainActivity;
import com.shumidub.todoapprealm.ui.dialog.note_dialog.AddNoteDialog;
import com.shumidub.todoapprealm.ui.fragment.task_section.folder_panel_sliding_fragment.FolderSlidingPanelFragment;


/**
 * Created by Артем on 19.12.2017.
 *
 */

public class FolderNoteFragment extends Fragment{

    RecyclerView rv;
    FolderNotesRecyclerViewAdapter adapter;
    ActionBar actionBar;
    ViewGroup container;

    interface IOnClick{
        void doOnClick(long id);
    }

    IOnClick onClick;

    long id = 0;
    int type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_fragment_layout, container, false);
        this.container = container;
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.recycle_view);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter();
        actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        actionBar.setTitle("Notes");
    }

    protected void setAdapter(){

        //todo need replace
        type = AddNoteDialog.TYPE_FOLDER;

        adapter = new FolderNotesRecyclerViewAdapter();
        adapter.setOnClickListener((h,p,id)->{

            onClick.doOnClick(id);

//            for (Fragment fragment: getActivity().getSupportFragmentManager().getFragments()){
//                if (fragment instanceof NoteFragmentContainer){
//                    ((NoteFragmentContainer) fragment).startNoteFragment(id);
//                }
//            }

//            NoteFragmentContainer.fragmentManager.
//                    beginTransaction()
//                    .replace(container.getId(), NoteFragment.newInstance(id))
//                    .commit();
//


        });
        adapter.setOnLongClickListener((h,p,id)->{

            return true;
        });
        rv.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem add = menu.add("add ");
        add.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        add.setIcon(R.drawable.ic_add);
        add.setOnMenuItemClickListener((MenuItem a) -> {
            AddNoteDialog addNoteDialog = AddNoteDialog.newInstance(type, id);
            try {
                addNoteDialog.show(getActivity().getSupportFragmentManager(), "Add_note");
            }catch (NullPointerException e){e.printStackTrace();}


//            new AddReportDialog().show(getActivity().getSupportFragmentManager(), AddReportDialog.ADD_REPORT_TITLE);
            return true;
        });

    }

    public void notifyDataChanged(){
       adapter.notifyDataSetChanged();
    }


    public void setIOnClick(IOnClick iOnClick){
        onClick = iOnClick;
    }

}
