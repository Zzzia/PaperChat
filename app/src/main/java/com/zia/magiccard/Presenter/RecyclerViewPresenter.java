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
    public void gotoChatActivity(View view) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(imp.getActivity(), view, "card");
        imp.getActivity().startActivity(new Intent(imp.getActivity(),ChatActivity.class), optionsCompat.toBundle());
    }

    @Override
    public void setRecyclerView() {
        imp.getRecyclerView().setLayoutManager(new LinearLayoutManager(imp.getActivity()));
        imp.getRecyclerView().setAdapter(imp.getAdapter());
    }
}
