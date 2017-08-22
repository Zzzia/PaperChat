package com.zia.magiccard.View;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zia.magiccard.Adapter.UserRecyclerAdapter;
import com.zia.magiccard.Base.BaseActivity;
import com.zia.magiccard.Presenter.RecyclerViewPresenter;
import com.zia.magiccard.Presenter.RecyclerViewPresenterImp;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.ConversationUtil;
import com.zia.magiccard.Util.MyRecyclerView;
import com.zia.magiccard.Util.PageUtil;
import com.zia.magiccard.Util.PushUtil;
import com.zia.magiccard.Util.RecyclerItemDivider;
import com.zia.magiccard.View.Fragments.RecyclerViewImp;

public class GroupActivity extends BaseActivity implements RecyclerViewImp {

    private MyRecyclerView recyclerView;
    private UserRecyclerAdapter adapter;
    private RecyclerViewPresenterImp presenter;
    private TextView textView;

    @Override
    protected void onCreated() {
        presenter.setRecyclerView();
        recyclerView.setExtraViewId(R.id.item_person_sendCard);
        recyclerView.addItemDecoration(new RecyclerItemDivider(this, RecyclerItemDivider.VERTICAL_LIST));
        final int p = getIntent().getIntExtra("position", -1);
        if (p != -1) {
            adapter.refreshData(MainActivity.classifyDatas.get(p).getUserDatas());
            textView.setText(MainActivity.classifyDatas.get(p).getClassName());
        }
        recyclerView.setMyListener(new MyRecyclerView.MyListener() {
            @Override
            public void onItemClick(View view, int position) {
                PageUtil.gotoChatPage(getActivity(), MainActivity.classifyDatas.get(p).getUserDatas().get(position), view);
            }

            @Override
            public void onDeleteClick(final int position) {
                new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("是否删除该好友及聊天记录？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String friendId = MainActivity.classifyDatas.get(p).getUserDatas().get(position).getObjectId();
                                //删除好友
                                MainActivity.classifyDatas.get(p).getUserDatas().remove(position);
                                adapter.refreshData(MainActivity.classifyDatas.get(p).getUserDatas());
                                //删除聊天记录
                                int po = ConversationUtil.getPositionByFriendId(friendId);
                                if(po != -1){
                                    MainActivity.conversationList.remove(po);
                                }
                                PushUtil.pushClassifyData();
                                PushUtil.saveConversations();
                            }
                        })
                        .setNegativeButton("否", null).show();
            }
        });
    }

    @Override
    protected void findWidgets() {
        presenter = new RecyclerViewPresenter(this);
        recyclerView = $(R.id.group_recycler);
        adapter = new UserRecyclerAdapter(this);
        textView = $(R.id.group_classify);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_group;
    }

    @Override
    protected void beforeSetContentView() {

    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }
}
