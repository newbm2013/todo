package com.shumidub.todoapprealm.ui.fragment.folder_panel_sliding_fragment;

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
import com.shumidub.todoapprealm.realmmodel.FolderObject;


import java.util.concurrent.TimeUnit;

import io.realm.RealmResults;

/**
 * Created by user on 22.01.18.
 */

public class FolderOfTaskRecyclerViewAdapter
        extends RecyclerView.Adapter<FolderOfTaskRecyclerViewAdapter.ViewHolder> {

    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 0;

    RealmResults<FolderObject> realmResults;
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


    public FolderOfTaskRecyclerViewAdapter(RealmResults<FolderObject> realmResults, Activity activity){
        this.realmResults = realmResults;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_FOOTER){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_add_new_list, parent, false);
            return new FooterViewHolder(view);
        }else if (viewType == TYPE_ITEM){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
            return new ItemViewHolder(view);
        } else return null;
    }

    @SuppressLint({"All", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("DTAG", "onBindViewHolder: position" + position + "array size = " + realmResults.size());
        if (holder.getItemViewType() == TYPE_ITEM) {
            ((ItemViewHolder) holder).textView.setText("" + realmResults.get(position).getName());
            ((ItemViewHolder) holder).textView.setTag(realmResults.get(position).getId());

            ((ItemViewHolder) holder).textView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        then = System.currentTimeMillis();

                        runnable = new Runnable() {
                            @Override
                            public void run() {
                               try {
                                    TimeUnit.MILLISECONDS.sleep(longClickDuration);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                v.post(() ->  {
                                    if ((System.currentTimeMillis() - then) > longClickDuration) {
                                        onHolderTextViewOnLongClickListener.onLongClick(holder, position);
                                    }
                                });
                            }
                        };

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
        return realmResults.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == realmResults.size()){
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



