package com.shumidub.todoapprealm.ui;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.bd.ItemsBD;
import com.shumidub.todoapprealm.model.RealmController;

import java.util.List;
import java.util.zip.Inflater;

import static com.shumidub.todoapprealm.model.RealmController.getRealmController;

/**
 * Created by Артем on 19.12.2017.
 */

public class ItemsRecyclerViewAdapter extends RecyclerView.Adapter<ItemsRecyclerViewAdapter.ViewHolder> {

    private List<ItemsBD> items;
    RealmController realmController = getRealmController();


    public ItemsRecyclerViewAdapter(List<ItemsBD> items){
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view, null, false);
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

        holder.textView.setText(items.get(position).getText());
        holder.checkBox.setChecked(items.get(position).isDone());
        setItemsGrayIfCheched(holder);
        holder.checkBox.setOnCheckedChangeListener(
                (cb, done) -> {
                    realmController.setDoneByItemsId(done, items.get(position).getId());
                    setItemsGrayIfCheched(holder);
                });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);



            textView = itemView.findViewById(R.id.tv);
            checkBox = itemView.findViewById(R.id.checkbox);



        }
    }


}