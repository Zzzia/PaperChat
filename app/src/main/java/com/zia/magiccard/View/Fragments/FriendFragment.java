package com.zia.magiccard.View.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zia.magiccard.Adapter.FriendRecyclerAdapter;
import com.zia.magiccard.Adapter.MessageRecyclerAdapter;
import com.zia.magiccard.Base.BaseFragment;
import com.zia.magiccard.Presenter.RecyclerViewPresenter;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.MyRecyclerView;

/**
 * 联系人fragment
 */
public class FriendFragment extends BaseFragment implements RecyclerViewImp {

    private RecyclerView recyclerView;
    private FriendRecyclerAdapter adapter;
    private RecyclerViewPresenter recyclerViewPresenter;

    @Override
    protected void findWidgets() {
        recyclerViewPresenter = new RecyclerViewPresenter(this);
        recyclerView = $(R.id.friend_recycler);
        adapter = new FriendRecyclerAdapter(getContext());
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_friend;
    }

    @Override
    protected void onCreated() {
        recyclerViewPresenter.setRecyclerView();
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
