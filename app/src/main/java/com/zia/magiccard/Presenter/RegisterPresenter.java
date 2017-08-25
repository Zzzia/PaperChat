package com.zia.magiccard.Presenter;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.zia.magiccard.R;
import com.zia.magiccard.View.CheckUserImp;
import com.zia.magiccard.View.MainActivity;
import com.zia.magiccard.View.RegisterActivity;

/**
 * Created by zia on 17-8-15.
 */

public class RegisterPresenter implements RegisterImp {

    private CheckUserImp activityImp;

    public RegisterPresenter(RegisterActivity activity){
        this.activityImp = activity;
    }

    @Override
    public void register() {
        if(activityImp.getUsername().getEditText() == null || activityImp.getPassword().getEditText() == null
                || activityImp.getNickname().getEditText() == null) return;
        //更改等待文字，设置为不可点击
        setButtonWait();
        //初始化错误提示
        activityImp.getUsername().setErrorEnabled(false);
        activityImp.getPassword().setErrorEnabled(false);
        activityImp.getNickname().setErrorEnabled(false);
        //获取表单
        final String username = activityImp.getUsername().getEditText().getText().toString();
        final String password = activityImp.getPassword().getEditText().getText().toString();
        String nickname = activityImp.getNickname().getEditText().getText().toString();
        //判断格式并设置错误提示
        if(username.isEmpty() || username.length() > 16){
            activityImp.getUsername().setError("用户名格式错误");
            setButtonReset();
            return;
        }else if (password.isEmpty() || password.length() < 6){
            activityImp.getPassword().setError("密码格式错误");
            setButtonReset();
            return;
        }else if(nickname.isEmpty()){
            activityImp.getNickname().setError("昵称不能为空");
            return;
        }
        //注册并登录
        AVUser newUser = new AVUser();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.put("nickname",nickname);
        String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
        if(installationId != null) newUser.put("installationId",installationId);
        newUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
                        @Override
                        public void done(AVUser avUser, AVException e) {
                            gotoMainActivity();
                            activityImp.getActivity().finish();
                        }
                    });
                }
                else{
                    setButtonReset();
                    activityImp.toast("该用户名已注册");
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
        View card = activityImp.getActivity().findViewById(R.id.register_card);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activityImp.getActivity(), card, card.getTransitionName());
        activityImp.getActivity().startActivity(new Intent(activityImp.getActivity(),MainActivity.class), optionsCompat.toBundle());
    }
}
