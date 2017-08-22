package com.zia.magiccard.Util;

import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.MessageData;
import com.zia.magiccard.View.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zia on 17-8-20.
 */

public class ConversationUtil {
    public static ConversationData getConversationFromMainByConversationId(String conversationId){
//        List<MessageData> list = new ArrayList<>();
        for (int i = 0; i< MainActivity.conversationList.size(); i++){
            if(MainActivity.conversationList.get(i).getConversationId().equals(conversationId)){
                return MainActivity.conversationList.get(i);
            }
        }
        return null;
    }

    /**
     * 找main集合里的position
     * 没有时返回-1
     * @param conversationId
     * @return
     */
    public static int getPositionByConversationId(String conversationId){
        for (int i = 0; i< MainActivity.conversationList.size(); i++){
            if(MainActivity.conversationList.get(i).getConversationId().equals(conversationId)){
                return i;
            }
        }
        return -1;
    }
}
