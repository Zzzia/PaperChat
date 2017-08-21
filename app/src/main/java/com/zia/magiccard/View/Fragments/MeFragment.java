package com.zia.magiccard.View.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.zia.magiccard.Base.BaseFragment;
import com.zia.magiccard.Presenter.MeFragmentPresenter;
import com.zia.magiccard.Presenter.MePresenterImp;
import com.zia.magiccard.R;
import com.zia.magiccard.View.ChangeActivity;
import com.zia.magiccard.View.LoginActivity;
import com.zia.magiccard.View.MainActivity;

public class MeFragment extends BaseFragment implements MeFragmentImp {

    private TextView loginOutButton,nickname,introduce;
    private CardView cardView;
    public static ImageView head;
    private MePresenterImp presenterImp;


    @Override
    protected void findWidgets() {
        loginOutButton = $(R.id.me_login_out);
        cardView = $(R.id.me_CardView);
        head = $(R.id.me_head);
        nickname = $(R.id.me_name);
        introduce = $(R.id.me_introduce);
        presenterImp = new MeFragmentPresenter(this);
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void onCreated() {
        presenterImp.initData();
        loginOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AVUser.logOut();
                presenterImp.gotoLoginActivity();
            }
        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenterImp.gotoChangeActivity();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        presenterImp.initData();
    }

    @Override
    public ImageView getHead() {
        return head;
    }

    @Override
    public TextView getNickname() {
        return nickname;
    }

    @Override
    public TextView getIntroduce() {
        return introduce;
    }
}
