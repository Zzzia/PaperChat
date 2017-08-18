package com.zia.magiccard.Presenter;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.zia.magiccard.Adapter.MessageRecyclerAdapter;
import com.zia.magiccard.Base.BaseImp;
import com.zia.magiccard.View.ChatActivity;
import com.zia.magiccard.View.Fragments.RecyclerViewImp;

/**
 * Created by zia on 17-8-17.
 */

public class MessagePresenter implements MessageImp {

    private BaseImp fragmentImp;

    public MessagePresenter(BaseImp fragmentImp){
        this.fragmentImp = fragmentImp;
    }


}
