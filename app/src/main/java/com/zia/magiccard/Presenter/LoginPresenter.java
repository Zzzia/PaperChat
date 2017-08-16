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

    private CheckUserImp activityImp;

    public LoginPresenter(CheckUserImp imp){
        this.activityImp = imp;
    }


    @Override
    public void login() {
        gotoMainActivity();
    }

    @Override
    public void gotoMainActivity() {
        View card = activityImp.getActivity().findViewById(R.id.login_card);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activityImp.getActivity(), card, card.getTransitionName());
        activityImp.getActivity().startActivity(new Intent(activityImp.getActivity(),MainActivity.class), optionsCompat.toBundle());
    }

    @Override
    public void gotoRegisterActivity() {
        View card = activityImp.getActivity().findViewById(R.id.login_card);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activityImp.getActivity(), card, card.getTransitionName());
        activityImp.getActivity().startActivity(new Intent(activityImp.getActivity(),RegisterActivity.class), optionsCompat.toBundle());
    }
}
