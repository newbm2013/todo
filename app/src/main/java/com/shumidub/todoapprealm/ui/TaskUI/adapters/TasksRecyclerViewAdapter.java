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

public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.ViewHolder> {

    private List<TaskModel> items;
    private boolean isNotEmpty;


    public TasksRecyclerViewAdapter(List<TaskModel> items){
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (items != null && !items.isEmpty() && items.size() > 0) {
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
//            Spannable text = new SpannableString(holder.textView.getText().toString());
//            text.setSpan(new StrikethroughSpan(), 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            holder.textView.setText(text);
        }else if(!isDone){
            holder.textView.setTextColor(Color.BLACK);
//            String text = holder.textView.getText().toString();
//            holder.textView.setText(text);
        }
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isNotEmpty) {

            TaskModel item = items.get(position);

            long taskId = item.getId();
            String text = item.getText();


//            Log.d(TAG + "1", "onBindViewHolder: taskID " +items.get(position).getText() + " =" + taskId );


            holder.textView.setText(text);
            holder.textView.setTag(taskId);


            holder.checkBox.setChecked(item.isDone());
            setTasksTextColor(holder, item.isDone());

            holder.checkBox.setOnCheckedChangeListener(
                    (cb, done) -> {
                        TasksRealmController.setTaskDone(item, done);

//                        holder.checkBox.isChecked()   done

//                        isDone = items.get(position).isDone(); //or better done = isDone ?
//
                        Log.d(TAG+ "1", "SET_DONE: " +
                                            "\n " +
                                        "\nitem text = " + item.getText() +
                                        "\nitems.get(position).getText() = " + items.get(position).getText() +
                                             "\n " +
                                        "\nitem = " + item.hashCode() +
                                        "\nitems.get(position) =" + items.get(position).hashCode() +
                                            "\n " +
                                        "\nitem.taskID = " + item.getId() +
                                        "\nitems.get(position).taskID = " + items.get(position).getId() +
                                        "\ntaskID = " + taskId +
                                            "\n " +
                                        "\nitem.isDone =" + item.isDone() +
                                        "\nitemsget(position).isDone =" + items.get(position).isDone() +
                                        "\ndone = " + done );

                        setTasksTextColor(holder, item.isDone());
                    });



            holder.textView.setOnClickListener( (a)->
                    {Log.d(TAG+ "1", "CLICK: " +
                            "\n " +
                            "\nitem text = " + item.getText() +
                            "\nitems.get(position).getText() = " + items.get(position).getText() +
                            "\n " +
                            "\nitem = " + item.hashCode() +
                            "\nitems.get(position) =" + items.get(position).hashCode() +
                            "\n " +
                            "\nitem.taskID = " + item.getId() +
                            "\nitems.get(position).taskID = " + items.get(position).getId() +
                            "\ntaskID = " + taskId +
                            "\n " +
                            "\nitem.isDone =" + item.isDone() +
                            "\nitemsget(position).isDone =" + items.get(position).isDone() );
                     });


            holder.textView.setOnLongClickListener((a)-> {
                Log.d(TAG+ "1", "onLongClick: taskID " +items.get(position).getText()
                    + " =" + taskId  +"/" + items.get(position).getId()
                   );
                return true;});
        }
    }

    @Override
    public int getItemCount() {
        return (items != null && !items.isEmpty() && items.size() > 0) ? items.size() : 1;
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