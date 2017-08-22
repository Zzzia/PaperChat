package com.zia.magiccard.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.bumptech.glide.Glide;
import com.zia.magiccard.Base.MyToast;
import com.zia.magiccard.Bean.MessageData;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.ConversationUtil;
import com.zia.magiccard.Util.MessageUtil;
import com.zia.magiccard.Util.PageUtil;
import com.zia.magiccard.Util.UserUtil;
import com.zia.magiccard.View.AddFriendActivity;
import com.zia.magiccard.View.ChatActivity;
import com.zia.magiccard.View.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

    private static final String TAG = "MessageRecyclerTest";

    private Context context;
    private List<MessageData> messageDataList;
    private RecyclerView recyclerView = null;
    private DateFormat dateFormat = new SimpleDateFormat("HH:mm");


    public MessageRecyclerAdapter(Context context){
        this.context = context;
        messageDataList = new ArrayList<>();
    }

    private interface OnDataFreshed{
        void onFresh();
    }

    private void freshData(int count, final int scrollPosition, final OnDataFreshed call){
        if(ChatActivity.currentConversationId == null) return;
        messageDataList.clear();
        MessageUtil.getInstance()
                .getClient()
                .getConversation(ChatActivity.currentConversationId)
                .queryMessages(count,new AVIMMessagesQueryCallback() {
                    @Override
                    public void done(List<AVIMMessage> list, AVIMException e) {
                        Log.d(TAG,"listSize:"+list.size());
                        Observable.from(list)
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .map(new Func1<AVIMMessage, MessageData>() {
                                    @Override
                                    public MessageData call(AVIMMessage avimMessage) {

                                        if(avimMessage instanceof AVIMTextMessage){
                                            MessageData messageData = new MessageData();
                                            AVQuery<AVUser> query = new AVQuery<AVUser>("_User");
                                            try {
                                                AVUser user = query.get(avimMessage.getFrom());
                                                AVFile file = user.getAVFile("head");
                                                if(file != null){
                                                    messageData.setHeadUrl(file.getUrl());
                                                }
                                                messageData.setTime(dateFormat.format(avimMessage.getTimestamp()));
                                                messageData.setContent(((AVIMTextMessage) avimMessage).getText());
                                                messageData.setNickname(user.getString("nickname"));
                                                messageData.setUserId(user.getObjectId());
                                                if(avimMessage.getFrom().equals(AVUser.getCurrentUser().getObjectId())){
                                                    messageData.setType(TEXT_RIGHT);
                                                }else{
                                                    messageData.setType(TEXT_LEFT);
                                                }
                                            } catch (AVException e1) {
                                                e1.printStackTrace();
                                            }
                                            return messageData;
                                        }

                                        return null;
                                    }
                                })
                                .subscribe(new Subscriber<MessageData>() {
                                    @Override
                                    public void onCompleted() {
                                        ((Activity)context).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                notifyDataSetChanged();
                                                if(recyclerView != null){
                                                    recyclerView.smoothScrollToPosition(scrollPosition);
                                                }
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
        freshData(8,8,null);
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
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final MessageData messageData;
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case HEADER:
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((HeadHolder) holder).textView.setText("正在加载...");
                        freshData(messageDataList.size() + 8, messageDataList.size(), new OnDataFreshed() {
                            @Override
                            public void onFresh() {
                                ((HeadHolder) holder).textView.setText("点击加载更多");
                            }
                        });
                    }
                });
                break;
            case TEXT_LEFT:
                messageData = messageDataList.get(position-1);
                LeftTextHolder leftTextHolder = (LeftTextHolder) holder;
                leftTextHolder.content.setText(messageData.getContent());
                //点击头像访问资料
                leftTextHolder.head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        UserUtil.getUserById(messageData.getUserId(), new UserUtil.OnUserGet() {
                            @Override
                            public void getUserData(UserData userData) {
                                Intent intent = new Intent(context, AddFriendActivity.class);
                                intent.putExtra("userData",userData);
                                PageUtil.gotoPageWithCard(context,view,intent);
                            }

                            @Override
                            public void onError(AVException e) {
                                MyToast.showToast(context,"网络出现了问题呢");
                            }
                        });
                    }
                });
                if(messageData.getHeadUrl() != null){
                    Glide.with(context).load(messageData.getHeadUrl()).into(leftTextHolder.head);
                }
                break;
            case TEXT_RIGHT:
                messageData = messageDataList.get(position-1);
                RightTextHolder rightTextHolder = (RightTextHolder) holder;
                rightTextHolder.content.setText(messageData.getContent());
                if(MainActivity.userData.getHeadUrl() != null){
                    Glide.with(context).load(MainActivity.userData.getHeadUrl()).into(rightTextHolder.head);
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

    class LeftTextHolder extends RecyclerView.ViewHolder {

        private ImageView head;
        private TextView nickname,content;

        LeftTextHolder(View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.item_message_left_text_head);
            content = itemView.findViewById(R.id.item_message_left_text_tv);
        }
    }

    class RightTextHolder extends  RecyclerView.ViewHolder {

        private ImageView head;
        private TextView nickname,content;

        RightTextHolder(View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.item_message_right_text_head);
            content = itemView.findViewById(R.id.item_message_right_text_tv);
        }
    }

    class HeadHolder extends  RecyclerView.ViewHolder {

        TextView textView;

        public HeadHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_head_text);
        }
    }
}
