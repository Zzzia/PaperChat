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
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.Util.CollectionUtil;
import com.zia.magiccard.Util.ConversationHelper;
import com.zia.magiccard.Util.PushUtil;
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
    private DateFormat dateFormat = new SimpleDateFormat("HH:mm");

    public CustomMessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onMessage(final AVIMMessage message, final AVIMConversation conversation, AVIMClient client) {
        Log.d(TAG, "message From: "+message.getFrom());
        super.onMessage(message, conversation, client);
        UserModel userModel = new UserModel();
        if (message instanceof AVIMTextMessage) {
            //查找main中的集合，判断时候新建对话
            final int position = ConversationHelper.getPositionByConversationId(conversation.getConversationId());
            //新建对话
            if(position == -1){
                final ConversationData conversationData = new ConversationData();
                conversationData.setMembers(conversation.getMembers());
                conversationData.setLastContent(((AVIMTextMessage) message).getText());
                conversationData.setTime(dateFormat.format(System.currentTimeMillis()));
                conversationData.setConversationId(conversation.getConversationId());

                userModel.getUserById(message.getFrom(), new UserModel.OnUserGet() {
                    @Override
                    public void getUserData(final UserData userData) {
                        conversationData.setName(userData.getNickname());
                        MessageData m = new MessageData();
                        m.setContent(((AVIMTextMessage) message).getText());
                        m.setTime(dateFormat.format(System.currentTimeMillis()));
                        m.setNickname(userData.getNickname());
                        m.setHeadUrl(userData.getHeadUrl());
                        m.setType(TEXT_LEFT);
                        m.setUserId(AVUser.getCurrentUser().getObjectId());
                        List<MessageData> messages = new ArrayList<MessageData>();
                        messages.add(m);
                        conversationData.setImageUrl(userData.getHeadUrl());
                        conversationData.setMessageDatas(messages);
                        MainActivity.conversationList.add(conversationData);
                        //刷新recyclerView
                        MainActivity.conversationRecyclerAdapter.freshMessageList(MainActivity.conversationList);
                        //置顶
                        CollectionUtil.swap(MainActivity.conversationList,MainActivity.conversationList.size()-1,0);
                        //更新聊天界面recycler
                        if(ChatActivity.adapter != null){
                            ChatActivity.adapter.freshData(conversationData.getConversationId());
                        }
                        Log.d(TAG, conversationData.toString());
                        //刷新recycler
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showToast(context, userData.getNickname()+" 给你发送了一条消息");
                            }
                        });
                        //保存conversations到服务器
                        PushUtil.saveConversations();
                    }

                    @Override
                    public void onError(AVException e) {
                        e.printStackTrace();
                    }
                });
                //更新原有对话
            }else{
                MainActivity.conversationList.get(position).setTime(dateFormat.format(System.currentTimeMillis()));
                userModel.getUserById(message.getFrom(), new UserModel.OnUserGet() {
                    @Override
                    public void getUserData(final UserData userData) {
                        MessageData m = new MessageData();
                        m.setContent(((AVIMTextMessage) message).getText());
                        m.setTime(dateFormat.format(System.currentTimeMillis()));
                        m.setNickname(userData.getNickname());
                        m.setHeadUrl(userData.getHeadUrl());
                        m.setType(TEXT_LEFT);
                        m.setUserId(AVUser.getCurrentUser().getObjectId());
                        MainActivity.conversationList.get(position).setTime(dateFormat.format(System.currentTimeMillis()));
                        MainActivity.conversationList.get(position).setLastContent(((AVIMTextMessage) message).getText());
                        MainActivity.conversationList.get(position).getMessageDatas().add(m);
                        //刷新recyclerView
                        MainActivity.conversationRecyclerAdapter.freshMessageList(MainActivity.conversationList);
                        //置顶
                        CollectionUtil.swap(MainActivity.conversationList,position,0);
                        //更新聊天界面
                        if(ChatActivity.adapter != null){
                            ChatActivity.adapter.freshData(conversation.getConversationId());
                        }

                        //保存conversations到服务器
                        PushUtil.saveConversations();
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showToast(context, userData.getNickname()+" 给你发送了一条消息");
                            }
                        });
                    }

                    @Override
                    public void onError(AVException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    @Override
    public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        super.onMessageReceipt(message, conversation, client);
    }
}
