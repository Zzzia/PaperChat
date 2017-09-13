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
import com.avos.avoscloud.im.v2.messages.AVIMVideoMessage;
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
import static com.zia.magiccard.Adapter.MessageRecyclerAdapter.PICTURE_LEFT;
import static com.zia.magiccard.Adapter.MessageRecyclerAdapter.TEXT_LEFT;
import static com.zia.magiccard.Adapter.MessageRecyclerAdapter.TEXT_RIGHT;
import static com.zia.magiccard.Adapter.MessageRecyclerAdapter.VIDEO_LEFT;

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
        Log.e("handler get unread:",conversation.getUnreadMessagesCount()+"");
        Log.d(TAG, "message From: "+message.getFrom());
        //创建一个messageData
        final MessageData messageData = new MessageData();
        messageData.setTime(System.currentTimeMillis());
        //查找main中的集合，判断是否新建对话
        int position = ConversationUtil.getPositionByConversationId(conversation.getConversationId());
        if(ChatActivity.currentConversationId != null && conversation.getConversationId().equals(ChatActivity.currentConversationId)){
            conversation.read();
        }
        Log.e("从服务器上获取对话人数:",conversation.getMembers().size()+"");
        //删除原有对话
        if(position != -1){
            MainActivity.conversations.remove(position);
        }
        //判断消息为文字消息
        if (message instanceof AVIMTextMessage) {
            UserUtil.getUserById(message.getFrom(), new UserUtil.OnUserGet() {
                @Override
                public void getUserData(final UserData userData) {
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
                    //刷新main中对话信息
                    freshConversationRecycler(conversation);
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
                    //如果当前页面在聊天页面，则先刷新该界面
                    if(ChatActivity.adapter != null && ChatActivity.currentConversationId != null &&
                            ChatActivity.currentConversationId.equals(conversation.getConversationId())){
                        messageData.setType(AUDIO_LEFT);
                        messageData.setAudioUrl(((AVIMAudioMessage) message).getFileUrl());
                        messageData.setUserId(userData.getObjectId());
                        messageData.setNickname(userData.getNickname());
                        messageData.setHeadUrl(userData.getHeadUrl());
                        messageData.setContent(((AVIMAudioMessage) message).getText());
                        ChatActivity.adapter.addData(messageData);
                    }
                    //刷新main中对话信息
                    freshConversationRecycler(conversation);
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
            UserUtil.getUserById(message.getFrom(), new UserUtil.OnUserGet() {
                @Override
                public void getUserData(final UserData userData) {
                    //如果当前页面在聊天页面，则先刷新该界面
                    if(ChatActivity.adapter != null && ChatActivity.currentConversationId != null &&
                            ChatActivity.currentConversationId.equals(conversation.getConversationId())){
                        messageData.setType(PICTURE_LEFT);
                        messageData.setPhotoUrl(((AVIMImageMessage) message).getFileUrl());
                        messageData.setUserId(userData.getObjectId());
                        messageData.setNickname(userData.getNickname());
                        messageData.setHeadUrl(userData.getHeadUrl());
                        messageData.setContent(((AVIMImageMessage) message).getText());
                        ChatActivity.adapter.addData(messageData);
                    }
                    //刷新main中对话信息
                    freshConversationRecycler(conversation);
                    //没有在聊天界面时toast提示
                    if(ChatActivity.currentConversationId == null){
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showToast(context, userData.getNickname()+" 给你发送了图片");
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
        else if(message instanceof AVIMVideoMessage){
            UserUtil.getUserById(message.getFrom(), new UserUtil.OnUserGet() {
                @Override
                public void getUserData(final UserData userData) {
                    //如果当前页面在聊天页面，则先刷新该界面
                    if(ChatActivity.adapter != null && ChatActivity.currentConversationId != null &&
                            ChatActivity.currentConversationId.equals(conversation.getConversationId())){
                        messageData.setType(VIDEO_LEFT);
                        messageData.setPhotoUrl((String)((AVIMVideoMessage) message).getAttrs().get("photoUrl"));
                        messageData.setVideoUrl(((AVIMVideoMessage) message).getFileUrl());
                        messageData.setUserId(userData.getObjectId());
                        messageData.setNickname(userData.getNickname());
                        messageData.setHeadUrl(userData.getHeadUrl());
                        ChatActivity.adapter.addData(messageData);
                    }
                    //刷新main中对话
                    freshConversationRecycler(conversation);
                    //没有在聊天界面时toast提示
                    if(ChatActivity.currentConversationId == null){
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showToast(context, userData.getNickname()+" 给你发送了视频");
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
    }

    /**
     * 刷新信息
     * @param conversation
     */
    private void freshConversationRecycler(AVIMConversation conversation){
        MainActivity.conversations.add(conversation);
        //置顶
        CollectionUtil.swap(MainActivity.conversations,MainActivity.conversations.size()-1,0);
        //刷新recyclerView
        MainActivity.conversationRecyclerAdapter.freshMessageList();
    }

    @Override
    public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        super.onMessageReceipt(message, conversation, client);
    }
}
