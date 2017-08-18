package com.zia.magiccard.View.Fragments;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import com.zia.magiccard.Adapter.MessageRecyclerAdapter;
import com.zia.magiccard.Base.BaseImp;

/**
 * Created by zia on 17-8-17.
 */

public interface RecyclerViewImp extends BaseImp {
    RecyclerView getRecyclerView();
    RecyclerView.Adapter getAdapter();
}
