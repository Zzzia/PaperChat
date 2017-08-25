package com.zia.magiccard.View;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zia.magiccard.Adapter.MarkdownChooseAdapter;
import com.zia.magiccard.Adapter.MessageRecyclerAdapter;
import com.zia.magiccard.Base.BaseActivity;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.MarkdownData;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.Presenter.ChatPresenter;
import com.zia.magiccard.Presenter.ChatPresenterImp;
import com.zia.magiccard.Presenter.RecyclerViewPresenter;
import com.zia.magiccard.Presenter.RecyclerViewPresenterImp;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.MyRecordButton;
import com.zia.magiccard.Util.PageUtil;
import com.zia.magiccard.Util.PermissionsUtil;
import com.zia.magiccard.Util.PushUtil;
import com.zia.magiccard.Util.ScreenUtil;
import com.zia.magiccard.View.Fragments.RecyclerViewImp;

import java.util.List;

public class ChatActivity extends BaseActivity implements ChatImp,RecyclerViewImp {

    private static final String TAG = "ChatActivityTest";
    private ChatPresenterImp presenterImp;
    private RecyclerViewPresenterImp recyclerViewPresenter;
    private RecyclerView recyclerView;
    public static MessageRecyclerAdapter adapter = null;
    public static String currentConversationId = null;
    private CardView cardView;
    private EditText editText;
    private Button sendButton;
    private LinearLayout rootView;
    private ImageView record,photo,camera,markdown;
    private MyRecordButton recording;
    private RelativeLayout recordLayout;
    private TextView recordHint;
    private Chronometer timer;
    private ProgressDialog dialog;
    private boolean isRecord = false;
    private static final int PERMISSION_MIC = 0;
    private static final int PERMISSION_DISK = 1;
    private static final int PERMISSION_CAMERA = 3;
    private static final int PICTURE_CODE = 2;
    private static final int PICTURE_CAMERA = 4;
    private static final int FOR_RESULT = 5;
    public static final int FOR_MARKDOWN = 6;

    @Override
    protected void onCreated() {
        //设置adapter
        recyclerViewPresenter.setRecyclerView();
        //为adapter设置recycler，接收到消息滑到最下面
        adapter.setRecyclerView(recyclerView);

        adapter.setRootView(rootView);
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
        setCamera();
        setProgressDialog();
        setMarkDown();
    }

    private void setMarkDown() {
        markdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int width = ScreenUtil.bulid(getActivity()).getPxWide();
                int height = ScreenUtil.bulid(getActivity()).getPxHiget();
                View pop = LayoutInflater.from(ChatActivity.this).inflate(R.layout.choose_markdown,null);
                RecyclerView recyclerView = pop.findViewById(R.id.choose_markdown_recycler);
                TextView write = pop.findViewById(R.id.choose_markdown_new);
                TextView send = pop.findViewById(R.id.choose_markdown_save);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                final MarkdownChooseAdapter adapter = new MarkdownChooseAdapter(getActivity(),MainActivity.markdownDatas);
                final PopupWindow popupWindow = new PopupWindow(pop,width/6*5,height/4*3);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAtLocation(cardView, Gravity.CENTER,0,0);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        List<MarkdownData> markdownDatas = adapter.getSelecteds();
                        for(MarkdownData markdownData : markdownDatas){
                            presenterImp.sendMessage(markdownData.getContent());
                        }
                        popupWindow.dismiss();
                    }
                });
                write.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(),MarkDownActivity.class);
                        startActivityForResult(intent,FOR_MARKDOWN);
                        popupWindow.dismiss();
                    }
                });
            }
        });
    }

    private void setProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(true);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setTitle("请等待");
        dialog.setMessage("正在发送");
    }

    private void setCamera() {
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PermissionsUtil.hasCameraPermission(getActivity(),PERMISSION_CAMERA)
                && PermissionsUtil.hasMicPermission(getActivity(),PERMISSION_MIC)){
                    Intent intent = new Intent(getActivity(),CameraActivity.class);
                    startActivityForResult(intent, FOR_RESULT);
                }
            }
        });
    }

    private void setPhoto() {
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PermissionsUtil.hasDiskPermission(getActivity(), PERMISSION_DISK)){
                    Matisse.from(getActivity())
                            .choose(MimeType.allOf())
                            .countable(true)
                            .maxSelectable(6)
                            .theme(R.style.Matisse_PhotoPicker)
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.6f)
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
        markdown = $(R.id.chat_markdown);
        cardView = $(R.id.chat_card);
        recordLayout = $(R.id.chat_extra_recordLayout);
        recording = $(R.id.chat_recording);
        recordHint = $(R.id.chat_extra_hint);
        timer = $(R.id.chat_extra_timer);
        rootView = $(R.id.chat_rootView);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        currentConversationId = null;
//        adapter = null;
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
    public MessageRecyclerAdapter getMessageAdapter() {
        return adapter;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public ProgressDialog getDialog() {
        return dialog;
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
            case PERMISSION_DISK:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{
                    toast("没有相册权限");
                }
                break;
        }
    }

    private String getRealFilePath(final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_CODE && resultCode == RESULT_OK) {
            List<Uri> mSelected = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mSelected.get(0).getPath());
            for (Uri uri : mSelected){
                String path = getRealFilePath(uri);
                Log.d("path:", path);
                presenterImp.sendPicture(path);
            }
        }

        if(requestCode == FOR_RESULT && resultCode == RESULT_OK){
            String photoPath = data.getStringExtra("photo");
            if(data.getStringExtra("videoPath") != null){
                presenterImp.sendVideo(data.getStringExtra("videoPath"));
            }else{
                presenterImp.sendPicture(photoPath);
            }
        }

        if(requestCode == FOR_MARKDOWN && resultCode == RESULT_OK){
            String markdown = data.getStringExtra("markdown");
            if(markdown != null){
                markdown = markdown + "#md";
                presenterImp.sendMessage(markdown);
                MarkdownData markdownData = new MarkdownData();
                markdownData.setTitle("md");
                markdownData.setContent(markdown);
                MainActivity.markdownDatas.add(markdownData);
                PushUtil.pushMarkdownData();
            }
        }
    }
}
