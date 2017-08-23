package com.zia.magiccard.Model;

import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.UserData;

/**
 * Created by zia on 17-8-22.
 */

public interface ChatModelImp {
    void sendTextMessage(String text, ConversationData conversationData,AVIMConversationCallback avimConversationCallback);
    void sendTextMessage(final String text,
                                final UserData userData,
                                final AVIMConversationCallback avimConversationCallback);
}
