package com.zia.magiccard.Model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMSingleMessageQueryCallback;
import com.zia.magiccard.Base.MyToast;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.MessageData;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.Util.CollectionUtil;
import com.zia.magiccard.Util.ConversationUtil;
import com.zia.magiccard.Util.MessageUtil;
import com.zia.magiccard.Util.PushUtil;
import com.zia.magiccard.Util.UserCacheUtil;
import com.zia.magiccard.Util.UserUtil;
import com.zia.magiccard.View.ChatActivity;
import com.zia.magiccard.View.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.zia.magiccard.Adapter.MessageRecyclerAdapter.AUDIO_RIGHT;
import static com.zia.magiccard.Adapter.MessageRecyclerAdapter.TEXT_RIGHT;

/**
 * Created by zia on 17-8-22.
 */

public class ChatModel implements ChatModelImp {

    private Context context;
    private static final String TAG = "ChatModelTest";

    public ChatModel(Context context){
        this.context = context;
    }

    @Override
    public void sendTextMessage(final String text, final ConversationData conversationData, final AVIMConversationCallback avimConversationCallback) {
        MessageUtil.getInstance().sendMessage(text, conversationData, null, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation avimConversation, AVIMException e) {
                if(e!= null){
                    e.printStackTrace();
                    return;
                }
                Log.d(TAG,"avimConversationId:"+avimConversation.getConversationId());
//                int position = ConversationUtil.getPositionByConversationId(avimConversation.getConversationId());
//                if (position != -1) {
//                    MainActivity.conversations.remove(position);
//                }
//                conversationData.setConversationId(avimConversation.getConversationId());
//                conversationData.setTime(System.currentTimeMillis());
//                conversationData.setLastContent(text);
                //及时更新recycler信息
//                MessageData messageData = new MessageData();
//                messageData.setTime(System.currentTimeMillis());
//                messageData.setType(TEXT_RIGHT);
//                messageData.setUserId(MainActivity.userData.getObjectId());
//                messageData.setNickname(MainActivity.userData.getNickname());
//                messageData.setHeadUrl(MainActivity.userData.getHeadUrl());
//                messageData.setContent(text);
//                if(ChatActivity.currentConversationId != null){
//                    ChatActivity.adapter.addData(messageData);
//                }
                //更新conversation列表数据
//                UserCacheUtil.getInstance().getUserDataAsyncByMember(conversationData.getMembers(), new UserCacheUtil.OnUserDataGet() {
//                    @Override
//                    public void onUserFind(UserData userData) {
//                        conversationData.setName(userData.getNickname());
//                        conversationData.setImageUrl(userData.getHeadUrl());
//                    }
//                });
                if(!MainActivity.conversations.contains(avimConversation)){
                    MainActivity.conversations.add(avimConversation);
                    CollectionUtil.swap(MainActivity.conversations,MainActivity.conversations.size()-1,0);
                    MainActivity.conversationRecyclerAdapter.freshMessageList();
                }
            }
        }, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                avimConversationCallback.done(e);
                if(e == null){
                    //上传对话数据
                    PushUtil.saveConversations();
                } else{
                    e.printStackTrace();
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyToast.showToast(context,"请检查网络");
                        }
                    });
                }
            }
        });
    }

    @Override
    public void sendTextMessage(final String text, final UserData userData, final AVIMConversationCallback avimConversationCallback){
        ConversationData conversationData = new ConversationData();
        conversationData.setMembers(getMembersByUser(userData));
        sendTextMessage(text,conversationData,avimConversationCallback);
    }

    @Override
    public void sendAudioMessage(byte[] bytes, final ConversationData conversationData, final AVIMConversationCallback avimConversationCallback) {
        MessageUtil.getInstance().sendAudioMessage(bytes, conversationData, null, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation avimConversation, AVIMException e) {
//                conversationData.setConversationId(avimConversation.getConversationId());
//                conversationData.setTime(System.currentTimeMillis());
//                conversationData.setLastContent("语音消息");
                //更新conversation列表数据
//                UserCacheUtil.getInstance().getUserDataAsyncByMember(conversationData.getMembers(), new UserCacheUtil.OnUserDataGet() {
//                    @Override
//                    public void onUserFind(UserData userData) {
//                        conversationData.setName(userData.getNickname());
//                        conversationData.setImageUrl(userData.getHeadUrl());
//                    }
//                });
                if(!MainActivity.conversations.contains(avimConversation)){
                    MainActivity.conversations.add(avimConversation);
                    CollectionUtil.swap(MainActivity.conversations,MainActivity.conversations.size()-1,0);
                    MainActivity.conversationRecyclerAdapter.freshMessageList();
                }
            }
        }, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                avimConversationCallback.done(e);
                //上传完成后刷新recycler
                ChatActivity.adapter.freshData();
            }
        });
    }

    @Override
    public void sendAudioMessage(byte[] bytes, UserData userData, AVIMConversationCallback avimConversationCallback) {
        ConversationData conversationData = new ConversationData();
        conversationData.setMembers(getMembersByUser(userData));
        sendAudioMessage(bytes,conversationData,avimConversationCallback);
    }

    @Override
    public void sendPictureMessage(String path, final ConversationData conversationData, final AVIMConversationCallback avimConversationCallback) {
        MessageUtil.getInstance().sendPhotoMessage(path, conversationData, null, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation avimConversation, AVIMException e) {
//                int position = ConversationUtil.getPositionByConversationId(avimConversation.getConversationId());
//                conversationData.setConversationId(avimConversation.getConversationId());
//                conversationData.setTime(System.currentTimeMillis());
//                conversationData.setLastContent("[图片]");
                //更新conversation列表数据
//                UserCacheUtil.getInstance().getUserDataAsyncByMember(conversationData.getMembers(), new UserCacheUtil.OnUserDataGet() {
//                    @Override
//                    public void onUserFind(UserData userData) {
//                        conversationData.setName(userData.getNickname());
//                        conversationData.setImageUrl(userData.getHeadUrl());
//                    }
//                });
                if(!MainActivity.conversations.contains(avimConversation)){
                    MainActivity.conversations.add(avimConversation);
                    CollectionUtil.swap(MainActivity.conversations,MainActivity.conversations.size()-1,0);
                    MainActivity.conversationRecyclerAdapter.freshMessageList();
                }
            }
        }, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                avimConversationCallback.done(e);
                //上传完成后刷新recycler
                ChatActivity.adapter.freshData();
            }
        });
    }

    @Override
    public void sendPictureMessage(String path, UserData userData, AVIMConversationCallback avimConversationCallback) {
        ConversationData conversationData = new ConversationData();
        conversationData.setMembers(getMembersByUser(userData));
        sendPictureMessage(path,conversationData,avimConversationCallback);
    }

    @Override
    public void sendVideoMessage(String videoPath, final ConversationData conversationData, final AVIMConversationCallback avimConversationCallback) {
        MessageUtil.getInstance().sendVideoMessage(videoPath, conversationData, null, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation avimConversation, AVIMException e) {
//                int position = ConversationUtil.getPositionByConversationId(avimConversation.getConversationId());
//                conversationData.setConversationId(avimConversation.getConversationId());
//                conversationData.setTime(System.currentTimeMillis());
//                conversationData.setLastContent("[视频]");
//                //更新conversation列表数据
//                UserCacheUtil.getInstance().getUserDataAsyncByMember(conversationData.getMembers(), new UserCacheUtil.OnUserDataGet() {
//                    @Override
//                    public void onUserFind(UserData userData) {
//                        conversationData.setName(userData.getNickname());
//                        conversationData.setImageUrl(userData.getHeadUrl());
//                    }
//                });
                if(!MainActivity.conversations.contains(avimConversation)){
                    MainActivity.conversations.add(avimConversation);
                    CollectionUtil.swap(MainActivity.conversations,MainActivity.conversations.size()-1,0);
                    MainActivity.conversationRecyclerAdapter.freshMessageList();
                }
            }
        }, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                avimConversationCallback.done(e);
                //上传完成后刷新recycler
                ChatActivity.adapter.freshData();
            }
        });
    }

    @Override
    public void sendVideoMessage(String videoPath, UserData userData, AVIMConversationCallback avimConversationCallback) {
        ConversationData conversationData = new ConversationData();
        conversationData.setMembers(getMembersByUser(userData));
        sendVideoMessage(videoPath,conversationData,avimConversationCallback);
    }


    private List<String> getMembersByUser(UserData userData){
        List<String> members = new ArrayList<>();
        members.add(AVUser.getCurrentUser().getObjectId());
        members.add(userData.getObjectId());
        return members;
    }
}
