package com.zia.magiccard.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.bumptech.glide.Glide;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.PushUtil;
import com.zia.magiccard.Util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zia on 17-8-17.
 */

public class ConversationRecyclerAdapter extends RecyclerView.Adapter<ConversationRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<ConversationData> messageList = new ArrayList<>();

    public ConversationRecyclerAdapter(Context context){
        this.context = context;
    }

    public void freshMessageList(List<ConversationData> messageList){
        this.messageList = messageList;
        notifyDataSetChanged();
        PushUtil.saveConversations();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_conversation,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ConversationData conversationData = messageList.get(position);
        holder.name.setText(conversationData.getName());
        holder.content.setText(conversationData.getLastContent());
        holder.time.setText(TimeUtil.getDateString(conversationData.getTime()));
        if(conversationData.getImageUrl() != null){
            Glide.with(context).load(conversationData.getImageUrl()).into(holder.headImage);
        }
        holder.unread.setText(conversationData.getUnreadCount()+"");
        if(conversationData.getUnreadCount() != 0){
            holder.unread.setVisibility(View.VISIBLE);
        }else{
            holder.unread.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView headImage;
        TextView name,content,time,unread;

        public ViewHolder(View itemView) {
            super(itemView);
            headImage = itemView.findViewById(R.id.item_message_image);
            name = itemView.findViewById(R.id.item_message_name);
            content = itemView.findViewById(R.id.item_message_content);
            time = itemView.findViewById(R.id.item_message_time);
            unread = itemView.findViewById(R.id.item_conversation_count);
        }
    }
}
