package com.zia.magiccard.Model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.zia.magiccard.Base.MyToast;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.MessageData;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.Util.CollectionUtil;
import com.zia.magiccard.Util.ConversationUtil;
import com.zia.magiccard.Util.PushUtil;
import com.zia.magiccard.Util.UserUtil;
import com.zia.magiccard.View.ChatActivity;
import com.zia.magiccard.View.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.zia.magiccard.Adapter.MessageRecyclerAdapter.AUDIO_LEFT;
import static com.zia.magiccard.Adapter.MessageRecyclerAdapter.TEXT_LEFT;
import static com.zia.magiccard.Adapter.MessageRecyclerAdapter.TEXT_RIGHT;

/**
 * Created by zia on 17-8-17.
 */

public class CustomMessageHandler extends AVIMMessageHandler {

    private Context context;
    private static final String TAG = "MessageHandlerTest";

    public CustomMessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onMessage(final AVIMMessage message, final AVIMConversation conversation, AVIMClient client) {
        super.onMessage(message, conversation, client);
        Log.d(TAG, "message From: "+message.getFrom());
        //创建一个messageData
        final MessageData messageData = new MessageData();
        messageData.setTime(System.currentTimeMillis());
        //查找main中的集合，判断是否新建对话
        int position = ConversationUtil.getPositionByConversationId(conversation.getConversationId());
        //创建conversationData
        final ConversationData conversationData = new ConversationData();
        conversationData.setMembers(conversation.getMembers());
        conversationData.setTime(System.currentTimeMillis());
        conversationData.setConversationId(conversation.getConversationId());
        //删除原有对话
        if(position != -1){
            MainActivity.conversationList.remove(position);
        }
        //判断消息为文字消息
        if (message instanceof AVIMTextMessage) {
            UserUtil.getUserById(message.getFrom(), new UserUtil.OnUserGet() {
                @Override
                public void getUserData(final UserData userData) {
                    conversationData.setLastContent(((AVIMTextMessage) message).getText());
                    //如果当前页面在聊天页面，则先刷新该界面
                    if(ChatActivity.adapter != null && ChatActivity.currentConversationId != null &&
                            ChatActivity.currentConversationId.equals(conversation.getConversationId())){
                        messageData.setType(TEXT_LEFT);
                        messageData.setUserId(userData.getObjectId());
                        messageData.setNickname(userData.getNickname());
                        messageData.setHeadUrl(userData.getHeadUrl());
                        messageData.setContent(((AVIMTextMessage) message).getText());
                        ChatActivity.adapter.addData(messageData);
                    }
                    //刷新main中的conversations
                    conversationData.setName(userData.getNickname());
                    if(userData.getHeadUrl() != null){
                        conversationData.setImageUrl(userData.getHeadUrl());
                    }
                    //刷新main中对话信息
                    freshConversationRecycler(conversationData);
                    //对话信息
                    Log.d(TAG, conversationData.toString());
                    //没有在聊天界面时toast提示
                    if(ChatActivity.currentConversationId == null){
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showToast(context, userData.getNickname()+" 给你发送了一条消息");
                            }
                        });
                    }
                    //保存conversations到服务器
                    PushUtil.saveConversations();
                }
                @Override
                public void onError(AVException e) {
                    e.printStackTrace();
                }
            });
        }
        //处理音频消息
        else if(message instanceof AVIMAudioMessage){
            UserUtil.getUserById(message.getFrom(), new UserUtil.OnUserGet() {
                @Override
                public void getUserData(final UserData userData) {
                    conversationData.setLastContent(((AVIMAudioMessage) message).getText());
                    //如果当前页面在聊天页面，则先刷新该界面
                    if(ChatActivity.adapter != null && ChatActivity.currentConversationId != null &&
                            ChatActivity.currentConversationId.equals(conversation.getConversationId())){
                        messageData.setType(AUDIO_LEFT);
                        messageData.setUserId(userData.getObjectId());
                        messageData.setNickname(userData.getNickname());
                        messageData.setHeadUrl(userData.getHeadUrl());
                        messageData.setContent(((AVIMAudioMessage) message).getText());
                        ChatActivity.adapter.addData(messageData);
                    }
                    //刷新main中的conversations
                    conversationData.setName(userData.getNickname());
                    if(userData.getHeadUrl() != null){
                        conversationData.setImageUrl(userData.getHeadUrl());
                    }
                    //刷新main中对话信息
                    freshConversationRecycler(conversationData);
                    //对话信息
                    Log.d(TAG, conversationData.toString());
                    //没有在聊天界面时toast提示
                    if(ChatActivity.currentConversationId == null){
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showToast(context, userData.getNickname()+" 给你发送了语音消息");
                            }
                        });
                    }
                    //保存conversations到服务器
                    PushUtil.saveConversations();
                }
                @Override
                public void onError(AVException e) {
                    e.printStackTrace();
                }
            });
        }
        else if(message instanceof AVIMImageMessage){

        }
    }

    /**
     * 刷新信息
     * @param conversationData
     */
    private void freshConversationRecycler(ConversationData conversationData){
        MainActivity.conversationList.add(conversationData);
        //刷新recyclerView
        MainActivity.conversationRecyclerAdapter.freshMessageList(MainActivity.conversationList);
        //置顶
        CollectionUtil.swap(MainActivity.conversationList,MainActivity.conversationList.size()-1,0);
    }

    @Override
    public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        super.onMessageReceipt(message, conversation, client);
    }
}
