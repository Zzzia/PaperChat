package com.zia.magiccard.Presenter;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.bumptech.glide.Glide;
import com.zia.magiccard.R;
import com.zia.magiccard.View.ChangeActivity;
import com.zia.magiccard.View.Fragments.MeFragmentImp;
import com.zia.magiccard.View.LoginActivity;
import com.zia.magiccard.View.MainActivity;

/**
 * Created by zia on 17-8-21.
 */

public class MeFragmentPresenter implements MePresenterImp {

    private MeFragmentImp imp;

    public MeFragmentPresenter(MeFragmentImp imp){
        this.imp = imp;
    }

    @Override
    public void initData() {
        if(MainActivity.userData != null){
            Glide.with(imp.getActivity()).load(MainActivity.userData.getHeadUrl()).into(imp.getHead());
            imp.getIntroduce().setText(MainActivity.userData.getIntroduce());
            imp.getNickname().setText(MainActivity.userData.getNickname());
        }
    }

    @Override
    public void gotoLoginActivity() {
        View root = imp.getActivity().findViewById(R.id.me_root);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(imp.getActivity(), root, "card");
        imp.getActivity().startActivity(new Intent(imp.getActivity(),LoginActivity.class), optionsCompat.toBundle());
        imp.getActivity().finish();
    }

    @Override
    public void gotoChangeActivity() {
        View head = imp.getActivity().findViewById(R.id.me_CardView);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(imp.getActivity(), head, "card");
        imp.getActivity().startActivity(new Intent(imp.getActivity(),ChangeActivity.class), optionsCompat.toBundle());
    }
}
