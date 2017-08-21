package com.zia.magiccard.View.Fragments;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zia.magiccard.Adapter.ClassifyRecyclerAdapter;
import com.zia.magiccard.Base.BaseFragment;
import com.zia.magiccard.Presenter.RecyclerViewPresenter;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.MyRecyclerView;

/**
 * 联系人fragment
 */
public class FriendFragment extends BaseFragment implements RecyclerViewImp {

    private MyRecyclerView recyclerView;
    private ClassifyRecyclerAdapter adapter;
    private RecyclerViewPresenter recyclerViewPresenter;

    @Override
    protected void onCreated() {
        recyclerViewPresenter.setRecyclerView();
        recyclerView.setExtraViewId(R.id.item_class_delete);
        recyclerView.setMyListener(new MyRecyclerView.MyListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onDeleteClick(int position) {

            }
        });
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
