package com.zia.magiccard.Presenter;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.zia.magiccard.View.ChatActivity;
import com.zia.magiccard.View.Fragments.RecyclerViewImp;

/**
 * Created by zia on 17-8-18.
 * recyclerView统一适配器
 */

public class RecyclerViewPresenter implements RecyclerViewPresenterImp {

    private RecyclerViewImp imp;

    public RecyclerViewPresenter(RecyclerViewImp imp){
        this.imp = imp;
    }

    @Override
    public void setRecyclerView() {
        imp.getRecyclerView().setLayoutManager(new LinearLayoutManager(imp.getActivity()));
        imp.getRecyclerView().setAdapter(imp.getAdapter());
    }

    @Override
    public void setReverseRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(imp.getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        imp.getRecyclerView().setLayoutManager(linearLayoutManager);
        imp.getRecyclerView().setAdapter(imp.getAdapter());
    }


}
