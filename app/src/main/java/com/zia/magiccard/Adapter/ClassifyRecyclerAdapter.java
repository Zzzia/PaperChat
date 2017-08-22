package com.zia.magiccard.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zia.magiccard.Bean.ClassifyData;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.PageUtil;
import com.zia.magiccard.View.GroupActivity;
import com.zia.magiccard.View.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zia on 17-8-18.
 */

public class ClassifyRecyclerAdapter extends RecyclerView.Adapter<ClassifyRecyclerAdapter.ViewHolder> {

    private Context context;

    public ClassifyRecyclerAdapter(Context context){
        this.context = context;
    }

    public void freshData(){
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_class,parent,false);
        return new ClassifyRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,GroupActivity.class);
                    intent.putExtra("position",position);
                    PageUtil.gotoPageWithCard(context, view, intent);
                }
            });
    }



    @Override
    public int getItemCount() {
        return MainActivity.classifyDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView gourp;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            gourp = itemView.findViewById(R.id.item_friend_group);
            cardView = itemView.findViewById(R.id.item_friend_card);
        }
    }
}
