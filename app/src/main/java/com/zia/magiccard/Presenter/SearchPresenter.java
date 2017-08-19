package com.zia.magiccard.Presenter;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.view.View;

import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.Model.UserModel;
import com.zia.magiccard.Util.PageUtil;
import com.zia.magiccard.Util.ScreenUtil;
import com.zia.magiccard.View.ChatActivity;
import com.zia.magiccard.View.SearchActivityImp;

import java.util.List;

/**
 * Created by zia on 17-8-19.
 */

public class SearchPresenter implements SearchPresenterImp {

    private SearchActivityImp imp;
    private UserModel model;

    public SearchPresenter(SearchActivityImp imp){
        this.imp = imp;
        model = new UserModel(imp.getActivity());
    }

    @Override
    public void moveEditText() {
        float curTranslationX = imp.getEditLayout().getTranslationX();
        float width = imp.getEditLayout().getWidth();
        float moveWidth = (width - ScreenUtil.dip2px(imp.getActivity(),60))/2;
        ObjectAnimator animator = ObjectAnimator.ofFloat(imp.getEditLayout(),"translationX",curTranslationX,-moveWidth);
        animator.setDuration(500);
        animator.start();
    }

    @Override
    public void searchUser() {
        if(imp.getEditText().getText() == null) return;
        String nickname = imp.getEditText().getText().toString();
        model.SearchUserByNickName(nickname, new UserModel.OnUserGetListener() {
            @Override
            public void getUserList(List<UserData> userDataList) {
                imp.getPersonAdapter().refreshData(userDataList);
            }
        });
    }

    @Override
    public void gotoChatPage(UserData userData, View view) {
        Intent intent = new Intent(imp.getActivity(), ChatActivity.class);
        intent.putExtra("userData",userData);
        PageUtil.gotoPageWithCard(imp.getActivity(),view,intent);
    }
}
