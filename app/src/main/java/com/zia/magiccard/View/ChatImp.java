package com.zia.magiccard.View;

import android.widget.EditText;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.zia.magiccard.Base.BaseImp;
import com.zia.magiccard.Bean.UserData;

/**
 * Created by zia on 17-8-17.
 */

public interface ChatImp extends BaseImp {
    UserData getUserData();
    EditText getEditText();
    AVIMClient getAVIMClient();
}
