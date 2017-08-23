package com.zia.magiccard.Presenter;

import android.animation.ObjectAnimator;
import android.view.View;

import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.Util.UserUtil;
import com.zia.magiccard.Util.PageUtil;
import com.zia.magiccard.Util.ScreenUtil;
import com.zia.magiccard.View.SearchActivityImp;

import java.util.List;

/**
 * Created by zia on 17-8-19.
 */

public class SearchPresenter implements SearchPresenterImp {

    private SearchActivityImp imp;
    private final static String TAG = "SearchPresenterTest";

    public SearchPresenter(SearchActivityImp imp){
        this.imp = imp;
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
        UserUtil.SearchUserByNickName(nickname, new UserUtil.OnUserGetListener() {
            @Override
            public void getUserList(List<UserData> userDataList) {
                imp.getPersonAdapter().refreshData(userDataList);
            }
        });
    }
}
