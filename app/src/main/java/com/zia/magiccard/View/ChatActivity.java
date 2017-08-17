package com.zia.magiccard.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.zia.magiccard.Base.BaseActivity;
import com.zia.magiccard.Presenter.ChatPresenter;
import com.zia.magiccard.Presenter.ChatPresenterImp;
import com.zia.magiccard.R;

import java.util.Arrays;

public class ChatActivity extends BaseActivity implements ChatImp {

    private ChatPresenterImp presenterImp;
    private EditText editText;
    private Button sendButton;
    private AVIMClient client;

    @Override
    protected void onCreated() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessageToJerryFromTom();
            }
        });
    }

    private void sendMessageToJerryFromTom(){
        client.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if(e == null){
                    avimClient.createConversation(Arrays.asList("599542005c497d0057d6838b"), "ziaTest", null,
                            new AVIMConversationCreatedCallback() {
                        @Override
                        public void done(AVIMConversation avimConversation, AVIMException e) {
                            if(e == null){
                                final String text = editText.getText().toString();
                                if(text.isEmpty()) return;
                                AVIMTextMessage message = new AVIMTextMessage();
                                message.setText(text);
                                avimConversation.sendMessage(message, new AVIMConversationCallback() {
                                    @Override
                                    public void done(AVIMException e) {
                                        if(e == null){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    toast("发送成功："+text);
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

    @Override
    protected void findWidgets() {
        presenterImp = new ChatPresenter(this);
        editText = $(R.id.chat_edit);
        sendButton = $(R.id.chat_send);
        client = AVIMClient.getInstance(AVUser.getCurrentUser());
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void beforeSetContentView() {

    }
}
