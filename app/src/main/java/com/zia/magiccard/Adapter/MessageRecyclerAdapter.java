package com.zia.magiccard.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.bumptech.glide.Glide;
import com.zia.magiccard.Bean.MessageData;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.Model.UserModel;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.ConversationUtil;
import com.zia.magiccard.View.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zia on 17-8-20.
 */

public class MessageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TEXT_LEFT = 0;
    public static final int TEXT_RIGHT = 1;

    private static final String TAG = "MessageRecyclerTest";

    private Context context;
    private List<MessageData> messageDataList;
    private RecyclerView recyclerView = null;


    public MessageRecyclerAdapter(Context context){
        this.context = context;
        messageDataList = new ArrayList<>();
    }

    public void freshData(String conversationId){
        int position = ConversationUtil.getPositionByConversationId(conversationId);
        if(position == -1) return;
        if(MainActivity.conversationList.size() == 0) Log.d(TAG,"MainActivity.conversationList.size() == 0");
        messageDataList = MainActivity.conversationList.get(position).getMessageDatas();
        notifyDataSetChanged();
        if(recyclerView != null) recyclerView.smoothScrollToPosition(messageDataList.size());
    }

    public void setRecyclerView(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType){
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MessageData messageData = messageDataList.get(position);
        Log.d("messageRecyclerTest","message:\n"+messageData.toString());
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case TEXT_LEFT:
                LeftTextHolder leftTextHolder = (LeftTextHolder) holder;
                leftTextHolder.content.setText(messageData.getContent());
                if(messageData.getHeadUrl() != null){
                    Glide.with(context).load(messageData.getHeadUrl()).into(leftTextHolder.head);
                }
                break;
            case TEXT_RIGHT:
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
        return messageDataList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        if(messageDataList == null) return 0;
        return messageDataList.size();
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
}
