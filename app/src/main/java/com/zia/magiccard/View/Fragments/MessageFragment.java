package com.zia.magiccard.View.Fragments;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.zia.magiccard.Adapter.MessageRecyclerAdapter;
import com.zia.magiccard.Base.BaseFragment;
import com.zia.magiccard.Presenter.MessageImp;
import com.zia.magiccard.Presenter.MessagePresenter;
import com.zia.magiccard.Presenter.RecyclerViewPresenter;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.MyRecyclerView;
import com.zia.magiccard.Util.RecyclerItemDivider;

/**
 * ----消息界面
 */
public class MessageFragment extends BaseFragment implements RecyclerViewImp {

    private RecyclerViewPresenter recyclerViewPresenter;
    private MessagePresenter messagePresenter;
    private MyRecyclerView recyclerView;
    private MessageRecyclerAdapter adapter;

    @Override
    protected void findWidgets() {
        recyclerViewPresenter = new RecyclerViewPresenter(this);
        adapter = new MessageRecyclerAdapter(getContext());
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
                recyclerViewPresenter.gotoChatActivity(view);
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
        return adapter;
    }
}
