package com.zia.magiccard.View.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.zia.magiccard.Base.BaseFragment;
import com.zia.magiccard.R;
import com.zia.magiccard.View.ChangeActivity;
import com.zia.magiccard.View.LoginActivity;
import com.zia.magiccard.View.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends BaseFragment {

    private TextView loginOutButton;
    private CardView cardView;


    @Override
    protected void findWidgets() {
        loginOutButton = $(R.id.me_login_out);
        cardView = $(R.id.me_CardView);
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void onCreated() {
        loginOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AVUser.logOut();
                View root = getActivity().findViewById(R.id.me_root);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), root, "card");
                getActivity().startActivity(new Intent(getContext(),LoginActivity.class), optionsCompat.toBundle());
                getActivity().finish();
            }
        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, "card");
                getActivity().startActivity(new Intent(getContext(),ChangeActivity.class), optionsCompat.toBundle());
            }
        });
    }

}
