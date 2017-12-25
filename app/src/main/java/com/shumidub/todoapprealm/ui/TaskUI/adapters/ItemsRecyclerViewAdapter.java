package com.shumidub.todoapprealm.ui.TaskUI.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.bd.ItemsBD;
import com.shumidub.todoapprealm.model.TasksRealmController;

import java.util.List;



/**
 * Created by Артем on 19.12.2017.
 */

public class ItemsRecyclerViewAdapter extends RecyclerView.Adapter<ItemsRecyclerViewAdapter.ViewHolder> {

    private List<ItemsBD> items;
    private boolean isNotEmpty;

    public ItemsRecyclerViewAdapter(List<ItemsBD> items){
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

    private void setItemsGrayIfCheched(ViewHolder holder){
        if (holder.checkBox.isChecked()){
            holder.textView.setTextColor(Color.GRAY);
//            Spannable text = new SpannableString(holder.textView.getText().toString());
//            text.setSpan(new StrikethroughSpan(), 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            holder.textView.setText(text);
        }
    }

    private void setItemsNormalIfNotCheched(ViewHolder holder){
        if (!holder.checkBox.isChecked()){
            holder.textView.setTextColor(Color.BLACK);
            String text = holder.textView.getText().toString();
            holder.textView.setText(text);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isNotEmpty) {
            holder.textView.setText(items.get(position).getText());
            holder.checkBox.setChecked(items.get(position).isDone());
            setItemsGrayIfCheched(holder);
            holder.checkBox.setOnCheckedChangeListener(
                    (cb, done) -> {
                        TasksRealmController.setDoneByItemsId(done, items.get(position).getId());
                        setItemsGrayIfCheched(holder);
                    });
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