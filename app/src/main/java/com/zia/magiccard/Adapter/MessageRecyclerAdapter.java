package com.zia.magiccard.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.zia.magiccard.R;
import com.zia.magiccard.Util.MyCard;
import com.zia.magiccard.Util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zia on 17-8-17.
 */

public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.ViewHolder> {

    private static final int PERSON = 0;
    private static final int GROUP = 1;
    private Context context;
    private List<String> tempList = new ArrayList<>();

    public MessageRecyclerAdapter(Context context){
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        //return tempList.size();
        return 20;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        //MyCard card;

        public ViewHolder(View itemView) {
            super(itemView);
            //card = itemView.findViewById(R.id.item_message_card);
        }
    }
}
