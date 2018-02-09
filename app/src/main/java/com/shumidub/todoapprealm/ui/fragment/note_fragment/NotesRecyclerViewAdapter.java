package com.shumidub.todoapprealm.ui.fragment.note_fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmmodel.notes.FolderNotesObject;
import com.shumidub.todoapprealm.realmmodel.notes.NoteObject;

import io.realm.RealmList;

/**
 * Created by A.shumidub on 07.02.18.
 *
 */

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> {

    RealmList<NoteObject> notesList;
    OnClickListener onClickListener;
    OnLongClickListener onLongClickListener;

    public interface OnClickListener {
        void onClick(ViewHolder holder, int position, long id);
    }
    public interface OnLongClickListener {
        boolean onLongClick(ViewHolder holder, int position, long id);
    }

    public NotesRecyclerViewAdapter(long folderNotesId) {
        App.initRealm();
        notesList = App.realm.where(FolderNotesObject.class)
                .equalTo("id", folderNotesId).findFirst().getTasks();
    }

    @Override
    public NotesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotesRecyclerViewAdapter.ViewHolder holder, int position) {
        long id = notesList.get(position).getId();

        holder.text.setText(notesList.get(position).getText());
        holder.text.setTag(id);

        holder.text.setOnClickListener((v)-> onClickListener.onClick(holder,position,id));
        holder.text.setOnLongClickListener((v)-> onLongClickListener.onLongClick(holder,position,id));

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.tv_note_text);
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }


}
