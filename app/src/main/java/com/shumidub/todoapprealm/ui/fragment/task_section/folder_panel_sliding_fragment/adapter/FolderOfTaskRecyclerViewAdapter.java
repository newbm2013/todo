package com.shumidub.todoapprealm.ui.fragment.task_section.folder_panel_sliding_fragment.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmmodel.FolderTaskObject;


import io.realm.RealmList;

/**
 * Created by user on 22.01.18.
 */

public class FolderOfTaskRecyclerViewAdapter
        extends RecyclerView.Adapter<FolderOfTaskRecyclerViewAdapter.ViewHolder> {


    RealmList<FolderTaskObject> realmListFolder;
    OnHolderTextViewOnClickListener onHolderTextViewOnClickListener;
    OnHolderTextViewOnLongClickListener onHolderTextViewOnLongClickListener;


    public interface OnHolderTextViewOnClickListener {
       void onClick(ViewHolder holder, int position);
    }
    public interface OnHolderTextViewOnLongClickListener {
       void onLongClick(ViewHolder holder, int position);
    }

    public FolderOfTaskRecyclerViewAdapter(RealmList<FolderTaskObject> realmListFolder, Activity activity){
        this.realmListFolder = realmListFolder;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
//        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_card_view, parent, false);
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_tasks_item_card_view, parent, false);

        return new ItemViewHolder(view);
    }

    @SuppressLint({"All", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("DTAG", "onBindViewHolder: position" + position + "array size = " + realmListFolder.size());

            ((ItemViewHolder) holder).textView.setText("" + realmListFolder.get(position).getName());
            ((ItemViewHolder) holder).textView.setTag(realmListFolder.get(position).getId());

            ((ItemViewHolder) holder).textView.setOnClickListener(
                    (v)->onHolderTextViewOnClickListener.onClick(holder, position));
            ((ItemViewHolder) holder).textView.setOnLongClickListener(
                    (v)->{
                        onHolderTextViewOnLongClickListener.onLongClick(holder, position);
                        return true;
                    }
            );

    }

    @Override
    public int getItemCount() {
        return realmListFolder.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ItemViewHolder extends ViewHolder {
        TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
//          textView = itemView.findViewById(R.id.item_text);
            textView = itemView.findViewById(R.id.tv_note_text);
        }
    }


    public void setOnHolderTextViewOnClickListener(OnHolderTextViewOnClickListener onHolderTextViewOnClickListener){
        this.onHolderTextViewOnClickListener = onHolderTextViewOnClickListener;
    }

    public void setOnHolderTextViewOnLongClickListener(OnHolderTextViewOnLongClickListener onHolderTextViewOnLongClickListener){
        this.onHolderTextViewOnLongClickListener = onHolderTextViewOnLongClickListener;
    }

}



