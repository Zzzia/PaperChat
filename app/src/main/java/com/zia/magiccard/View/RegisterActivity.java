package com.zia.magiccard.View;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zia.magiccard.Base.BaseActivity;
import com.zia.magiccard.Presenter.RegisterImp;
import com.zia.magiccard.Presenter.RegisterPresenter;
import com.zia.magiccard.R;

public class RegisterActivity extends BaseActivity implements CheckUserImp {

    private TextView registerButton;
    private TextInputLayout username,password,nickname;
    private RegisterImp presenter;

    @Override
    protected void onCreated() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.register();
            }
        });
    }

    @Override
    protected void findWidgets() {
        presenter = new RegisterPresenter(this);
        username = $(R.id.register_username);
        password = $(R.id.register_password);
        nickname = $(R.id.register_nickname);
        registerButton = $(R.id.register_register);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
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
        return nickname;
    }

    @Override
    public String getWaitTitle() {
        return "正在注册";
    }

    @Override
    public TextView getButton() {
        return registerButton;
    }
}
