package com.shumidub.todoapprealm.ui.fragment.note_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.ui.dialog.note_dialog.AddNoteDialog;

import static com.shumidub.todoapprealm.ui.dialog.note_dialog.AddNoteDialog.TYPE_NOTE;

/**
 * Created by Артем on 19.12.2017.
 *
 */

public class NoteFragment extends FolderNoteFragment{


    long id = 0;
    NotesRecyclerViewAdapter adapter;

    public static NoteFragment newInstance(long idFolderNote) {

        Bundle args = new Bundle();
        args.putLong("id", idFolderNote );
        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void setAdapter() {

        type = TYPE_NOTE;


        if (getArguments()!= null){
            id = getArguments().getLong("id", 0);
        }
        if (id !=0){
            adapter = new NotesRecyclerViewAdapter(id);
            adapter.setOnClickListener((h,p,id)->{

            });
            adapter.setOnLongClickListener((h,p,id)->{

                return true;
            });
            rv.setAdapter(adapter);
        }
    }

    public void notifyDataChanged(){
        adapter.notifyDataSetChanged();
    }

}
