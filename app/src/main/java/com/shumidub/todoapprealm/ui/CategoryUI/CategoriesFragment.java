package com.shumidub.todoapprealm.ui.CategoryUI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.model.TasksRealmController;

/**
 * Created by Артем on 19.12.2017.
 */

public class CategoriesFragment extends Fragment {

    Button enter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blank, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        enter = (Button) view.findViewById(R.id.btnEnter);


//        enter.setOnClickListener( (b) ->  {
//            realmController.insertItems(editText.getText().toString());
//            editText.setText("");
//        }  );

    }
}
