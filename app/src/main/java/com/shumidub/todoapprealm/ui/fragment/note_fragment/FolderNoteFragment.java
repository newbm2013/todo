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
import com.shumidub.todoapprealm.realmcontrollers.notescontroller.FolderNotesRealmController;
import com.shumidub.todoapprealm.ui.activity.main.MainActivity;
import com.shumidub.todoapprealm.ui.dialog.note_dialog.AddNoteDialog;
import com.shumidub.todoapprealm.ui.fragment.task_section.folder_panel_sliding_fragment.FolderSlidingPanelFragment;

import static com.shumidub.todoapprealm.ui.dialog.note_dialog.AddNoteDialog.TYPE_NOTE;


/**
 * Created by Артем on 19.12.2017.
 *
 */

public class FolderNoteFragment extends Fragment{

    ActionBar actionBar;

    RecyclerView rv;

    FolderNotesRecyclerViewAdapter folderAdapter;
    NotesRecyclerViewAdapter noteAdapter;

    int type = AddNoteDialog.TYPE_FOLDER;

    public boolean isNoteFragment = false;

    String title = "Notes";


    interface IOnClick{
        void doOnClick(long idFolderFromAdapter);
    }

    IOnClick onClick = new IOnClick() {
        @Override
        public void doOnClick(long idFolderFromAdapter) {
            setNoteViews(idFolderFromAdapter);
        }
    };

    long idFolderNoteObject = 0;
    long idNoteObject = 0;
    long id;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_fragment_layout, container, false);
        actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.recycle_view);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        setFolderNoteViews();
        actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        actionBar.setTitle("Notes");
        id = idFolderNoteObject;
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
        try {
            folderAdapter.notifyDataSetChanged();
        }catch(NullPointerException e){}

        try {
            noteAdapter.notifyDataSetChanged();
        }catch (NullPointerException e){}

    }


    public void setIOnClick(IOnClick iOnClick){
        onClick = iOnClick;
    }


    public void setFolderNoteViews(){
        title = "Notes";
        actionBar.setTitle(title);

        type = AddNoteDialog.TYPE_FOLDER;
        isNoteFragment = false;

        folderAdapter = new FolderNotesRecyclerViewAdapter();
        folderAdapter.setOnClickListener((h,p,idFolderFromAdapter)->{

            onClick.doOnClick(idFolderFromAdapter);


        });
        folderAdapter.setOnLongClickListener((h,p,id)->{

            return true;
        });
        rv.setAdapter(folderAdapter);

    }

    public void setNoteViews(long idFolderFromAdapter){
        title = "<  " + FolderNotesRealmController.getFolderNote(idFolderFromAdapter).getName();
        actionBar.setTitle(title);

        isNoteFragment = true;

        type = TYPE_NOTE;
        id = idFolderFromAdapter;

        noteAdapter = new NotesRecyclerViewAdapter(idFolderFromAdapter);
        rv.setAdapter(noteAdapter);
        noteAdapter.setOnClickListener((h,p,id)->{

        });
        noteAdapter.setOnLongClickListener((h,p,id)->{

            return true;
        });
        rv.setAdapter(noteAdapter);
    }

}
