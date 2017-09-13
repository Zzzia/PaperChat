package com.zia.magiccard.Util;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.MessageData;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.View.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zia on 17-8-20.
 */

public class ConversationUtil {

    private static final String TAG = "ConversationUtilTest";

    public static ConversationData getConversationFromMainByConversationId(String conversationId){
//        List<MessageData> list = new ArrayList<>();
//        for (int i = 0; i< MainActivity.conversationList.size(); i++){
//            if(MainActivity.conversationList.get(i).getConversationId().equals(conversationId)){
//                return MainActivity.conversationList.get(i);
//            }
//        }
        return null;
    }

    public interface OnNameGet{
        void onNameGet(String name);
    }

    public static String getConversationName(final List<String> members) throws AVException {
        for (int i = 0; i < members.size(); i++) {
            if(members.get(i).equals(AVUser.getCurrentUser().getObjectId()))
                members.remove(i);
        }
        if(members.size() == 1){
            return UserCacheUtil.getInstance().getUserDataById(members.get(0)).getNickname();
        }
        if(members.size() >= 2){
            String name = UserCacheUtil.getInstance().getUserDataById(members.get(0)).getNickname();
            name  = name + "、";
            name  = name + UserCacheUtil.getInstance().getUserDataById(members.get(1)).getNickname();
            name  = name + "等";
            name  = name + (members.size()+1);
            name  = name + "人的聊天";
            return name;
        }
        else return "";
    }


    /**
     * 找main集合里的position
     * 没有时返回-1
     * @param conversationId
     * @return
     */
    public static int getPositionByConversationId(String conversationId){
        for (int i = 0; i< MainActivity.conversations.size(); i++){
            if(MainActivity.conversations.get(i).getConversationId().equals(conversationId)){
                return i;
            }
        }
        return -1;
    }

    /**
     * 以userId作为参数查找main中集合的conversation
     * @param friendId
     * @return -1则没找到
     */
    public static int getPositionByFriendId(String friendId){
        for(int i =0;i<MainActivity.conversations.size();i++){
            AVIMConversation conversation = MainActivity.conversations.get(i);
            if(conversation.getMembers().size() <= 2){
                for(String id : conversation.getMembers()){
                    if(id.equals(friendId)){
                        return i;
                    }
                }
            }
        }
        return -1;
    }
}
