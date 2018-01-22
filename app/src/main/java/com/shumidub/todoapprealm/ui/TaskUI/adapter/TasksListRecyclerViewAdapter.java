package com.shumidub.todoapprealm.ui.TaskUI.adapter;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.model.ListModel;

import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * Created by user on 22.01.18.
 */

public class TasksListRecyclerViewAdapter
        extends RecyclerView.Adapter<TasksListRecyclerViewAdapter.ViewHolder> {

    RealmResults<ListModel> realmResults;


    public TasksListRecyclerViewAdapter(RealmResults<ListModel> realmResults, Activity activity){
        this.realmResults = realmResults;
        //activity as context

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText("" + realmResults.get(position).getName());
//        holder.itemView
        holder.textView.setTag(realmResults.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_text);
        }
    }
}
