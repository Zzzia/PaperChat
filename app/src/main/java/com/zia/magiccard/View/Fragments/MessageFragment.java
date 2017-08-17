package com.zia.magiccard.View.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zia.magiccard.Adapter.MessageRecyclerAdapter;
import com.zia.magiccard.Base.BaseFragment;
import com.zia.magiccard.Presenter.MessageImp;
import com.zia.magiccard.Presenter.MessagePresenter;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.MyRecyclerView;
import com.zia.magiccard.View.ChatActivity;
import com.zia.magiccard.View.MainActivity;

/**
 * ----消息界面
 */
public class MessageFragment extends BaseFragment implements MessageFragmentImp {

    private Button button;
    private MessageImp presenter;
    private CardView cardView;
    private MyRecyclerView recyclerView;
    private MessageRecyclerAdapter adapter;

    @Override
    protected void findWidgets() {
        presenter = new MessagePresenter(this);
        adapter = new MessageRecyclerAdapter(getContext());
        button = $(R.id.message_button);
        recyclerView = $(R.id.message_recycler);
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void onCreated() {
        presenter.setRecyclerView();
        recyclerView.setMyListener(new MyRecyclerView.MyListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("123","click");
            }

            @Override
            public void onDeleteClick(int position) {
                Log.d("123","deleteClick");
            }
        });
    }

    @Override
    public CardView getCardView() {
        return cardView;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public MessageRecyclerAdapter getAdapter() {
        return adapter;
    }
}
