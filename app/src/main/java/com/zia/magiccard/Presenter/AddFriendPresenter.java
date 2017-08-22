package com.zia.magiccard.Presenter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zia.magiccard.Bean.ClassifyData;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.PushUtil;
import com.zia.magiccard.Util.RecyclerItemDivider;
import com.zia.magiccard.View.AddFriendImp;
import com.zia.magiccard.View.MainActivity;

import static android.widget.ListPopupWindow.WRAP_CONTENT;

/**
 * Created by zia on 17-8-22.
 */

public class AddFriendPresenter implements AddFriendPresenterImp {

    private AddFriendImp imp;
    private PopupWindow window;

    public AddFriendPresenter(AddFriendImp imp){
        this.imp = imp;
    }

    @Override
    public void showPopWindow() {
        View view = imp.getActivity().getLayoutInflater().inflate(R.layout.classify_popwindow,null);
        RecyclerView recyclerView = view.findViewById(R.id.classify_popwindow_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(imp.getActivity()));
        recyclerView.setAdapter(new AddClassifyAdapter());
        recyclerView.addItemDecoration(new RecyclerItemDivider(imp.getActivity(),RecyclerItemDivider.VERTICAL_LIST));
        window = new PopupWindow(view,WRAP_CONTENT,WRAP_CONTENT);
        window.setAnimationStyle(R.style.popup_window_anim);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();
        window.setContentView(view);
        window.showAtLocation(imp.getRootView(), Gravity.CENTER, 0, 0);
    }

    @Override
    public void initData() {
        UserData userData = imp.getUserData();
        imp.getNickname().setText(userData.getNickname());
        imp.getIntroduce().setText(userData.getIntroduce());
        if(userData.isboy()){
            imp.getSex().setText("男");
        }else {
            imp.getSex().setText("女");
        }
        if(userData.getHeadUrl() != null){
            Glide.with(imp.getActivity()).load(userData.getHeadUrl()).into(imp.getHead());
        }
        out:for(ClassifyData classifyData : MainActivity.classifyDatas){
            for(UserData u : classifyData.getUserDatas()){
                if(u.getObjectId().equals(userData.getObjectId())){
                    imp.getAddButton().setVisibility(View.GONE);
                    break out;
                }
            }
        }
    }


    /**
     * 添加到分组的adapter
     */
    class AddClassifyAdapter extends RecyclerView.Adapter<AddClassifyAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(imp.getActivity()).inflate(R.layout.item_class1,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.textView.setText(MainActivity.classifyDatas.get(position).getClassName());
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //添加到该分组里
                    MainActivity.classifyDatas.get(position).getUserDatas().add(imp.getUserData());
                    //设置添加好友按钮不可见
                    imp.getAddButton().setVisibility(View.GONE);
                    //弹出通知
                    imp.toast("添加成功");
                    //保存分组信息到服务器
                    PushUtil.pushClassifyData();
                    window.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return MainActivity.classifyDatas.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView textView;

            ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.item_class1_text);
            }
        }
    }
}


