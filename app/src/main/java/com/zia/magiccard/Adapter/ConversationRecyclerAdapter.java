package com.zia.magiccard.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationsQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMSingleMessageQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avos.avoscloud.im.v2.messages.AVIMVideoMessage;
import com.bumptech.glide.Glide;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.ConversationUtil;
import com.zia.magiccard.Util.GroupView;
import com.zia.magiccard.Util.MessageUtil;
import com.zia.magiccard.Util.PushUtil;
import com.zia.magiccard.Util.TimeUtil;
import com.zia.magiccard.Util.UserCacheUtil;
import com.zia.magiccard.View.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by zia on 17-8-17.
 */

public class ConversationRecyclerAdapter extends RecyclerView.Adapter<ConversationRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<ConversationData> messageList = new ArrayList<>();
    private List<AVIMConversation> conversationList = new ArrayList<>();
    private final static String TAG = "ConversationAdapterTest";

    public ConversationRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void freshMessageList(List<ConversationData> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
        if (messageList.size() > 0) {
            PushUtil.saveConversations();
        }
    }

    /**
     * 从网络中获取消息列表，并刷新Recycler
     */
    public void pullConversationList() {
        MessageUtil.getInstance().getClient().open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e == null) {
                    AVIMConversationsQuery query = avimClient.getConversationsQuery();
                    query.setLimit(30);
                    query.findInBackground(new AVIMConversationQueryCallback() {
                        @Override
                        public void done(List<AVIMConversation> list, AVIMException e) {
                            if (e == null) {
                                Log.e(TAG, "conversation Size: " + list.size());
                                Collections.reverse(list);
                                conversationList = list;
                                MainActivity.conversations = list;
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        notifyDataSetChanged();
                                    }
                                });
                            } else e.printStackTrace();
                        }
                    });
                } else e.printStackTrace();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_conversation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AVIMConversation conversation = conversationList.get(position);
        Log.e(TAG, "bind holder :" + position);
        conversation.fetchInfoInBackground(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    final List<String> members = new ArrayList<>();
                    members.addAll(conversation.getMembers());
                    //设置对话名字和头像
                    if (members.size() == 2) {//单人对话
                        for (int i = 0; i < members.size(); i++) {
                            if (members.get(i).equals(AVUser.getCurrentUser().getObjectId()))
                                members.remove(i);
                            else Log.e(TAG, "members delete error");
                        }
                        UserCacheUtil.getInstance().getUserDataAsyncById(members.get(0), new UserCacheUtil.OnUserDataGet() {
                            @Override
                            public void onUserFind(final UserData userData) {
                                holder.name.setText(userData.getNickname());
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(userData.getHeadUrl() != null){
                                            Glide.with(context).load(userData.getHeadUrl()).into(holder.headImage);
                                        }else{
                                            Glide.with(context).load(R.mipmap.head_default).into(holder.headImage);
                                        }
                                    }
                                });
                            }
                        });
                        Log.e(TAG, "holder.name.setText");
                    } else {//多人对话
                        String name = null;
                        try {
                            name = UserCacheUtil.getInstance().getUserDataById(members.get(0)).getNickname();
                            name = name + "、";
                            name = name + UserCacheUtil.getInstance().getUserDataById(members.get(1)).getNickname();
                        } catch (AVException e1) {
                            e1.printStackTrace();
                        }
                        name = name + "等";
                        name = name + (members.size() + 1);
                        name = name + "人的聊天";
                        final String finalName = name;
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                holder.name.setText(finalName);
                                holder.headImage.setImageResource(R.mipmap.group);
                            }
                        });

                    }
                    conversation.getLastMessage(new AVIMSingleMessageQueryCallback() {
                        @Override
                        public void done(final AVIMMessage message, AVIMException e) {
                            if(e == null){
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(message instanceof AVIMTextMessage){
                                        holder.content.setText(((AVIMTextMessage) message).getText());
                                    }
                                    else if(message instanceof AVIMAudioMessage){
                                        holder.content.setText("[语音]");
                                    }
                                    else if(message instanceof AVIMImageMessage){
                                        holder.content.setText("[图片]");
                                    }
                                    else if(message instanceof AVIMVideoMessage){
                                        holder.content.setText("[视频]");
                                    }
                                    else holder.content.setText("[未知消息]");
                                    holder.time.setText(TimeUtil.getDateString(conversation.getUpdatedAt().getTime()));
                                    holder.unread.setText(conversation.getUnreadMessagesCount());
                                    if (conversation.getUnreadMessagesCount() == 0) {
                                        holder.unread.setVisibility(View.INVISIBLE);
                                    } else {
                                        holder.unread.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }else e.printStackTrace();
                        }
                    });
                } else e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView headImage;
        TextView name, content, time, unread;

        private ViewHolder(View itemView) {
            super(itemView);
            headImage = itemView.findViewById(R.id.item_message_image);
            name = itemView.findViewById(R.id.item_message_name);
            content = itemView.findViewById(R.id.item_message_content);
            time = itemView.findViewById(R.id.item_message_time);
            unread = itemView.findViewById(R.id.item_conversation_count);
        }
    }
}
