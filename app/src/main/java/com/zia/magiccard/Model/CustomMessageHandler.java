package com.zia.magiccard.Model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.zia.magiccard.Base.ToastUnit;

/**
 * Created by zia on 17-8-17.
 */

public class CustomMessageHandler extends AVIMMessageHandler {

    private Context context;
    private static final String TAG = "MessageHandlerTest";

    public CustomMessageHandler(Context context){
        this.context = context;
    }

    @Override
    public void onMessage(final AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        Log.d(TAG,message.getFrom());
        super.onMessage(message, conversation, client);
        if(message instanceof AVIMTextMessage){
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUnit.showToast(context,((AVIMTextMessage) message).getText());
                }
            });
        }
        Log.d(TAG,message.getContent());
    }

    @Override
    public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        super.onMessageReceipt(message, conversation, client);
    }
}