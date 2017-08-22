package com.zia.magiccard.Presenter;

import android.util.Log;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.MessageData;
import com.zia.magiccard.Util.CollectionUtil;
import com.zia.magiccard.Util.ConversationUtil;
import com.zia.magiccard.Util.PushUtil;
import com.zia.magiccard.View.ChatActivity;
import com.zia.magiccard.View.ChatImp;
import com.zia.magiccard.View.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.zia.magiccard.Adapter.MessageRecyclerAdapter.TEXT_RIGHT;

/**
 * Created by zia on 17-8-17.
 */

public class ChatPresenter implements ChatPresenterImp {

    private ChatImp imp;
    private static final String TAG = "ChatPresenterTest";
    private DateFormat dateFormat = new SimpleDateFormat("HH:mm");

    public ChatPresenter(ChatImp imp) {
        this.imp = imp;
    }


    /**
     * 发送信息
     */
    @Override
    public void sendMessage() {
        //获取信息
        if (imp.getEditText().getText() == null) return;
        final String text = imp.getEditText().getText().toString();
        if (text.isEmpty()) return;
        final ConversationData conversationData = imp.getConversationData();
        if (conversationData != null) {
            imp.getAVIMClient().open(new AVIMClientCallback() {
                @Override
                public void done(final AVIMClient avimClient, AVIMException e) {
                    if (e != null) {
                        e.printStackTrace();
                        return;
                    }
                    //组装messageData
                    final MessageData m = new MessageData();
                    m.setContent(text);
                    m.setTime(dateFormat.format(System.currentTimeMillis()));
                    m.setNickname(AVUser.getCurrentUser().getString("nickname"));
                    m.setType(TEXT_RIGHT);
                    m.setUserId(AVUser.getCurrentUser().getObjectId());
                    if (MainActivity.userData != null && MainActivity.userData.getHeadUrl() != null) {
                        m.setHeadUrl(MainActivity.userData.getHeadUrl());
                    }

                    /*
                     conversationData.setImageUrl(userData.getHeadUrl());
                     conversationData.setMembers(members);
                     conversationData.setName(userData.getNickname());
                     */
                    //新建对话，需要参数：conversationData.getMembers(), conversationData.getName()
                    avimClient.createConversation(conversationData.getMembers(), conversationData.getName(), null, false, true,
                            new AVIMConversationCreatedCallback() {
                                @Override
                                public void done(AVIMConversation avimConversation, AVIMException e) {
                                    if (e == null) {
                                        int position = ConversationUtil.getPositionByConversationId(avimConversation.getConversationId());
                                        List<MessageData> messages = null;
                                        if (position == -1) messages = new ArrayList<MessageData>();
                                        else {
                                            messages = MainActivity.conversationList.get(position).getMessageDatas();
                                            MainActivity.conversationList.remove(position);
                                        }
                                        messages.add(m);
                                        //组装信息
                                        conversationData.setConversationId(avimConversation.getConversationId());
                                        conversationData.setLastContent(text);
                                        conversationData.setTime(dateFormat.format(System.currentTimeMillis()));
                                        conversationData.setMessageDatas(messages);
                                        Log.d(TAG, "新建的conversationData:" + conversationData.toString());
                                        //添加到main中
                                        MainActivity.conversationList.add(conversationData);
                                        MainActivity.conversationRecyclerAdapter.freshMessageList(MainActivity.conversationList);
                                        if (ChatActivity.adapter != null) {
                                            ChatActivity.adapter.freshData(conversationData.getConversationId());
                                        }
                                        CollectionUtil.swap(MainActivity.conversationList, MainActivity.conversationList.size() - 1, 0);
                                        //保存数据在服务器
                                        PushUtil.saveConversations();
                                        //发送消息
                                        AVIMTextMessage message = new AVIMTextMessage();
                                        message.setText(text);
                                        avimConversation.sendMessage(message, new AVIMConversationCallback() {
                                            @Override
                                            public void done(AVIMException e) {
                                                if (e == null) {
                                                    imp.getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            imp.toast("发送成功：" + text);
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                }
            });
        }

    }

    /**
     * 初始化recycler数据
     */
    @Override
    public void initData() {
        String conversationId = imp.getConversationData().getConversationId();
        for (ConversationData c : MainActivity.conversationList) {
            if (c.getConversationId().equals(conversationId)) {
                imp.getMessageAdapter().freshData(c.getConversationId());
            }
        }
    }
}
