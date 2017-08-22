package com.zia.magiccard.Model;

import com.zia.magiccard.Bean.ConversationData;

/**
 * Created by zia on 17-8-22.
 */

public interface ChatModelImp {
    void sendTextMessage(String text, ConversationData conversationData);
}
