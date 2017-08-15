package com.zia.magiccard.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zia.magiccard.Base.BaseActivity;
import com.zia.magiccard.R;

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreated() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void findWidgets() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_start;
    }

}
