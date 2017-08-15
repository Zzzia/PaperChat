package com.zia.magiccard.Presenter;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.zia.magiccard.R;
import com.zia.magiccard.View.CheckUserImp;
import com.zia.magiccard.View.MainActivity;
import com.zia.magiccard.View.RegisterActivity;

/**
 * Created by zia on 17-8-13.
 */

public class LoginPresenter implements LoginImp {

    private CheckUserImp activity;

    public LoginPresenter(CheckUserImp imp){
        this.activity = imp;
    }


    @Override
    public void login() {
        gotoMainActivity();
    }

    @Override
    public void gotoMainActivity() {
        View card = activity.getActivity().findViewById(R.id.login_card);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity.getActivity(), card, card.getTransitionName());
        activity.getActivity().startActivity(new Intent(activity.getActivity(),MainActivity.class), optionsCompat.toBundle());
    }

    @Override
    public void gotoRegisterActivity() {
        View card = activity.getActivity().findViewById(R.id.login_card);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity.getActivity(), card, card.getTransitionName());
        activity.getActivity().startActivity(new Intent(activity.getActivity(),RegisterActivity.class), optionsCompat.toBundle());
    }
}
