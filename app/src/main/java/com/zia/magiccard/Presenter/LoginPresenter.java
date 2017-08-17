package com.zia.magiccard.Presenter;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
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
        if(activityImp.getUsername().getEditText() == null || activityImp.getPassword().getEditText() == null) return;
        //更改等待文字，设置为不可点击
        setButtonWait();
        //初始化错误提示
        activityImp.getUsername().setErrorEnabled(false);
        activityImp.getPassword().setErrorEnabled(false);
        //获取表单
        String username = activityImp.getUsername().getEditText().getText().toString();
        String password = activityImp.getPassword().getEditText().getText().toString();
        //判断格式并设置错误提示
        if(username.isEmpty() || username.length() > 16){
            activityImp.getUsername().setError("用户名格式错误");
            setButtonReset();
            return;
        }else if (password.isEmpty() || password.length() < 6){
            activityImp.getPassword().setError("密码格式错误");
            setButtonReset();
            return;
        }
        //登录
        AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if(e == null){
                    gotoMainActivity();
                    activityImp.getActivity().finish();
                }else{
                    //恢复为登录字样
                    setButtonReset();
                    activityImp.toast("账号或密码错误");
                }
            }
        });
    }

    private void setButtonWait(){
        activityImp.getButton().setText(activityImp.getWaitTitle());
        activityImp.getButton().setClickable(false);
    }

    private void setButtonReset(){
        activityImp.getButton().setText(activityImp.getActivity().getResources().getString(R.string.login));
        activityImp.getButton().setClickable(true);
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
