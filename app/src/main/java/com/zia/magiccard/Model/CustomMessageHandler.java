package com.zia.magiccard.Model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.zia.magiccard.Base.MyToast;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.MessageData;
import com.zia.magiccard.Util.CollectionUtil;
import com.zia.magiccard.Util.ConversationHelper;
import com.zia.magiccard.View.ChatActivity;
import com.zia.magiccard.View.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.zia.magiccard.Adapter.MessageRecyclerAdapter.TEXT_LEFT;

/**
 * Created by zia on 17-8-17.
 */

public class CustomMessageHandler extends AVIMMessageHandler {

    private Context context;
    private static final String TAG = "MessageHandlerTest";
    private DateFormat dateFormat = new SimpleDateFormat("hh:mm");

    public CustomMessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onMessage(final AVIMMessage message, final AVIMConversation conversation, AVIMClient client) {
        Log.d(TAG, message.getFrom());
        super.onMessage(message, conversation, client);
        if (message instanceof AVIMTextMessage) {
            //查找main中的集合，判断时候新建对话
            final int position = ConversationHelper.getPositionByConversationId(conversation.getConversationId());
            if(position == -1){//新建对话
                final ConversationData conversationData = new ConversationData();
                conversationData.setLastContent(((AVIMTextMessage) message).getText());
                conversationData.setTime(dateFormat.format(message.getUpdateAt()));
                conversationData.setConversationId(conversation.getConversationId());
                AVObject user = AVObject.createWithoutData("_User", message.getFrom());
                user.fetchInBackground(new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        final String creator = avObject.getString("nickname");
                        conversationData.setName(creator);
                        MessageData m = new MessageData();
                        m.setContent(((AVIMTextMessage) message).getText());
                        m.setTime(dateFormat.format(System.currentTimeMillis()));
                        m.setNickname(creator);
                        m.setType(TEXT_LEFT);
                        m.setUserId(AVUser.getCurrentUser().getObjectId());
                        List<MessageData> messages = new ArrayList<MessageData>();
                        messages.add(m);
                        conversationData.setMessageDatas(messages);
                        MainActivity.conversationList.add(conversationData);
                        //刷新recyclerView
                        MainActivity.adapter.freshMessageList(MainActivity.conversationList);
                        if(ChatActivity.adapter != null){
                            ChatActivity.adapter.freshData(conversation.getConversationId());
                        }
                        Log.d(TAG, conversationData.toString());
                        //刷新recycler
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showToast(context, creator+" 给你发送了一条消息");
                            }
                        });
                    }
                });
            }else{

                MainActivity.conversationList.get(position).setTime(dateFormat.format(message.getUpdateAt()));
                AVObject user = AVObject.createWithoutData("_User", message.getFrom());
                user.fetchInBackground(new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        final String creator = avObject.getString("nickname");
                        MessageData m = new MessageData();
                        m.setContent(((AVIMTextMessage) message).getText());
                        m.setTime(dateFormat.format(System.currentTimeMillis()));
                        m.setNickname(creator);
                        m.setType(TEXT_LEFT);
                        m.setUserId(AVUser.getCurrentUser().getObjectId());
                        MainActivity.conversationList.get(position).getMessageDatas().add(m);
                        //置顶
                        CollectionUtil.swap(MainActivity.conversationList,position,0);
                        //刷新recyclerView
                        MainActivity.adapter.freshMessageList(MainActivity.conversationList);
                        if(ChatActivity.adapter != null){
                            ChatActivity.adapter.freshData(conversation.getConversationId());
                        }
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showToast(context, creator+" 给你发送了一条消息");
                            }
                        });
                    }
                });
            }

        }
        Log.d(TAG, message.getContent());
    }

    @Override
    public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        super.onMessageReceipt(message, conversation, client);
    }
}
