package com.shumidub.todoapprealm.ui.TaskUI.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.model.TaskModel;
import com.shumidub.todoapprealm.realmcontrollers.TasksRealmController;

import java.util.List;

import static com.shumidub.todoapprealm.App.TAG;


/**
 * Created by Артем on 19.12.2017.
 */

public class DoneTasksRecyclerViewAdapter extends RecyclerView.Adapter<DoneTasksRecyclerViewAdapter.ViewHolder> {

    private List<TaskModel> doneTasks;
    private boolean isNotEmpty;


    public DoneTasksRecyclerViewAdapter(List<TaskModel> items){
        this.doneTasks = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (doneTasks != null && !doneTasks.isEmpty() && doneTasks.size() > 0) {
            isNotEmpty = true;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view, null, false);
        }else{
            isNotEmpty = false;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_empty_state, parent, false);
        }

        return new ViewHolder(view);
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
        if (isNotEmpty) {

            TaskModel item = doneTasks.get(position);

            long taskId = item.getId();
            String text = item.getText();


            holder.textView.setText(text);
            holder.textView.setTag(taskId);


            holder.checkBox.setChecked(item.isDone());
            setTasksTextColor(holder, item.isDone());

            holder.checkBox.setOnClickListener(
                    (cb) -> {

                        TasksRealmController.setTaskDone(item, holder.checkBox.isChecked());

                        Log.d(TAG+ "1", "SET_DONE: " +
                                            "\n " +
                                        "\nitem text = " + item.getText() +
                                        "\nitems.get(position).getText() = " + doneTasks.get(position).getText() +
                                             "\n " +
                                        "\nitem = " + item.hashCode() +
                                        "\nitems.get(position) =" + doneTasks.get(position).hashCode() +
                                            "\n " +
                                        "\nitem.taskID = " + item.getId() +
                                        "\nitems.get(position).taskID = " + doneTasks.get(position).getId() +
                                        "\ntaskID = " + taskId +
                                            "\n " +
                                        "\nitem.isDone =" + item.isDone() +
                                        "\nitemsget(position).isDone =" + doneTasks.get(position).isDone());

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
    }

    @Override
    public int getItemCount() {
        return (doneTasks != null && !doneTasks.isEmpty() && doneTasks.size() > 0) ? doneTasks.size() : 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);

            if(isNotEmpty) {
                textView = itemView.findViewById(R.id.tv);
                checkBox = itemView.findViewById(R.id.checkbox);
            }
        }
    }
}