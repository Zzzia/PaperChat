package com.zia.magiccard.Util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by zia on 17-8-18.
 */

public class FullRecyclerView extends MyRecyclerView {
    public FullRecyclerView(Context context) {
        super(context);
    }

    public FullRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FullRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }
}
