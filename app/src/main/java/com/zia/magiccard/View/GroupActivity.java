package com.zia.magiccard.View;

import android.support.v7.widget.RecyclerView;

import com.zia.magiccard.Adapter.PersonRecyclerAdapter;
import com.zia.magiccard.Base.BaseActivity;
import com.zia.magiccard.Presenter.RecyclerViewPresenter;
import com.zia.magiccard.Presenter.RecyclerViewPresenterImp;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.MyRecyclerView;
import com.zia.magiccard.Util.RecyclerItemDivider;
import com.zia.magiccard.View.Fragments.RecyclerViewImp;

public class GroupActivity extends BaseActivity implements RecyclerViewImp {

    private MyRecyclerView recyclerView;
    private PersonRecyclerAdapter adapter;
    private RecyclerViewPresenterImp presenter;

    @Override
    protected void onCreated() {
        presenter.setRecyclerView();
        recyclerView.setExtraViewId(R.id.item_person_sendCard);
        recyclerView.addItemDecoration(new RecyclerItemDivider(this,RecyclerItemDivider.VERTICAL_LIST));
    }

    @Override
    protected void findWidgets() {
        presenter = new RecyclerViewPresenter(this);
        recyclerView = $(R.id.group_recycler);
        adapter = new PersonRecyclerAdapter(this);
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
