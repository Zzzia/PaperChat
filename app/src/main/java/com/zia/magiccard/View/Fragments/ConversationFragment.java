package com.zia.magiccard.View.Fragments;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.zia.magiccard.Adapter.ConversationRecyclerAdapter;
import com.zia.magiccard.Base.BaseFragment;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Presenter.MessagePresenterImp;
import com.zia.magiccard.Presenter.MessagePresenter;
import com.zia.magiccard.Presenter.RecyclerViewPresenter;
import com.zia.magiccard.Presenter.RecyclerViewPresenterImp;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.MyRecyclerView;
import com.zia.magiccard.Util.PageUtil;
import com.zia.magiccard.Util.RecyclerItemDivider;
import com.zia.magiccard.View.ChatActivity;
import com.zia.magiccard.View.MainActivity;


/**
 * ----消息界面
 */
public class ConversationFragment extends BaseFragment implements RecyclerViewImp {

    private RecyclerViewPresenterImp recyclerViewPresenter;
    private MessagePresenterImp messagePresenter;
    private MyRecyclerView recyclerView;
    private static final String TAG = "ConversationTest";

    @Override
    protected void findWidgets() {
        recyclerViewPresenter = new RecyclerViewPresenter(this);
        messagePresenter = new MessagePresenter(this);
        recyclerView = $(R.id.message_recycler);
        MainActivity.conversationRecyclerAdapter = new ConversationRecyclerAdapter(getContext());
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void onCreated() {
        recyclerViewPresenter.setRecyclerView();
        recyclerView.setExtraViewId(R.id.item_message_delete);
        recyclerView.addItemDecoration(new RecyclerItemDivider(getContext(),RecyclerItemDivider.VERTICAL_LIST));
        recyclerView.setMyListener(new MyRecyclerView.MyListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG,"click position:" + position);
                MainActivity.conversations.get(position).read();
                MainActivity.conversationRecyclerAdapter.pullConversationList();
                Intent intent = new Intent(getContext(), ChatActivity.class);
                ConversationData conversationData = new ConversationData();
                conversationData.setMembers(MainActivity.conversations.get(position).getMembers());
                conversationData.setConversationId(MainActivity.conversations.get(position).getConversationId());
                intent.putExtra("conversationData",conversationData);
                PageUtil.gotoPageWithCard(getContext(),view,intent);
            }

            @Override
            public void onDeleteClick(int position) {
                Log.d(TAG,"delete conversation "+position);
                messagePresenter.deleteConversation(position);
            }
        });
        if(MainActivity.conversations.size() != 0) MainActivity.conversationRecyclerAdapter.freshMessageList();
        else MainActivity.conversationRecyclerAdapter.pullConversationList();
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        if(MainActivity.conversationRecyclerAdapter == null){
            Log.d(TAG,"MainActivity.conversationRecyclerAdapter == null");
        }
        return MainActivity.conversationRecyclerAdapter;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MainActivity.conversations.size() != 0) MainActivity.conversationRecyclerAdapter.freshMessageList();
        else MainActivity.conversationRecyclerAdapter.pullConversationList();
    }
}
