package com.shumidub.todoapprealm.ui.fragment.task_section.folder_panel_sliding_fragment.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmmodel.FolderTaskObject;
import com.shumidub.todoapprealm.realmmodel.RealmInteger;
import com.shumidub.todoapprealm.realmmodel.TaskObject;


import java.util.Calendar;

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

        setFolderTaskCounts(holder, position);

    }


    @Override
    public int getItemCount() {
        return realmListFolder.size();
    }


    private void setFolderTaskCounts(ViewHolder holder, int position){
        int todayDate = Integer.valueOf("" + Calendar.getInstance().get(Calendar.DAY_OF_YEAR) +
                Calendar.getInstance().get(Calendar.YEAR));

        int done = 0;
        int all = 0;

        RealmList<TaskObject> realmList = realmListFolder.get(position).getTasks();

        for (TaskObject task: realmList){

            all = all + (task.getCountValue() * task.getMaxAccumulation());

            if (false) { // todo  not day folder

                int equalDateCount = 0;
                for (RealmInteger realmInteger : task.getDateCountAccumulation()) {
                    equalDateCount++;
                }
                done = done + equalDateCount * task.getCountValue();

            } else {

                if (task.getLastDoneDate() == todayDate) {
                    int equalDateCount = 0;
                    for (RealmInteger realmInteger : task.getDateCountAccumulation()) {
                        if (realmInteger.getMyInteger() == todayDate) {
                            equalDateCount++;
                        }
                    }
                    done = done + equalDateCount * task.getCountValue();
                }
            }




        }

        String folderTaskCounts = String.format("%d / %d", done, all);
        ((ItemViewHolder) holder).tvFolderTaskCounts.setText(folderTaskCounts);
        if (!realmListFolder.get(position).isDaily){
            ((ItemViewHolder) holder).tvFolderTaskCounts.setTextColor(Color.BLUE);
        } else {
            //todo default color ?
//            ((ItemViewHolder) holder).tvFolderTaskCounts.setTextColor(Color.BLUE);
        }


    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ItemViewHolder extends ViewHolder {
        TextView textView;
        TextView tvFolderTaskCounts;

        public ItemViewHolder(View itemView) {
            super(itemView);
//          textView = itemView.findViewById(R.id.item_text);
            textView = itemView.findViewById(R.id.tv_note_text);
            tvFolderTaskCounts = itemView.findViewById(R.id.tvFolderTaskCounts);
        }
    }


    public void setOnHolderTextViewOnClickListener(OnHolderTextViewOnClickListener onHolderTextViewOnClickListener){
        this.onHolderTextViewOnClickListener = onHolderTextViewOnClickListener;
    }

    public void setOnHolderTextViewOnLongClickListener(OnHolderTextViewOnLongClickListener onHolderTextViewOnLongClickListener){
        this.onHolderTextViewOnLongClickListener = onHolderTextViewOnLongClickListener;
    }



}



