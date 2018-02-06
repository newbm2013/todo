package com.shumidub.todoapprealm.ui.fragment.report_section.report_fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmmodel.report.ReportObject;

import java.util.Calendar;
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
            holder.tvDayCount.setText(String.valueOf(reportObject.getCountOfDay()));
            holder.tvRetortText.setText(reportObject.getReportText());
            holder.ratingBarSoul.setRating(reportObject.getSoulRating());
            holder.ratingBarSoul.setIsIndicator(true);
            holder.ratingBarHealth.setRating(reportObject.getHealthRating());
            holder.ratingBarHealth.setIsIndicator(true);
            holder.itemView.setTag(reportObject.getId());
            holder.itemView.setOnClickListener((view)-> onItemClicked.onClick(view, position, reportObject.getId() ));
            holder.itemView.setOnLongClickListener((View view) -> {
                return onItemLongClicked.onLongClick(view, position, reportObject.getId());
            });

            if (reportObject.isWeekReport()){
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(reportObject.getId());
                int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
                holder.tvDate.setText("Week" + String.valueOf(weekNumber));
            }
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