package com.zia.magiccard.Presenter;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.zia.magiccard.View.ChatActivity;
import com.zia.magiccard.View.Fragments.MessageFragmentImp;
import com.zia.magiccard.View.MainActivity;

/**
 * Created by zia on 17-8-17.
 */

public class MessagePresenter implements MessageImp {

    private MessageFragmentImp fragmentImp;

    public MessagePresenter(MessageFragmentImp fragmentImp){
        this.fragmentImp = fragmentImp;
    }

    @Override
    public void gotoChatActivity(View view) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(fragmentImp.getActivity(), view, "card");
        fragmentImp.getActivity().startActivity(new Intent(fragmentImp.getActivity(),ChatActivity.class), optionsCompat.toBundle());
    }

    @Override
    public void setRecyclerView() {
        fragmentImp.getRecyclerView().setLayoutManager(new LinearLayoutManager(fragmentImp.getActivity()));
        fragmentImp.getRecyclerView().setAdapter(fragmentImp.getAdapter());
    }
}
