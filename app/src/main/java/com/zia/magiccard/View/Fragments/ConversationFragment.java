package com.zia.magiccard.View.Fragments;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.zia.magiccard.Base.BaseFragment;
import com.zia.magiccard.Presenter.MessagePresenterImp;
import com.zia.magiccard.Presenter.MessagePresenterPresenter;
import com.zia.magiccard.Presenter.RecyclerViewPresenter;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.MyRecyclerView;
import com.zia.magiccard.Util.RecyclerItemDivider;
import com.zia.magiccard.View.MainActivity;

/**
 * ----消息界面
 */
public class ConversationFragment extends BaseFragment implements RecyclerViewImp {

    private RecyclerViewPresenter recyclerViewPresenter;
    private MessagePresenterImp messagePresenter;
    private MyRecyclerView recyclerView;
    private static final String TAG = "MessageFragmentTest";

    @Override
    protected void findWidgets() {
        recyclerViewPresenter = new RecyclerViewPresenter(this);
        messagePresenter = new MessagePresenterPresenter(this);
        recyclerView = $(R.id.message_recycler);
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
                Log.d(TAG,position+"");
                Log.d(TAG,MainActivity.conversationList.get(position).toString());
                messagePresenter.gotoChatPage(MainActivity.conversationList.get(position),view);
            }

            @Override
            public void onDeleteClick(int position) {

            }
        });
    }

    @Override
    public RecyclerView getRecyclerView() {

        return recyclerView;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        if(MainActivity.adapter == null){
            Log.d(TAG,"MainActivity.adapter == null");
        }
        return MainActivity.adapter;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.adapter.freshMessageList(MainActivity.conversationList);
    }
}
