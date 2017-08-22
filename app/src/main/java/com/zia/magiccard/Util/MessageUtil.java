package com.zia.magiccard.Util;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMSingleMessageQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zia on 17-8-22.
 */

public class MessageUtil {

    private static final String TAG = "MessageUtilTest";
    private static MessageUtil instance;
    private AVIMClient client;

    /**
     * 最底层封装
     * @param text 聊天信息
     * @param members 对话成员
     * @param conversationName 对话名字
     * @param avimConversationCreatedCallback 对话建立成功回调
     * @param avimConversationCallback 信息发送成功回调
     */
    private void sMessage(final String text, List<String> members, String conversationName, Map<String,Object> map,
                          final AVIMConversationCreatedCallback avimConversationCreatedCallback,
                          final AVIMConversationCallback avimConversationCallback){
        client.createConversation(members, conversationName, map, false, true, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation avimConversation, AVIMException e) {
                avimConversationCreatedCallback.done(avimConversation,e);
                if(e == null){
                    AVIMTextMessage message = new AVIMTextMessage();
                    message.setText(text);
                    avimConversation.sendMessage(message, avimConversationCallback);
                }
            }
        });
    }

    /**
     * 通过userData发送消息，没有回调
     * @param text
     * @param userData
     */
    public void sendMessage(String text, UserData userData){
        sendMessage(text,userData,null,null,null);
    }

    public void sendMessage(String text, UserData userData,Map<String,Object> map){
        sendMessage(text,userData,map,null,null);
    }


    public void sendMessage(String text, UserData userData,Map<String,Object> map,
                            final AVIMConversationCreatedCallback avimConversationCreatedCallback){
        sendMessage(text,userData,map,avimConversationCreatedCallback,null);
    }

    /**
     * 用userData发送消息
     * @param text 消息
     * @param userData userData
     * @param map 附加信息
     * @param avimConversationCreatedCallback 对话创建后的回调
     * @param avimConversationCallback 消息发送回调
     */
    public void sendMessage(String text, UserData userData,Map<String,Object> map,
                            final AVIMConversationCreatedCallback avimConversationCreatedCallback,
                            final AVIMConversationCallback avimConversationCallback){
        sMessage(text,getMembersFromUserData(userData),
                getNameFromMembers(getMembersFromUserData(userData)),
                map,avimConversationCreatedCallback,avimConversationCallback);
    }


    /**
     * 用原有conversation来发送消息,最上层，没有回调
     * @param text 消息
     * @param conversationData 聊天室
     */
    public void sendMessage(String text, ConversationData conversationData){
        sendMessage(text,conversationData,null,null);
    }

    public void sendMessage(String text, ConversationData conversationData, Map<String,Object> map){
        sendMessage(text,conversationData,map,null,null);
    }

    /**
     * 用原有conversation来发送消息,
     * 接收一个含有avimConversation的回调
     * @param text 消息
     * @param conversationData 聊天室
     * @param avimConversationCreatedCallback 含有avimConversation的回调
     */
    public void sendMessage(String text, ConversationData conversationData,Map<String,Object> map,
                            final AVIMConversationCreatedCallback avimConversationCreatedCallback){
        sendMessage(text,conversationData,map,avimConversationCreatedCallback,null);
    }

    /**
     * 用原有conversation来发送消息,底层，拥有所有回调
     * @param text 消息
     * @param conversationData 聊天室
     */
    public void sendMessage(String text, ConversationData conversationData,Map<String,Object> map,
                            final AVIMConversationCreatedCallback avimConversationCreatedCallback,
                            final AVIMConversationCallback avimConversationCallback){
        String conversationName = getNameFromMembers(conversationData.getMembers());
        sMessage(text,conversationData.getMembers(),conversationName,map,avimConversationCreatedCallback,avimConversationCallback);
    }

    private String getNameFromMembers(List<String> members){
        String conversationName = "";
        for(String m : members){
            conversationName += m;
            conversationName += "&";
        }
        conversationName.substring(conversationName.length()-1);
        return conversationName;
    }

    private List<String> getMembersFromUserData(UserData userData){
        List<String> members = new ArrayList<>();
        members.add(userData.getObjectId());
        members.add(AVUser.getCurrentUser().getObjectId());
        return members;
    }

    public void loginOut(){
        client.close(null);
    }

    private MessageUtil(){
        //开启聊天功能
        client = AVIMClient.getInstance(AVUser.getCurrentUser());
        client.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if(e != null) e.printStackTrace();
            }
        });
    }

    public static MessageUtil getInstance(){
        if(instance == null){
            synchronized (MessageUtil.class){
                if(instance == null){
                    instance = new MessageUtil();
                }
            }
        }
        return instance;
    }

}
