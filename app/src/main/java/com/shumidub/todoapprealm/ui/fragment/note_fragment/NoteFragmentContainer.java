package com.shumidub.todoapprealm.ui.fragment.note_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.ui.activity.main.MainActivity;
import com.shumidub.todoapprealm.ui.dialog.note_dialog.AddNoteDialog;


/**
 * Created by Артем on 19.12.2017.
 *
 */

public class NoteFragmentContainer extends Fragment{

    FrameLayout container;
    static FragmentManager fragmentManager;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_fragment_container, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        container = view.findViewById(R.id.frame_layout);
        fragmentManager = getChildFragmentManager();
        FolderNoteFragment folderNoteFragment =  new FolderNoteFragment();


        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, folderNoteFragment).addToBackStack("note_folder").commit();


        folderNoteFragment.setIOnClick((id)->{
            Log.d("DTAG", "startNoteFragment: ");
            fragmentManager.
                    beginTransaction()
                    .replace(R.id.frame_layout, NoteFragment.newInstance(id))
                    .addToBackStack("note")
                    .commit();
        });
    }


    public void startNoteFragment(long id){
        Log.d("DTAG", "startNoteFragment: ");
            fragmentManager.
            beginTransaction()
            .replace(R.id.frame_layout, NoteFragment.newInstance(id))
            .commit();
    }

}
