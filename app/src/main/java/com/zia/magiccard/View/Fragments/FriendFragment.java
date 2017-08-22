package com.zia.magiccard.View.Fragments;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zia.magiccard.Adapter.ClassifyRecyclerAdapter;
import com.zia.magiccard.Base.BaseFragment;
import com.zia.magiccard.Presenter.RecyclerViewPresenter;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.MyRecyclerView;
import com.zia.magiccard.Util.RecyclerItemDivider;

/**
 * 联系人fragment
 */
public class FriendFragment extends BaseFragment implements RecyclerViewImp {

    private RecyclerView recyclerView;
    public static ClassifyRecyclerAdapter adapter;
    private RecyclerViewPresenter recyclerViewPresenter;

    @Override
    protected void onCreated() {
        recyclerViewPresenter.setRecyclerView();
        recyclerView.addItemDecoration(new RecyclerItemDivider(getContext(),RecyclerItemDivider.VERTICAL_LIST));
    }

    @Override
    protected void findWidgets() {
        recyclerViewPresenter = new RecyclerViewPresenter(this);
        recyclerView = $(R.id.friend_recycler);
        adapter = new ClassifyRecyclerAdapter(getContext());
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_friend;
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
