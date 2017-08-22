package com.zia.magiccard.Presenter;

import android.util.Log;

import com.avos.avoscloud.AVException;
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
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.Model.ChatModel;
import com.zia.magiccard.Model.ChatModelImp;
import com.zia.magiccard.Util.CollectionUtil;
import com.zia.magiccard.Util.ConversationUtil;
import com.zia.magiccard.Util.MessageUtil;
import com.zia.magiccard.Util.PushUtil;
import com.zia.magiccard.Util.UserUtil;
import com.zia.magiccard.View.ChatActivity;
import com.zia.magiccard.View.ChatImp;
import com.zia.magiccard.View.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.zia.magiccard.Adapter.MessageRecyclerAdapter.TEXT_RIGHT;

/**
 * Created by zia on 17-8-17.
 */

public class ChatPresenter implements ChatPresenterImp {

    private ChatImp imp;
    private ChatModelImp modelImp;
    private static final String TAG = "ChatPresenterTest";


    public ChatPresenter(ChatImp imp) {
        this.imp = imp;
        modelImp = new ChatModel(imp.getActivity());
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
        //发送信息
        if (conversationData != null) {
            modelImp.sendTextMessage(text,conversationData);
        }
        imp.getEditText().setText("");
    }

    /**
     * 初始化recycler数据
     */
    @Override
    public void initData() {
        if(imp.getConversationData() != null){
            String conversationId = imp.getConversationData().getConversationId();
            ChatActivity.currentConversationId = conversationId;
            imp.getMessageAdapter().freshData();
        }
        if(imp.getUserData() != null){
            MessageUtil.getInstance().createConversation(imp.getUserData(), new AVIMConversationCreatedCallback() {
                @Override
                public void done(AVIMConversation avimConversation, AVIMException e) {
                    ChatActivity.currentConversationId = avimConversation.getConversationId();
                    imp.getMessageAdapter().freshData();
                }
            });
        }
    }
}
