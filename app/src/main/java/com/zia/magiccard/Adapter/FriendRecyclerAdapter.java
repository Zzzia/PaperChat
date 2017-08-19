package com.zia.magiccard.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.zia.magiccard.R;
import com.zia.magiccard.Util.FullRecyclerView;
import com.zia.magiccard.Util.MyRecyclerView;
import com.zia.magiccard.Util.PageUtil;
import com.zia.magiccard.Util.ScreenUtil;
import com.zia.magiccard.View.GroupActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zia on 17-8-18.
 */

public class FriendRecyclerAdapter extends RecyclerView.Adapter<FriendRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Boolean> isOpen;

    public FriendRecyclerAdapter(Context context){
        this.context = context;
        isOpen = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friend,parent,false);
        return new FriendRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PageUtil.gotoPageWithCard(context,view, GroupActivity.class);
                }
            });
    }



    @Override
    public int getItemCount() {
        isOpen.add(false);
        isOpen.add(false);
        isOpen.add(false);
        isOpen.add(false);
        isOpen.add(false);
        return 5;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView gourp;
        CardView cardView;
//        FullRecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            gourp = itemView.findViewById(R.id.item_friend_group);
            cardView = itemView.findViewById(R.id.item_friend_card);
//            recyclerView = itemView.findViewById(R.id.item_friend_recycler);
        }
    }
}
