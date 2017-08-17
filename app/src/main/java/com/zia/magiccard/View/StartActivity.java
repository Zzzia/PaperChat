package com.zia.magiccard.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.zia.magiccard.Base.BaseActivity;
import com.zia.magiccard.Presenter.StartImp;
import com.zia.magiccard.Presenter.StartPresenter;
import com.zia.magiccard.R;

public class StartActivity extends BaseActivity {

    private StartImp imp;

    @Override
    protected void onCreated() {
        imp.openService();
        if(AVUser.getCurrentUser() == null){
            imp.gotoLoginPage();
        }else{
            imp.gotoMainPage();
            //保存推送所需要的信息
            imp.saveInstallationId();
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
