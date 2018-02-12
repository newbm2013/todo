package com.shumidub.todoapprealm.ui.fragment.task_section.folder_panel_sliding_fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmmodel.FolderTaskObject;


import java.util.concurrent.TimeUnit;

import io.realm.RealmList;

/**
 * Created by user on 22.01.18.
 */

public class FolderOfTaskRecyclerViewAdapter
        extends RecyclerView.Adapter<FolderOfTaskRecyclerViewAdapter.ViewHolder> {

    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 0;

    RealmList<FolderTaskObject> realmListFolder;
    OnHolderTextViewOnClickListener onHolderTextViewOnClickListener;
    OnHolderTextViewOnLongClickListener onHolderTextViewOnLongClickListener;
    OnFooterTextViewOnClickListener onFooterTextViewOnClickListener;

    Thread thread;
    Runnable runnable;

    long then;
    int longClickDuration = 1100;
    int middleClickDuration = 400;

    public interface OnHolderTextViewOnClickListener {
       void onClick(ViewHolder holder, int position);
    }
    public interface OnHolderTextViewOnLongClickListener {
       void onLongClick(ViewHolder holder, int position);
    }

    public interface OnFooterTextViewOnClickListener {
       void onClick(ViewHolder holder, int position);
    }


    public FolderOfTaskRecyclerViewAdapter(RealmList<FolderTaskObject> realmListFolder, Activity activity){
        this.realmListFolder = realmListFolder;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_FOOTER){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_card_view_add_new_folder, parent, false);
            return new FooterViewHolder(view);
        }else if (viewType == TYPE_ITEM){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_card_view, parent, false);
            return new ItemViewHolder(view);
        } else return null;
    }

    @SuppressLint({"All", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("DTAG", "onBindViewHolder: position" + position + "array size = " + realmListFolder.size());
        if (holder.getItemViewType() == TYPE_ITEM) {
            ((ItemViewHolder) holder).textView.setText("" + realmListFolder.get(position).getName());
            ((ItemViewHolder) holder).textView.setTag(realmListFolder.get(position).getId());

            ((ItemViewHolder) holder).textView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        then = System.currentTimeMillis();


                        thread = new Thread(runnable);
                        thread.start();
                    } else {
                        if (thread.getState()!= Thread.State.TERMINATED && thread != null){
                            thread.interrupt();
                        }
                    }

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if ((System.currentTimeMillis() - then) > middleClickDuration) {
                            return false;
                        }else {
                            if (thread.getState()== Thread.State.RUNNABLE){
                                thread.interrupt();
                            }
                            onHolderTextViewOnClickListener.onClick(holder, position);
                            return false;
                        }
                    }
                    return true;
                }
            });

        } else if(holder.getItemViewType() == TYPE_FOOTER){
            ((FooterViewHolder) holder).footerTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onFooterTextViewOnClickListener.onClick(holder, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return realmListFolder.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == realmListFolder.size()){
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
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
            textView = itemView.findViewById(R.id.item_text);
        }
    }

    public class FooterViewHolder extends ViewHolder {
        TextView footerTextView;

        public FooterViewHolder(View itemView) {
            super(itemView);
            footerTextView = itemView.findViewById(R.id.item_text);
        }
    }

    public void setOnHolderTextViewOnClickListener(OnHolderTextViewOnClickListener onHolderTextViewOnClickListener){
        this.onHolderTextViewOnClickListener = onHolderTextViewOnClickListener;
    }

    public void setOnHolderTextViewOnLongClickListener(OnHolderTextViewOnLongClickListener onHolderTextViewOnLongClickListener){
        this.onHolderTextViewOnLongClickListener = onHolderTextViewOnLongClickListener;
    }

    public void setOnFooterTextViewSetOnClickListener(OnFooterTextViewOnClickListener onFooterTextViewOnClickListener){
        this.onFooterTextViewOnClickListener = onFooterTextViewOnClickListener;
    }
}



