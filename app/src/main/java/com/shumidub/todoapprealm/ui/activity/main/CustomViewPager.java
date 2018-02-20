package com.shumidub.todoapprealm.ui.activity.main;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * Created by A.shumidub on 20.02.18.
 *
 */


public class CustomViewPager extends ViewPager {

    boolean enable = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (enable){
            return super.onTouchEvent(ev);
        } else{
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return enable && super.onInterceptTouchEvent(ev);
    }

    public void setPageCanChangedScrolled(boolean enable){
        this.enable = enable;
    }


}
