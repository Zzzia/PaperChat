package com.zia.magiccard.Presenter;

import android.view.View;

import com.zia.magiccard.Bean.ConversationData;

/**
 * Created by zia on 17-8-17.
 */

public interface MessagePresenterImp {
    void gotoChatPage(ConversationData conversationData, View view);
    void deleteConversation(int position);
}
