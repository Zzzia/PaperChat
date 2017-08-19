package com.zia.magiccard.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.zia.magiccard.View.ChatActivity;

/**
 * Created by zia on 17-8-18.
 */

public class PageUtil {
    /**
     * 共享元素打开activity
     */
    public static void gotoPageWithCard(Context context, View view, Class targetActivity){
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, view, "card");
        context.startActivity(new Intent(context,targetActivity), optionsCompat.toBundle());
    }

    public static void gotoPageWithCard(Context context, View view,Intent intent){
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, view, "card");
        context.startActivity(intent, optionsCompat.toBundle());
    }
}
