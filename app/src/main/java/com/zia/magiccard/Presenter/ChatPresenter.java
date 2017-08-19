package com.zia.magiccard.Presenter;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.View.ChatImp;

import java.util.Arrays;

/**
 * Created by zia on 17-8-17.
 */

public class ChatPresenter implements ChatPresenterImp {

    private ChatImp imp;

    public ChatPresenter(ChatImp imp) {
        this.imp = imp;
    }


    @Override
    public void sendMessage() {
        //获取信息
        if(imp.getEditText().getText() == null) return;
        final String text = imp.getEditText().getText().toString();
        if (text.isEmpty()) return;
        final UserData userData = imp.getUserData();
        if(userData == null) return;

        //发送信息
        imp.getAVIMClient().open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e == null) {
                    avimClient.createConversation(Arrays.asList(userData.getObjectId()), userData.getNickname(), null,
                            new AVIMConversationCreatedCallback() {
                                @Override
                                public void done(AVIMConversation avimConversation, AVIMException e) {
                                    if (e == null) {
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
            }
        });
    }
}
