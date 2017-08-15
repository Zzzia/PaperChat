package com.zia.magiccard.View;

import android.support.design.widget.TextInputLayout;

import com.zia.magiccard.Base.BaseImp;

/**
 * Created by zia on 17-8-13.
 */

public interface CheckUserImp extends BaseImp {
    TextInputLayout getUsername();
    TextInputLayout getPassword();
}
