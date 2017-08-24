package com.zia.magiccard.View;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zia.magiccard.Base.BaseActivity;
import com.zia.magiccard.Presenter.LoginImp;
import com.zia.magiccard.Presenter.LoginPresenter;
import com.zia.magiccard.R;

public class LoginActivity extends BaseActivity implements CheckUserImp {

    private TextView registerButton,login;
    private TextInputLayout username,password;
    private LoginImp presenter;

    @Override
    protected void onCreated() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            presenter.gotoRegisterActivity();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.login();
            }
        });
    }

    @Override
    protected void findWidgets() {
        presenter = new LoginPresenter(this);
        registerButton = $(R.id.login_register);
        username = $(R.id.login_username);
        password = $(R.id.login_password);
        login = $(R.id.login_login);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void beforeSetContentView() {

    }

    @Override
    public TextInputLayout getUsername() {
        return username;
    }

    @Override
    public TextInputLayout getPassword() {
        return password;
    }

    @Override
    public TextInputLayout getNickname() {
        return null;
    }

    @Override
    public String getWaitTitle() {
        return "正在登录";
    }

    @Override
    public TextView getButton() {
        return login;
    }


}
