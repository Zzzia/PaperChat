package com.zia.magiccard.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.View;

import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.View.ChatActivity;
import com.zia.magiccard.View.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zia on 17-8-18.
 */

public class PageUtil {

    private static final String TAG = "PageUtilTest";
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

    /**
     * 利用UserData跳转到ChatActivity
     * @param context
     * @param userData
     * @param view
     */
    public static void gotoChatPage(Context context, UserData userData, View view) {
        Intent intent = new Intent(context, ChatActivity.class);
//        //在main集合中找是否建立过对话
        if(MainActivity.conversationList == null) return;
        ConversationData conversationData = new ConversationData();
        out:for (ConversationData c : MainActivity.conversationList) {
            for (String memberId : c.getMembers()) {
                if(memberId.equals(userData.getObjectId()) && c.getMembers().size() <= 2){
                    Log.d(TAG,"在main中找到对话 && c.getMembers().size() <= 2");
                    conversationData.setConversationId(c.getConversationId());
                    break out;
                }
            }
        }
        List<String> member = new ArrayList<>();
        member.add(userData.getObjectId());
        conversationData.setImageUrl(userData.getHeadUrl());
        conversationData.setMembers(member);
        conversationData.setName(userData.getNickname());
        intent.putExtra("conversationData",conversationData);
        gotoPageWithCard(context,view,intent);
    }
}
