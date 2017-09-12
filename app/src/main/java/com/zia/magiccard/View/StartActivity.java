package com.zia.magiccard.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.zia.magiccard.Base.BaseActivity;
import com.zia.magiccard.Presenter.StartImp;
import com.zia.magiccard.Presenter.StartPresenter;
import com.zia.magiccard.R;

public class StartActivity extends BaseActivity {

    private StartImp imp;

    @Override
    protected void onCreated() {
        if(AVUser.getCurrentUser() == null){
            imp.openService(null);
            imp.gotoLoginPage();
        }else{
            imp.openService(new AVIMClientCallback() {
                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                    imp.gotoMainPage();
                    //保存推送所需要的信息
                    imp.saveInstallationId();
                }
            });
        }
    }

    @Override
    protected void findWidgets() {
        imp = new StartPresenter(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_start;
    }

    @Override
    protected void beforeSetContentView() {

    }

}
