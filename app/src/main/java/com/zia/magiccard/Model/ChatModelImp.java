package com.zia.magiccard.Model;

import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.UserData;

/**
 * Created by zia on 17-8-22.
 */

public interface ChatModelImp {
    void sendTextMessage(String text, ConversationData conversationData,AVIMConversationCallback avimConversationCallback);
    void sendTextMessage(String text, UserData userData, AVIMConversationCallback avimConversationCallback);
    void sendAudioMessage(byte[] bytes, ConversationData conversationData,AVIMConversationCallback avimConversationCallback);
    void sendAudioMessage(byte[] bytes, UserData userData,AVIMConversationCallback avimConversationCallback);
    void sendPictureMessage(String path, ConversationData conversationData,AVIMConversationCallback avimConversationCallback);
    void sendPictureMessage(String path, UserData userData,AVIMConversationCallback avimConversationCallback);
}
