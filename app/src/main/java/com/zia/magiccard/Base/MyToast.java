package com.zia.magiccard.Base;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zia.magiccard.R;

/**
 * Created by zia on 17-8-11.
 */

public class MyToast {
    private Toast mToast;
    private MyToast(Context context, CharSequence text, int duration) {
        View v = LayoutInflater.from(context).inflate(R.layout.toast, null);
        TextView textView = (TextView) v.findViewById(R.id.toast_tv);
        textView.setText(text);
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(v);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mToast.cancel();
            }
        });
    }

    public static void showToast(Context context, CharSequence text) {
        new MyToast(context, text, Toast.LENGTH_LONG).show();
    }
    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }
    public void setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }
    }
}
