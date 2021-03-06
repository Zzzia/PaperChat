package com.zia.magiccard.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avos.avoscloud.im.v2.messages.AVIMVideoMessage;
import com.bumptech.glide.Glide;
import com.zia.magiccard.Base.MyToast;
import com.zia.magiccard.Bean.MessageData;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.AudioPlayer;
import com.zia.magiccard.Util.AudioTrackManager;
import com.zia.magiccard.Util.ConversationUtil;
import com.zia.magiccard.Util.MarkdownUtil;
import com.zia.magiccard.Util.MessageUtil;
import com.zia.magiccard.Util.PageUtil;
import com.zia.magiccard.Util.TimeUtil;
import com.zia.magiccard.Util.UserCacheUtil;
import com.zia.magiccard.Util.UserUtil;
import com.zia.magiccard.View.AddFriendActivity;
import com.zia.magiccard.View.ChatActivity;
import com.zia.magiccard.View.MainActivity;
import com.zia.magiccard.View.PhotoActivity;
import com.zia.magiccard.View.VideoActivity;
import com.zzhoujay.richtext.RichText;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zia on 17-8-20.
 */

public class MessageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER = -1;
    public static final int TEXT_LEFT = 0;
    public static final int TEXT_RIGHT = 1;
    public static final int AUDIO_RIGHT = 2;
    public static final int AUDIO_LEFT = 3;
    public static final int PICTURE_RIGHT = 4;
    public static final int PICTURE_LEFT = 5;
    public static final int VIDEO_RIGHT = 6;
    public static final int VIDEO_LEFT = 7;

    private static final String TAG = "MessageRecyclerTest";

    private Context context;
    private int currentCount = 0;
    private List<MessageData> messageDataList;
    private RecyclerView recyclerView = null;
    private boolean isPlaying = false;//录音
    private AudioPlayer audioPlayer;
    private boolean isGroup = false;
    private View rootView = null;
    private String text = "";


    public MessageRecyclerAdapter(Context context){
        this.context = context;
        messageDataList = new ArrayList<>();
    }

    public void setRootView(View view){
        this.rootView = view;
    }

    //设置是否为群聊
    public void setIsGroup(boolean t){
        isGroup = t;
    }

    private interface OnDataFreshed{
        void onFresh();
    }

    public void scrollToLast(){
        if(recyclerView != null){
            recyclerView.smoothScrollToPosition(messageDataList.size());
        }
    }

    private void freshData(int count, final OnDataFreshed call){
        if(ChatActivity.currentConversationId == null) return;
        messageDataList.clear();
        MessageUtil.getInstance()
                .getClient()
                .getConversation(ChatActivity.currentConversationId)
                .queryMessages(count,new AVIMMessagesQueryCallback() {
                    @Override
                    public void done(List<AVIMMessage> list, AVIMException e) {
                        if(list == null) return;
                        Log.d(TAG,"listSize:"+list.size());
                        Observable.from(list)
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .map(new Func1<AVIMMessage, MessageData>() {
                                    @Override
                                    public MessageData call(AVIMMessage avimMessage) {
                                        //组装基础信息
                                        MessageData messageData = new MessageData();
                                        try {
                                            UserData userData = UserCacheUtil.getInstance().getUserDataById(avimMessage.getFrom());
                                            messageData.setTime(avimMessage.getTimestamp());
                                            messageData.setNickname(userData.getNickname());
                                            messageData.setHeadUrl(userData.getHeadUrl());
                                            messageData.setUserId(userData.getObjectId());
                                        } catch (AVException e1) {
                                            e1.printStackTrace();
                                        }
                                        //分别处理不同类型消息

                                        //处理文本消息
                                        if(avimMessage instanceof AVIMTextMessage){
                                            messageData.setContent(((AVIMTextMessage) avimMessage).getText());
                                            if(avimMessage.getFrom().equals(AVUser.getCurrentUser().getObjectId())){
                                                messageData.setType(TEXT_RIGHT);
                                            }else{
                                                messageData.setType(TEXT_LEFT);
                                            }
                                        }
                                        //处理语音消息
                                        if(avimMessage instanceof AVIMAudioMessage){
                                            messageData.setContent(((AVIMAudioMessage) avimMessage).getText());
                                            messageData.setAudioUrl(((AVIMAudioMessage) avimMessage).getFileUrl());
                                            if(avimMessage.getFrom().equals(AVUser.getCurrentUser().getObjectId())){
                                                messageData.setType(AUDIO_RIGHT);
                                            }else{
                                                messageData.setType(AUDIO_LEFT);
                                            }
                                        }
                                        //处理图片消息
                                        if(avimMessage instanceof AVIMImageMessage){
                                            messageData.setPhotoUrl(((AVIMImageMessage) avimMessage).getFileUrl());
                                            if(avimMessage.getFrom().equals(AVUser.getCurrentUser().getObjectId())){
                                                messageData.setType(PICTURE_RIGHT);
                                            }else{
                                                messageData.setType(PICTURE_LEFT);
                                            }
                                        }
                                        //处理视频消息
                                        if(avimMessage instanceof AVIMVideoMessage){
                                            Map<String, Object> metaData = ((AVIMVideoMessage)avimMessage).getAttrs();
                                            if(metaData.get("photoUrl") != null){
                                                messageData.setPhotoUrl((String)metaData.get("photoUrl"));
                                            }
                                            if((((AVIMVideoMessage)avimMessage).getFileUrl() != null)){
                                                messageData.setVideoUrl(((AVIMVideoMessage)avimMessage).getFileUrl());
                                            }
                                            if(avimMessage.getFrom().equals(AVUser.getCurrentUser().getObjectId())){
                                                messageData.setType(VIDEO_RIGHT);
                                            }else{
                                                messageData.setType(VIDEO_LEFT);
                                            }
                                        }
                                        return messageData;
                                    }
                                })
                                .subscribe(new Subscriber<MessageData>() {
                                    @Override
                                    public void onCompleted() {
                                        currentCount = messageDataList.size();
                                        ((Activity)context).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                notifyDataSetChanged();
                                                if(call != null) call.onFresh();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onNext(MessageData messageData) {
                                        if(messageData != null){
                                            messageDataList.add(messageData);
                                            Log.d(TAG,messageData.toString());
                                        }
                                    }
                                });
                    }
                });
    }

    public void freshData(){
        freshData(15, new OnDataFreshed() {
            @Override
            public void onFresh() {
                if(recyclerView != null){
                    recyclerView.scrollToPosition(15);
                    if(messageDataList.size() < 15){
                        recyclerView.scrollToPosition(messageDataList.size());
                    }
                }
            }
        });
    }

    public void addData(MessageData messageData){
        messageDataList.add(messageData);
        notifyDataSetChanged();
        if(recyclerView != null){
            recyclerView.smoothScrollToPosition(messageDataList.size());
        }
    }

    public void setRecyclerView(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder = null;
        switch (viewType){
            case HEADER:
                view = LayoutInflater.from(context).inflate(R.layout.item_head,parent,false);
                holder = new HeadHolder(view);
                break;
            case TEXT_LEFT:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_left_text,parent,false);
                holder = new LeftTextHolder(view);
                break;
            case TEXT_RIGHT:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_right_text,parent,false);
                holder = new RightTextHolder(view);
                break;
            case AUDIO_RIGHT:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_right_text,parent,false);
                holder = new RightTextHolder(view);
                break;
            case AUDIO_LEFT:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_left_text,parent,false);
                holder = new LeftTextHolder(view);
                break;
            case PICTURE_LEFT:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_left_picture,parent,false);
                holder = new LeftPictureHolder(view);
                break;
            case PICTURE_RIGHT:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_right_picture,parent,false);
                holder = new RightPictureHolder(view);
                break;
            case VIDEO_RIGHT:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_right_video,parent,false);
                holder = new RightVideoHolder(view);
                break;
            case VIDEO_LEFT:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_left_video,parent,false);
                holder = new LeftVideoHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final MessageData messageData;
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case HEADER:
                if(messageDataList.size() >= 5){
                    ((HeadHolder) holder).textView.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((HeadHolder) holder).textView.setText("正在加载...");
                        final int count = currentCount;
                        freshData(messageDataList.size() + 14, new OnDataFreshed() {
                            @Override
                            public void onFresh() {
                                if(count == currentCount){
                                    ((HeadHolder) holder).textView.setText("没有更多了");
                                    ((HeadHolder) holder).textView.setClickable(false);
                                    if(recyclerView != null){
                                        recyclerView.scrollToPosition(0);
                                    }
                                    return;
                                }
                                if(recyclerView != null){
                                    recyclerView.scrollToPosition(15);
                                }
                                ((HeadHolder) holder).textView.setText("点击加载更多");
                            }
                        });
                    }
                });
                }
                else{
                    ((HeadHolder) holder).textView.setVisibility(View.GONE);
                }
                break;
            case TEXT_LEFT:
                messageData = messageDataList.get(position-1);
                LeftTextHolder leftTextHolder = (LeftTextHolder) holder;
                text = messageData.getContent();
                if(text.length() >= 3 && text.substring(text.length()-3,text.length()).equals("#md")){
                    RichText.fromMarkdown(text.substring(0,text.length()-3)).into(leftTextHolder.content);
                    leftTextHolder.content.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.e("MessageAdapter","item click");
                            MarkdownUtil.previewMarkDown(context,rootView,messageData.getContent().substring(0,text.length()-3));
                        }
                    });
                }else{
                    leftTextHolder.content.setText(messageData.getContent());
                }
                //点击头像访问资料
                leftTextHolder.head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        UserCacheUtil.getInstance().getUserDataAsyncById(messageData.getUserId(), new UserCacheUtil.OnUserDataGet() {
                            @Override
                            public void onUserFind(UserData userData) {
                                Intent intent = new Intent(context, AddFriendActivity.class);
                                intent.putExtra("userData",userData);
                                PageUtil.gotoPageWithCard(context,view,intent);
                            }
                        });
                    }
                });
                if(messageData.getHeadUrl() != null){
                    Glide.with(context).load(messageData.getHeadUrl()).into(leftTextHolder.head);
                }
                if((position >=2 && (float)((messageData.getTime() - messageDataList.get(position - 2).getTime())/(60*1000)) > 3)
                        || position == 1){
                    leftTextHolder.time.setVisibility(View.VISIBLE);
                    leftTextHolder.time.setText(TimeUtil.getDateString(messageData.getTime()));
                }
                if(isGroup){
                    leftTextHolder.nickname.setVisibility(View.VISIBLE);
                    leftTextHolder.nickname.setText(messageData.getNickname());
                }
                break;
            case TEXT_RIGHT:
                messageData = messageDataList.get(position-1);
                RightTextHolder rightTextHolder = (RightTextHolder) holder;
                text = messageData.getContent();
                if(text.length() >= 3 && text.substring(text.length()-3,text.length()).equals("#md")){
                    RichText.fromMarkdown(text.substring(0,text.length()-3)).into(rightTextHolder.content);
                    rightTextHolder.content.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.e("MessageAdapter","item click");
                            MarkdownUtil.previewMarkDown(context,rootView,messageData.getContent().substring(0,text.length()-3));
                        }
                    });
                }else{
                    rightTextHolder.content.setText(messageData.getContent());
                }
                if(MainActivity.userData != null && MainActivity.userData.getHeadUrl() != null){
                    Glide.with(context).load(MainActivity.userData.getHeadUrl()).into(rightTextHolder.head);
                }
                //时间超过三分钟，显示时间
                if((position >=2 && (float)((messageData.getTime() - messageDataList.get(position - 2).getTime())/(60*1000)) > 3)
                        || position == 1){
                    rightTextHolder.time.setVisibility(View.VISIBLE);
                    rightTextHolder.time.setText(TimeUtil.getDateString(messageData.getTime()));
                }
                break;
            case AUDIO_RIGHT:
                messageData = messageDataList.get(position-1);
                rightTextHolder = (RightTextHolder) holder;
                rightTextHolder.content.setText(messageData.getContent());
                if(MainActivity.userData != null && MainActivity.userData.getHeadUrl() != null){
                    Glide.with(context).load(MainActivity.userData.getHeadUrl()).into(rightTextHolder.head);
                }
                //时间超过三分钟，显示时间
                if((position >=2 && (float)((messageData.getTime() - messageDataList.get(position - 2).getTime())/(60*1000)) > 3)
                        || position == 1){
                    rightTextHolder.time.setVisibility(View.VISIBLE);
                    rightTextHolder.time.setText(TimeUtil.getDateString(messageData.getTime()));
                }
                rightTextHolder.record.setVisibility(View.VISIBLE);
                rightTextHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!isPlaying &&messageData.getAudioUrl() != null){
                            try {
                                AudioTrackManager.getInstance().loadAudio(messageData.getAudioUrl(), System.currentTimeMillis() + "", new AudioTrackManager.LoadAudioListener() {
                                    @Override
                                    public void finish(String path) {
                                        audioPlayer = new AudioPlayer(path);
                                        audioPlayer.start();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if(isPlaying){
                            if(audioPlayer != null) audioPlayer.stop();
                        }
                    }
                });
                break;
            case AUDIO_LEFT:
                messageData = messageDataList.get(position-1);
                leftTextHolder = (LeftTextHolder) holder;
                leftTextHolder.content.setText(messageData.getContent());
                if(messageData.getHeadUrl() != null){
                    Glide.with(context).load(messageData.getHeadUrl()).into(leftTextHolder.head);
                }
                //时间超过三分钟，显示时间
                if((position >=2 && (float)((messageData.getTime() - messageDataList.get(position - 2).getTime())/(60*1000)) > 3)
                        || position == 1){
                    leftTextHolder.time.setVisibility(View.VISIBLE);
                    leftTextHolder.time.setText(TimeUtil.getDateString(messageData.getTime()));
                }
                leftTextHolder.record.setVisibility(View.VISIBLE);
                leftTextHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!isPlaying &&messageData.getAudioUrl() != null){
                            try {
                                AudioTrackManager.getInstance().loadAudio(messageData.getAudioUrl(), System.currentTimeMillis() + "", new AudioTrackManager.LoadAudioListener() {
                                    @Override
                                    public void finish(String path) {
                                        audioPlayer = new AudioPlayer(path);
                                        audioPlayer.start();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if(isPlaying){
                            if(audioPlayer != null) audioPlayer.stop();
                        }
                    }
                });
                if(isGroup){
                    leftTextHolder.nickname.setVisibility(View.VISIBLE);
                    leftTextHolder.nickname.setText(messageData.getNickname());
                }
                break;
            case PICTURE_RIGHT:
                messageData = messageDataList.get(position-1);
                RightPictureHolder rightPictureHolder = (RightPictureHolder) holder;
                if(MainActivity.userData != null && MainActivity.userData.getHeadUrl() != null){
                    Glide.with(context).load(MainActivity.userData.getHeadUrl()).into(rightPictureHolder.head);
                }
                //时间超过三分钟，显示时间
                if((position >=2 && (float)((messageData.getTime() - messageDataList.get(position - 2).getTime())/(60*1000)) > 3)
                        || position == 1){
                    rightPictureHolder.time.setVisibility(View.VISIBLE);
                    rightPictureHolder.time.setText(TimeUtil.getDateString(messageData.getTime()));
                }
                if(messageData.getPhotoUrl() != null){
                    Glide.with(context).load(messageData.getPhotoUrl()).override(150,200).into(rightPictureHolder.image);
                }
                rightPictureHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, PhotoActivity.class);
                        intent.putExtra("url",messageData.getPhotoUrl());
                        PageUtil.gotoPageWithCard(context,view,intent);
                    }
                });
                break;
            case PICTURE_LEFT:
                messageData = messageDataList.get(position-1);
                LeftPictureHolder leftPictureHolder = (LeftPictureHolder) holder;
                if(messageData.getHeadUrl() != null){
                    Glide.with(context).load(messageData.getHeadUrl()).override(150,200).into(leftPictureHolder.head);
                }
                //时间超过三分钟，显示时间
                if((position >=2 && (float)((messageData.getTime() - messageDataList.get(position - 2).getTime())/(60*1000)) > 3)
                        || position == 1){
                    leftPictureHolder.time.setVisibility(View.VISIBLE);
                    leftPictureHolder.time.setText(TimeUtil.getDateString(messageData.getTime()));
                }
                if(messageData.getPhotoUrl() != null){
                    Glide.with(context).load(messageData.getPhotoUrl()).into(leftPictureHolder.image);
                }
                leftPictureHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, PhotoActivity.class);
                        intent.putExtra("url",messageData.getPhotoUrl());
                        PageUtil.gotoPageWithCard(context,view,intent);
                    }
                });
                if(isGroup){
                    leftPictureHolder.nickname.setVisibility(View.VISIBLE);
                    leftPictureHolder.nickname.setText(messageData.getNickname());
                }
                break;
            case VIDEO_RIGHT:
                messageData = messageDataList.get(position-1);
                final RightVideoHolder rightVideoHolder = (RightVideoHolder) holder;
                if(messageData.getHeadUrl() != null){
                    Glide.with(context).load(messageData.getHeadUrl()).into(rightVideoHolder.head);
                }
                //时间超过三分钟，显示时间
                if((position >=2 && (float)((messageData.getTime() - messageDataList.get(position - 2).getTime())/(60*1000)) > 3)
                        || position == 1){
                    rightVideoHolder.time.setVisibility(View.VISIBLE);
                    rightVideoHolder.time.setText(TimeUtil.getDateString(messageData.getTime()));
                }
                if(messageData.getPhotoUrl() != null){
                    Glide.with(context).load(messageData.getPhotoUrl()).override(180,280).into(rightVideoHolder.photo);
                }
                rightVideoHolder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        rightVideoHolder.play.setVisibility(View.VISIBLE);
                        rightVideoHolder.photo.setVisibility(View.VISIBLE);
                        rightVideoHolder.videoView.pause();
                    }
                });
                rightVideoHolder.play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rightVideoHolder.play.setVisibility(View.GONE);
                        rightVideoHolder.photo.setVisibility(View.GONE);
                        rightVideoHolder.videoView.setMediaController(new MediaController(context));
                        rightVideoHolder.videoView.setVideoURI(Uri.parse(messageData.getVideoUrl()));
                        rightVideoHolder.videoView.start();
                    }
                });
                break;
            case VIDEO_LEFT:
                messageData = messageDataList.get(position-1);
                final LeftVideoHolder leftVideoHolder = (LeftVideoHolder) holder;
                if(messageData.getHeadUrl() != null){
                    Glide.with(context).load(messageData.getHeadUrl()).into(leftVideoHolder.head);
                }
                //时间超过三分钟，显示时间
                if((position >=2 && (float)((messageData.getTime() - messageDataList.get(position - 2).getTime())/(60*1000)) > 3)
                        || position == 1){
                    leftVideoHolder.time.setVisibility(View.VISIBLE);
                    leftVideoHolder.time.setText(TimeUtil.getDateString(messageData.getTime()));
                }
                if(messageData.getPhotoUrl() != null){
                    Glide.with(context).load(messageData.getPhotoUrl()).override(180,280).into(leftVideoHolder.photo);
                }
                leftVideoHolder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        leftVideoHolder.play.setVisibility(View.VISIBLE);
                        leftVideoHolder.photo.setVisibility(View.VISIBLE);
                        leftVideoHolder.videoView.pause();
                    }
                });
                leftVideoHolder.play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        leftVideoHolder.play.setVisibility(View.GONE);
                        leftVideoHolder.photo.setVisibility(View.GONE);
                        leftVideoHolder.videoView.setMediaController(new MediaController(context));
                        leftVideoHolder.videoView.setVideoURI(Uri.parse(messageData.getVideoUrl()));
                        leftVideoHolder.videoView.start();
                    }
                });
                if(isGroup){
                    leftVideoHolder.nickname.setVisibility(View.VISIBLE);
                    leftVideoHolder.nickname.setText(messageData.getNickname());
                }
                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) return HEADER;
        return messageDataList.get(position-1).getType();
    }

    @Override
    public int getItemCount() {
        if(messageDataList.size() == 0) return 0;
        return messageDataList.size()+1;
    }

    private class LeftTextHolder extends RecyclerView.ViewHolder {

        private ImageView head,record;
        private TextView time,content,nickname;

        LeftTextHolder(View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.item_message_left_text_head);
            content = itemView.findViewById(R.id.item_message_left_text_tv);
            time = itemView.findViewById(R.id.item_message_left_text_time);
            record = itemView.findViewById(R.id.item_message_left_text_record);
            nickname = itemView.findViewById(R.id.item_message_left_text_nickname);
        }
    }

    private class RightTextHolder extends  RecyclerView.ViewHolder {

        private ImageView head,record;
        private TextView time,content,nickname;

        RightTextHolder(View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.item_message_right_text_head);
            content = itemView.findViewById(R.id.item_message_right_text_tv);
            time = itemView.findViewById(R.id.item_message_right_text_time);
            record = itemView.findViewById(R.id.item_message_right_text_record);
            nickname = itemView.findViewById(R.id.item_message_right_text_nickname);
        }
    }

    private class HeadHolder extends  RecyclerView.ViewHolder {

        TextView textView;

        HeadHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_head_text);
        }
    }

    private class LeftPictureHolder extends RecyclerView.ViewHolder {

        private ImageView head,image;
        private TextView time,nickname;

        LeftPictureHolder(View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.item_message_left_picture_head);
            image = itemView.findViewById(R.id.item_message_left_picture_image);
            time = itemView.findViewById(R.id.item_message_left_picture_time);
            nickname = itemView.findViewById(R.id.item_message_left_picture_nickname);
        }
    }

    private class RightPictureHolder extends RecyclerView.ViewHolder {

        private ImageView head,image;
        private TextView time,nickname;

        RightPictureHolder(View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.item_message_right_picture_head);
            image = itemView.findViewById(R.id.item_message_right_picture_image);
            time = itemView.findViewById(R.id.item_message_right_picture_time);
            nickname = itemView.findViewById(R.id.item_message_right_picture_nickname);
        }
    }

    private class RightVideoHolder extends RecyclerView.ViewHolder {

        private VideoView videoView;
        private ImageView photo,head,play;
        private TextView time,nickname;

        RightVideoHolder(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.item_message_right_video_videoview);
            head = itemView.findViewById(R.id.item_message_right_video_head);
            photo = itemView.findViewById(R.id.item_message_right_video_photo);
            play = itemView.findViewById(R.id.item_message_right_video_play);
            time = itemView.findViewById(R.id.item_message_right_video_time);
            nickname = itemView.findViewById(R.id.item_message_right_video_nickname);
        }
    }

    private class LeftVideoHolder extends RecyclerView.ViewHolder {

        private VideoView videoView;
        private ImageView photo,head,play;
        private TextView time,nickname;

        LeftVideoHolder(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.item_message_left_video_videoview);
            head = itemView.findViewById(R.id.item_message_left_video_head);
            photo = itemView.findViewById(R.id.item_message_left_video_photo);
            play = itemView.findViewById(R.id.item_message_left_video_play);
            time = itemView.findViewById(R.id.item_message_left_video_time);
            nickname = itemView.findViewById(R.id.item_message_left_video_nickname);
        }
    }
}
