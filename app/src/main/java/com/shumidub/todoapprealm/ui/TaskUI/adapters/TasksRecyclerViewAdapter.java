package com.shumidub.todoapprealm.ui.TaskUI.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.model.TaskModel;
import com.shumidub.todoapprealm.realmcontrollers.TasksRealmController;
import com.shumidub.todoapprealm.ui.TaskUI.TasksFragment;

import java.util.List;

import static com.shumidub.todoapprealm.App.TAG;
import static com.shumidub.todoapprealm.ui.CategoryUI.activity.CategoryActivity.listId;


/**
 * Created by Артем on 19.12.2017.
 */

public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.ViewHolder> {

    private List<TaskModel> tasks;
    private boolean isNotEmpty;


    private static final int FOOTER_VIEW = 123;

    TasksFragment tasksFragment;



    public TasksRecyclerViewAdapter(List<TaskModel> items, TasksFragment tasksFragment){
        this.tasks = items;
        this.tasksFragment = tasksFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (tasks != null && !tasks.isEmpty() && tasks.size() > 0) {
            isNotEmpty = true;

            if(viewType!=FOOTER_VIEW) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view, null, false);
                return new NormalViewHolder(view);
            }else{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.textview_done_tasks, parent, false);
                return new FooterViewHolder(view);
            }


        }else{
            isNotEmpty = false;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_empty_state, parent, false);
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

                TaskModel item = tasks.get(position);

                long taskId = item.getId();
                String text = item.getText();


                holder.textView.setText(text);
                holder.textView.setTag(taskId);


                holder.checkBox.setChecked(item.isDone());
                setTasksTextColor(holder, item.isDone());

                holder.checkBox.setOnClickListener(
                        (cb) -> {

                            TasksRealmController.setTaskDone(item, holder.checkBox.isChecked());
                            notifyDataSetChanged();
                            tasksFragment.getActivity().invalidateOptionsMenu();

//


                            setTasksTextColor(holder, item.isDone());
                        });
//
//
//
//            holder.textView.setOnClickListener( (a)->
//                    {Log.d(TAG+ "1", "CLICK: " +
//                            "\n " +
//                            "\nitem text = " + item.getText() +
//                            "\nitems.get(position).getText() = " + items.get(position).getText() +
//                            "\n " +
//                            "\nitem = " + item.hashCode() +
//                            "\nitems.get(position) =" + items.get(position).hashCode() +
//                            "\n " +
//                            "\nitem.taskID = " + item.getId() +
//                            "\nitems.get(position).taskID = " + items.get(position).getId() +
//                            "\ntaskID = " + taskId +
//                            "\n " +
//                            "\nitem.isDone =" + item.isDone() +
//                            "\nitemsget(position).isDone =" + items.get(position).isDone() );
//                     });
//
//
//            holder.textView.setOnLongClickListener((a)-> {
//                Log.d(TAG+ "1", "onLongClick: taskID " +items.get(position).getText()
//                    + " =" + taskId  +"/" + items.get(position).getId()
//                   );
//                return true;});
            }

            else if (holder instanceof FooterViewHolder){

                holder.textViewDoneTask.setText("Done " + tasksFragment.doneTasks.size() + " tasks");

                if (tasksFragment.isAllTaskShowing) holder.textViewDoneTask.setVisibility(View.INVISIBLE);


                    holder.textViewDoneTask.setOnClickListener((v) -> {
                        Toast.makeText(tasksFragment.getContext(), "+", Toast.LENGTH_SHORT).show();
                        tasksFragment.showAllTasks();
                    });


            }


        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == tasks.size() && tasks.size() > 0) {
            // This is where we'll add footer.
            return FOOTER_VIEW;
        }

        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return (tasks != null && !tasks.isEmpty() && tasks.size() > 0) ? tasks.size()+1 : 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        CheckBox checkBox;
        TextView textViewDoneTask;

        public ViewHolder(View itemView) {
            super(itemView);

            if(isNotEmpty) {
                textView = itemView.findViewById(R.id.tv);
                checkBox = itemView.findViewById(R.id.checkbox);
                textViewDoneTask = itemView.findViewById(R.id.tv_done_tasks);
            }
        }
    }

//    class FooterViewHolder extends RecyclerView.ViewHolder {
//
//        TextView textViewDoneTask;
//
//        public FooterViewHolder(View itemView) {
//            super(itemView);
//
//            if(isNotEmpty) {
//                textViewDoneTask = itemView.findViewById(R.id.tv_done_tasks);
//            }
//        }
//    }

    public class FooterViewHolder extends ViewHolder {

//        TextView textViewDoneTask;

        public FooterViewHolder(View itemView) {
            super(itemView);



//            if(isNotEmpty) {
//                textViewDoneTask = itemView.findViewById(R.id.tv_done_tasks);
//                textViewDoneTask.setText("Done " + tasks.size() + "tasks");
//                textViewDoneTask.setOnClickListener((v) -> {
//                    if (listId == 0) tasks = tasksFragment.realmController.getTasks();
//                    else tasks = tasksFragment.realmController.getTasks(listId);
//                    tasksFragment.tasksRecyclerViewAdapter.notifyDataSetChanged();
//                    textViewDoneTask.setVisibility(View.INVISIBLE);
//                });
//            }













//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Do whatever you want on clicking the item
//                }
//            });
        }
    }


    public class NormalViewHolder extends ViewHolder {
        public NormalViewHolder(View itemView) {
            super(itemView);

        }
    }

}