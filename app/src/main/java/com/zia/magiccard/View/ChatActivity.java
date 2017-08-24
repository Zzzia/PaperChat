package com.zia.magiccard.View;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zia.magiccard.Adapter.MessageRecyclerAdapter;
import com.zia.magiccard.Base.BaseActivity;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.Presenter.ChatPresenter;
import com.zia.magiccard.Presenter.ChatPresenterImp;
import com.zia.magiccard.Presenter.RecyclerViewPresenter;
import com.zia.magiccard.Presenter.RecyclerViewPresenterImp;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.MyRecordButton;
import com.zia.magiccard.Util.PermissionsUtil;
import com.zia.magiccard.View.Fragments.RecyclerViewImp;

import java.util.Arrays;
import java.util.List;

public class ChatActivity extends BaseActivity implements ChatImp,RecyclerViewImp {

    private ChatPresenterImp presenterImp;
    private RecyclerViewPresenterImp recyclerViewPresenter;
    private RecyclerView recyclerView;
    public static MessageRecyclerAdapter adapter;
    public static String currentConversationId = null;
    private EditText editText;
    private Button sendButton;
    private AVIMClient client;
    private ImageView record,photo,camera;
    private MyRecordButton recording;
    private RelativeLayout recordLayout;
    private TextView recordHint;
    private Chronometer timer;
    private boolean isRecord = false;
    private static final int PERMISSION_MIC = 0;
    private static final int PERMISION_DISK = 1;
    private static final int PICTURE_CODE = 2;

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
        //录音小按钮监听
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取权限
                if(PermissionsUtil.hasMicPermission(getActivity(),PERMISSION_MIC)){
                    adapter.scrollToLast();
                    recordHint.setText("按下以录音，上滑取消录音");
                    if(isRecord){
                        recordLayout.setVisibility(View.GONE);
                    }else{
                        recordLayout.setVisibility(View.VISIBLE);
                    }
                    isRecord = !isRecord;
                }

            }
        });
        //录音按钮监听
        setRecording();
        setPhoto();
    }

    private void setPhoto() {
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PermissionsUtil.hasDiskPermission(getActivity(),PERMISION_DISK)){
                    Matisse.from(getActivity())
                            .choose(MimeType.allOf())
                            .maxSelectable(6)
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.5f)
                            .imageEngine(new GlideEngine())
                            .forResult(PICTURE_CODE);
                }
            }
        });
    }

    private void setRecording(){
        recording.setRecordListener(new MyRecordButton.RecordListener() {
            @Override
            public void onBegin() {
                recordHint.setVisibility(View.INVISIBLE);
                timer.setVisibility(View.VISIBLE);
                timer.setBase(SystemClock.elapsedRealtime());//计时器清零
                int hour = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60);
                timer.setFormat("0"+String.valueOf(hour)+":%s");
                timer.start();
            }

            @Override
            public void onCancel() {
                toast("取消了录音");
            }

            @Override
            public void onFinish() {
                timer.stop();
                timer.setVisibility(View.INVISIBLE);
                recordHint.setVisibility(View.VISIBLE);
                recordHint.setText("正在发送，请稍等");
                presenterImp.sendAudio(recordHint);
            }

            @Override
            public void onTimeChange() {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(isRecord){
            recordLayout.setVisibility(View.GONE);
            isRecord = false;
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void findWidgets() {
        recyclerViewPresenter = new RecyclerViewPresenter(this);
        presenterImp = new ChatPresenter(this);
        adapter = new MessageRecyclerAdapter(this);
        recyclerView = $(R.id.chat_recycler);
        editText = $(R.id.chat_edit);
        sendButton = $(R.id.chat_send);
        record = $(R.id.chat_record);
        photo = $(R.id.chat_pick_photo);
        camera = $(R.id.chat_camera);
        recordLayout = $(R.id.chat_extra_recordLayout);
        recording = $(R.id.chat_recording);
        recordHint = $(R.id.chat_extra_hint);
        timer = $(R.id.chat_extra_timer);
        client = AVIMClient.getInstance(AVUser.getCurrentUser());
    }

    @Override
    protected void onStop() {
        super.onStop();
        currentConversationId = null;
        adapter = null;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_MIC:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{
                    toast("没有录音权限");
                }
                break;
            case PERMISION_DISK:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{
                    toast("没有相册权限");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_CODE && resultCode == RESULT_OK) {
            List<Uri> mSelected = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mSelected);
        }
    }
}
