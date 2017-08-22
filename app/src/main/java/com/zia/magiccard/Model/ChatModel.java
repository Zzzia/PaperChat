package com.zia.magiccard.Model;

import android.app.Activity;
import android.content.Context;

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
import com.zia.magiccard.Util.UserUtil;
import com.zia.magiccard.View.ChatActivity;
import com.zia.magiccard.View.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static com.zia.magiccard.Adapter.MessageRecyclerAdapter.TEXT_RIGHT;

/**
 * Created by zia on 17-8-22.
 */

public class ChatModel implements ChatModelImp {

    private DateFormat dateFormat = new SimpleDateFormat("HH:mm");
    private Context context;

    public ChatModel(Context context){
        this.context = context;
    }

    @Override
    public void sendTextMessage(final String text, final ConversationData conversationData) {
        MessageUtil.getInstance().sendMessage(text, conversationData, null, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation avimConversation, AVIMException e) {
                int position = ConversationUtil.getPositionByConversationId(avimConversation.getConversationId());
                if (position != -1) {
                    MainActivity.conversationList.remove(position);
                }
                conversationData.setConversationId(avimConversation.getConversationId());
                conversationData.setTime(dateFormat.format(System.currentTimeMillis()));
                conversationData.setLastContent(text);
                //及时更新recycler信息
                MessageData messageData = new MessageData();
                messageData.setTime(dateFormat.format(System.currentTimeMillis()));
                messageData.setType(TEXT_RIGHT);
                messageData.setUserId(MainActivity.userData.getObjectId());
                messageData.setNickname(MainActivity.userData.getNickname());
                messageData.setHeadUrl(MainActivity.userData.getHeadUrl());
                messageData.setContent(text);
                if(ChatActivity.currentConversationId != null && ChatActivity.currentConversationId.equals(avimConversation.getConversationId())){
                    ChatActivity.adapter.addData(messageData);
                }
            }
        }, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if(e == null){
                    //发送成功后更新conversation列表数据
                    if(conversationData.getImageUrl() == null){
                        UserUtil.getFirstUserByMembers(conversationData.getMembers(), new UserUtil.OnUserGet() {
                            @Override
                            public void getUserData(UserData userData) {
                                conversationData.setImageUrl(userData.getHeadUrl());
                                MainActivity.conversationList.add(conversationData);
                                MainActivity.conversationRecyclerAdapter.freshMessageList(MainActivity.conversationList);
                                CollectionUtil.swap(MainActivity.conversationList, MainActivity.conversationList.size() - 1, 0);
                                PushUtil.saveConversations();
                            }

                            @Override
                            public void onError(AVException e) {

                            }
                        });
                    }else{
                        MainActivity.conversationList.add(conversationData);
                        MainActivity.conversationRecyclerAdapter.freshMessageList(MainActivity.conversationList);
                        CollectionUtil.swap(MainActivity.conversationList, MainActivity.conversationList.size() - 1, 0);
                        PushUtil.saveConversations();
                    }
                }
                else{
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
}
