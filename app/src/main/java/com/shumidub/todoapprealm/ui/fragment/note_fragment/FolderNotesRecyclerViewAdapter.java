package com.shumidub.todoapprealm.ui.fragment.note_fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmmodel.notes.FolderNotesObject;
import com.shumidub.todoapprealm.ui.actionmode.EmptyActionModeCallback;
import com.shumidub.todoapprealm.ui.activity.main.MainActivity;


import io.realm.RealmList;

/**
 * Created by A.shumidub on 07.02.18.
 *
 */

public class FolderNotesRecyclerViewAdapter extends RecyclerView.Adapter<FolderNotesRecyclerViewAdapter.ViewHolder> {

    RealmList<FolderNotesObject> folderNotesList;
    OnClickListener onClickListener;
    OnLongClickListener onLongClickListener;
    MainActivity activity;

    public interface OnClickListener {
        void onClick(ViewHolder holder, int position, long id);
    }
    public interface OnLongClickListener {
        boolean onLongClick(ViewHolder holder, int position, long id);
    }

    public FolderNotesRecyclerViewAdapter(MainActivity activity) {
        this.activity = activity;
        App.initRealm();
        folderNotesList = App.folderOfNotesContainerList;
    }

    @Override
    public FolderNotesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FolderNotesRecyclerViewAdapter.ViewHolder holder, int position) {
        long id = folderNotesList.get(position).getId();

        holder.text.setText(folderNotesList.get(position).getName());
        holder.text.setTag(id);

        holder.text.setOnClickListener((v)-> onClickListener.onClick(holder,position,id));
        holder.text.setOnLongClickListener((v)-> onLongClickListener.onLongClick(holder,position,id));
    }

    @Override
    public int getItemCount() {
        return folderNotesList.size();
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


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);


        // set ITEM TOUCH HELPER for folder rv
        App.initRealm();
        RealmList<FolderNotesObject> folderOfNotesContainerList = App.folderOfNotesContainerList;
        //todo try is it working, or need not use linked variable

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN ,0) {

            int dragFrom = -1;
            int dragTo = -1;

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {

                activity.getSupportActionBar().startActionMode(new EmptyActionModeCallback());

                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                if (dragFrom == -1) {
                    dragFrom = fromPosition;
                }
                dragTo = toPosition;

                notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            // todo need fix if move bellow "add list"
            private void reallyMoved(int from, int to) {
                Log.d("DTAG", "reallyMoved: ++++");
                App.initRealm();
                if (from < folderOfNotesContainerList.size() ){
                    App.realm.executeTransaction((realm) -> {
                        int to2 = to<folderOfNotesContainerList.size() ? to : folderOfNotesContainerList.size()-1;
                        folderOfNotesContainerList.add(to2, folderOfNotesContainerList.remove(from));
                        Log.d("DTAG", "reallyMoved: from " + from +  " to " + to2);
                    });
                }

            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

                if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
                    reallyMoved(dragFrom, dragTo);
                }
                dragFrom = dragTo = -1;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) { }

        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

    }
}
