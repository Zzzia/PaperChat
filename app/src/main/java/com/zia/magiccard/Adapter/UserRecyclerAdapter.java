package com.zia.magiccard.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.zia.magiccard.Base.MyToast;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.PageUtil;
import com.zia.magiccard.View.AddFriendActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zia on 17-8-18.
 */

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<UserData> userDataList = new ArrayList<>();

    public UserRecyclerAdapter(Context context){
        this.context = context;
    }

    public void refreshData(List<UserData> userDataList){
        if(userDataList == null) return;
        this.userDataList = userDataList;
        notifyDataSetChanged();
    }

    public List<UserData> getUserDataList(){
        return userDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_person,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final UserData userData = userDataList.get(position);
        holder.name.setText(userData.getNickname());
        holder.imageCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddFriendActivity.class);
                intent.putExtra("userData",userData);
                PageUtil.gotoPageWithCard(context,view,intent);
            }
        });
        //如果是本人，取消监听
        if(userData.getObjectId().equals(AVUser.getCurrentUser().getObjectId())){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyToast.showToast(context,"感觉自己萌萌哒");
                }
            });
        }
        if(userData.getIntroduce() != null){
            holder.introduce.setText(userData.getIntroduce());
        }
        if(userData.getHeadUrl() != null){
            Glide.with(context).load(userData.getHeadUrl()).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return userDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,introduce;
        CardView imageCard;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_person_name);
            introduce = itemView.findViewById(R.id.item_person_introduce);
            image = itemView.findViewById(R.id.item_person_image);
            imageCard = itemView.findViewById(R.id.item_person_imageCard);
        }
    }
}
