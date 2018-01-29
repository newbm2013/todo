package com.shumidub.todoapprealm.ui.fragment.small_tasks_fragment;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmcontrollers.TasksRealmController;
import com.shumidub.todoapprealm.realmmodel.TaskObject;


import java.util.List;

/**
 * Created by Артем on 19.12.2017.
 */

public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.ViewHolder> {

    private List<TaskObject> tasks;
    private List<TaskObject> doneTasks;
    private boolean isNotEmpty;
    private static final int FOOTER_VIEW = 123;
    SmallTasksFragment smallTasksFragment;
    OnItemLongClicked onItemLongClicked;
    OnItemClicked onItemClicked;

    public interface OnItemLongClicked{
        void onLongClick (View view, int position);
    }

    public interface OnItemClicked{
        void onClick (View view, int position);
    }

    public void setOnLongClicked(OnItemLongClicked onItemLongClicked){
        this.onItemLongClicked = onItemLongClicked;
    }

    public void setOnClicked(OnItemClicked onItemClicked){
        this.onItemClicked = onItemClicked;
    }


    public TasksRecyclerViewAdapter(List<TaskObject> tasks, List<TaskObject> doneTasks, SmallTasksFragment smallTasksFragment){
        this.tasks = tasks;
        this.doneTasks = doneTasks;
        this.smallTasksFragment = smallTasksFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if ((tasks != null && !tasks.isEmpty() && tasks.size() > 0)
                || (doneTasks!=null && !doneTasks.isEmpty() && doneTasks.size()>0)) {

            isNotEmpty = true;

            if(viewType!=FOOTER_VIEW) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card_view, parent, false);
                return new NormalViewHolder(view);
            }else{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card_view_done_tasks, parent, false);
                return new FooterViewHolder(view);
            }

        }else{
            isNotEmpty = false;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_empty_state, parent, false);
            return new ViewHolder(view);
        }
    }

    private void setTasksTextColor(ViewHolder holder, boolean isDone){
        if (isDone){
            holder.textView.setTextColor(Color.GRAY);
        }else if(!isDone){
            holder.textView.setTextColor(Color.BLACK);
        }
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isNotEmpty ) {

            if (holder instanceof NormalViewHolder) {

                TaskObject taskObject = tasks.get(position);

                long taskId = taskObject.getId();
                String text = taskObject.getText();

                holder.textView.setText(text);
                holder.textView.setTag(taskId);
                holder.tvCount.setText("" + taskObject.getCountValue());
                holder.tvAccumulation.setText(taskObject.getCountAccumulation() + "/" + taskObject.getMaxAccumulation());

                int priority = taskObject.getPriority();
                String textPriority = "";

                while (priority>0){
                    textPriority += "!";
                    priority-=1;
                }

                holder.tvPriority.setText(textPriority);

                int color = taskObject.isCycling() ? Color.RED : Color.WHITE;
                holder.tvCycling.setTextColor(color);

                holder.checkBox.setChecked(taskObject.isDone());
                setTasksTextColor(holder, taskObject.isDone());

                holder.checkBox.setOnClickListener(
                        (cb) -> {
                            TasksRealmController.setTaskDoneOrParticullaryDone(taskObject, holder.checkBox.isChecked());
                            notifyDataSetChanged();
                            smallTasksFragment.getActivity().invalidateOptionsMenu();
                            setTasksTextColor(holder, taskObject.isDone());
                        });

                holder.textView.setOnLongClickListener((View view) -> {
                    Log.d("DTAG", "onLongClick: " + view.toString() + " " + position);
                    onItemLongClicked.onLongClick(view, position);
                    return true;
                });

                holder.textView.setOnClickListener((View view) -> {
                    if (onItemClicked!=null) onItemClicked.onClick(view, position);
                });
            }
            else if (holder instanceof FooterViewHolder){
                holder.textViewDoneTask.setText("Done " + smallTasksFragment.doneTasks.size() + " tasks");
                holder.textViewDoneTask.setTag("footer");
                holder.textViewDoneTask.setOnClickListener((v) -> smallTasksFragment.showAllTasks());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == tasks.size() && tasks.size() > 0) {
            return FOOTER_VIEW;
        }if (tasks.size() <= 0 && doneTasks.size()>0
//                && position==tasks.size()
                ) {
            return FOOTER_VIEW;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return ((tasks != null && !tasks.isEmpty() && tasks.size() > 0))
                || (doneTasks!=null && !doneTasks.isEmpty() && doneTasks.size()>0)
                ? tasks.size()+1 : 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        TextView tvCount;
        TextView tvPriority;
        TextView tvCycling;
        CheckBox checkBox;
        TextView textViewDoneTask;
        TextView tvAccumulation;

        public ViewHolder(View itemView) {
            super(itemView);

            if(isNotEmpty) {
                textView = itemView.findViewById(R.id.tv);
                checkBox = itemView.findViewById(R.id.checkbox);
                tvCount = itemView.findViewById(R.id.task_value);
                tvPriority = itemView.findViewById(R.id.task_priority);
                tvCycling = itemView.findViewById(R.id.task_cycling);
                textViewDoneTask = itemView.findViewById(R.id.tv_done_tasks);
                tvAccumulation = itemView.findViewById(R.id.task_accumulation);
            }
        }
    }

    public class FooterViewHolder extends ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class NormalViewHolder extends ViewHolder {
        public NormalViewHolder(View itemView) {
            super(itemView);
        }
    }
}