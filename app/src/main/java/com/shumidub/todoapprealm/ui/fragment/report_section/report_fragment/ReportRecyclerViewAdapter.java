package com.shumidub.todoapprealm.ui.fragment.report_section.report_fragment;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmcontrollers.taskcontroller.TasksRealmController;
import com.shumidub.todoapprealm.realmmodel.TaskObject;
import com.shumidub.todoapprealm.realmmodel.report.ReportObject;
import com.shumidub.todoapprealm.ui.fragment.task_section.small_tasks_fragment.SmallTasksFragment;

import java.util.List;

/**
 * Created by Артем on 19.12.2017.
 */

public class ReportRecyclerViewAdapter extends RecyclerView.Adapter<ReportRecyclerViewAdapter.ViewHolder> {

    private List<ReportObject> reportObjects;

    private OnItemLongClicked onItemLongClicked;
    private OnItemClicked onItemClicked;
    private ItemTouchHelper itemTouchHelper;
    private ItemTouchHelper.SimpleCallback itemTouchHelperSimpleCallback;

    public interface OnItemLongClicked{
        boolean onLongClick(View view, int position, long idReportObject);
    }

    public interface OnItemClicked{
        void onClick(View view, int position, long idReportObject);
    }

    public void setOnLongClicked(OnItemLongClicked onItemLongClicked){
        this.onItemLongClicked = onItemLongClicked;
    }

    public void setOnClicked(OnItemClicked onItemClicked){
        this.onItemClicked = onItemClicked;
    }


    public ReportRecyclerViewAdapter(List<ReportObject> reportObjects){
       this.reportObjects = reportObjects;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_card_view, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!reportObjects.isEmpty()) {

            ReportObject reportObject = reportObjects.get(position);

            holder.tvDate.setText(reportObject.getDate());
            holder.tvDayCount.setText(reportObject.getCountOfDay());
            holder.tvRetortText.setText(reportObject.getReportText());
            holder.ratingBarSoul.setRating(1);
            holder.ratingBarHealth.setRating(1);

            holder.itemView.setTag(reportObject.getId());

            holder.itemView.setOnClickListener((view)-> onItemClicked.onClick(view, position, reportObject.getId() ));
            holder.itemView.setOnLongClickListener((View view) -> {
                return onItemLongClicked.onLongClick(view, position, reportObject.getId());
            });
        }
    }


    @Override
    public int getItemCount() {
        return reportObjects.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate;
        TextView tvDayCount;
        TextView tvRetortText;

        RatingBar ratingBarSoul;
        RatingBar ratingBarHealth;


        public ViewHolder(View itemView) {
            super(itemView);

            if(!reportObjects.isEmpty()) {
                tvDate = itemView.findViewById(R.id.tv_date);
                tvDayCount = itemView.findViewById(R.id.tv_count_value);
                tvRetortText = itemView.findViewById(R.id.tv_report_text);
                ratingBarSoul = itemView.findViewById(R.id.ratingbar_soul);
                ratingBarHealth = itemView.findViewById(R.id.ratingbar_health);
            }
        }
    }




}