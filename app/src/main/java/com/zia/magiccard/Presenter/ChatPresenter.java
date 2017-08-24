package com.zia.magiccard.Presenter;

import android.util.Log;
import android.widget.TextView;

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
            modelImp.sendTextMessage(text, conversationData, new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if(e==null){
                        Log.e(TAG,"发送消息成功");
                    }else{
                        e.printStackTrace();
                    }
                }
            });
        }
        if(imp.getUserData() != null){
            modelImp.sendTextMessage(text, imp.getUserData(), new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if(e==null){
                        Log.e(TAG,"发送消息成功");
                    }else{
                        e.printStackTrace();
                    }
                }
            });
        }
        imp.getEditText().setText("");
    }



    /**
     * 初始化recycler数据
     */
    @Override
    public void initData() {
        if(imp.getConversationData() != null){
            Log.d(TAG,"conversationId:"+imp.getConversationData().getConversationId());
            ChatActivity.currentConversationId = imp.getConversationData().getConversationId();
            imp.getMessageAdapter().freshData();
        }
        if(imp.getUserData() != null){
            MessageUtil.getInstance().createConversation(imp.getUserData(), new AVIMConversationCreatedCallback() {
                @Override
                public void done(AVIMConversation avimConversation, AVIMException e) {
                    Log.d(TAG,"conversationId:"+avimConversation.getConversationId());
                    ChatActivity.currentConversationId = avimConversation.getConversationId();
                    imp.getMessageAdapter().freshData();
                }
            });
        }
    }

    @Override
    public void sendAudio(final TextView hintView) {
        if(imp.getConversationData() != null){
            modelImp.sendAudioMessage(null, imp.getConversationData(), new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if(e == null){
                        imp.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hintView.setText("发送成功");
                            }
                        });
                    }
                }
            });
        }
        if(imp.getUserData() != null){
            modelImp.sendAudioMessage(null, imp.getUserData(), new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if(e == null){
                        imp.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hintView.setText("发送成功");
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void sendPicture(String path) {
        if(path == null || path.isEmpty()) return;
        imp.getDialog().show();
        if(imp.getConversationData() != null){
            modelImp.sendPictureMessage(path, imp.getConversationData(), new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if(e == null){
                        imp.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imp.getDialog().hide();
                            }
                        });
                    }
                }
            });
        }
        if(imp.getUserData() != null){
            modelImp.sendPictureMessage(path, imp.getUserData(), new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if(e == null){
                        imp.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imp.getDialog().hide();
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void sendVideo(String path) {
        if(path == null || path.isEmpty()) return;
        imp.getDialog().show();
        if(imp.getConversationData() != null){
            modelImp.sendVideoMessage(path, imp.getConversationData(), new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if(e == null){
                        imp.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imp.getDialog().hide();
                            }
                        });
                    }
                }
            });
        }
        if(imp.getUserData() != null){
            modelImp.sendVideoMessage(path, imp.getUserData(), new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if(e == null){
                        imp.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imp.getDialog().hide();
                            }
                        });
                    }
                }
            });
        }
    }
}
