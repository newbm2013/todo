package com.shumidub.todoapprealm.ui.CategoryUI.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.shumidub.todoapprealm.R;

/**
 * Created by Артем on 09.01.2018.
 */

public class CustomOnCancelListener implements DialogInterface.OnCancelListener{

    Context context;
    private static long back_pressed;

    public CustomOnCancelListener(Context context){
        this.context = context;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        Log.d("DTAG", "onCancel: ");
        if (back_pressed + 2000 > System.currentTimeMillis())
            dialog.cancel();
        else
            Toast.makeText(context, R.string.back_button_pressed_notify,
                    Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

}
