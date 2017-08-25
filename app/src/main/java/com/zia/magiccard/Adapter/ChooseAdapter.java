package com.zia.magiccard.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zia on 17-8-25.
 */

public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.MyViewHolder>{

    private Context context;
    private List<UserData> selecteds;
    private List<UserData> userDatas;

    public ChooseAdapter(Context context,List<UserData> userDatas){
        this.context = context;
        selecteds = new ArrayList<>();
        this.userDatas = userDatas;
    }

    public List<UserData> getSelecteds(){
        return selecteds;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_choose_friends,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final UserData userData = userDatas.get(position);
        if(userData.getHeadUrl() != null){
            Glide.with(context).load(userData.getHeadUrl()).into(holder.head);
        }
        holder.nickname.setText(userData.getNickname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //没有选中
                if(holder.selected.getVisibility() == View.INVISIBLE){
                    holder.selected.setVisibility(View.VISIBLE);
                    if(!selecteds.contains(userData)){
                        selecteds.add(userData);
                    }
                }else{
                    holder.selected.setVisibility(View.INVISIBLE);
                    selecteds.remove(userData);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView selected,head;
        private TextView nickname;
        MyViewHolder(View itemView) {
            super(itemView);
            selected = itemView.findViewById(R.id.item_choose_selected);
            head = itemView.findViewById(R.id.item_choose_head);
            nickname = itemView.findViewById(R.id.item_choose_nickname);
        }
    }
}
