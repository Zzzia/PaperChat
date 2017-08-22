package com.zia.magiccard.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.zia.magiccard.Adapter.MessageRecyclerAdapter;
import com.zia.magiccard.Base.BaseActivity;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.Presenter.ChatPresenter;
import com.zia.magiccard.Presenter.ChatPresenterImp;
import com.zia.magiccard.Presenter.RecyclerViewPresenter;
import com.zia.magiccard.Presenter.RecyclerViewPresenterImp;
import com.zia.magiccard.R;
import com.zia.magiccard.View.Fragments.RecyclerViewImp;

import java.util.Arrays;

public class ChatActivity extends BaseActivity implements ChatImp,RecyclerViewImp {

    private ChatPresenterImp presenterImp;
    private RecyclerViewPresenterImp recyclerViewPresenter;
    private RecyclerView recyclerView;
    public static MessageRecyclerAdapter adapter;
    private EditText editText;
    private Button sendButton;
    private AVIMClient client;

    @Override
    protected void onCreated() {
        //设置adapter
        recyclerViewPresenter.setRecyclerView();
        //为adapter设置recycler，接收到消息滑到最下面
        adapter.setRecyclerView(recyclerView);
        //初始化recycler数据
        presenterImp.initData();
        //发送消息
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenterImp.sendMessage();
            }
        });
    }

    @Override
    protected void findWidgets() {
        recyclerViewPresenter = new RecyclerViewPresenter(this);
        presenterImp = new ChatPresenter(this);
        adapter = new MessageRecyclerAdapter(this);
        recyclerView = $(R.id.chat_recycler);
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

    @Override
    public UserData getUserData() {
        if(getIntent() == null) return null;
        return (UserData)getIntent().getSerializableExtra("userData");
    }

    @Override
    public ConversationData getConversationData() {
        if(getIntent() == null) return null;
        return (ConversationData) getIntent().getSerializableExtra("conversationData");
    }

    @Override
    public EditText getEditText() {
        return editText;
    }

    @Override
    public AVIMClient getAVIMClient() {
        return client;
    }

    @Override
    public MessageRecyclerAdapter getMessageAdapter() {
        return adapter;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }
}
